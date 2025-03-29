package egovframework.kt.dxp.api.domain.manageNotice;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.file.AbstractFileHandler;
import egovframework.kt.dxp.api.common.file.FileStoragePathEnum;
import egovframework.kt.dxp.api.common.file.FileValidator;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.file.FileRepository;
import egovframework.kt.dxp.api.domain.manageNotice.record.FileUploadResponse;
import egovframework.kt.dxp.api.domain.manageNotice.record.NoticeCreateRequest;
import egovframework.kt.dxp.api.domain.manageNotice.record.NoticeCreateResponse;
import egovframework.kt.dxp.api.domain.manageNotice.record.NoticeDeleteRequest;
import egovframework.kt.dxp.api.domain.notice.NoticeMapper;
import egovframework.kt.dxp.api.domain.notice.NoticeRepository;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.M_NOTICE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerNoticeService {

    private final FileRepository fileRepository;
    private final MessageConfig messageConfig;
    private final NoticeRepository noticeRepository;
    private static final NoticeMapper noticeMapper = NoticeMapper.INSTANCE;
    private final AbstractFileHandler fileHandler;
    private final FileValidator fileValidator = new FileValidator();

    @Transactional
    public ItemResponse<NoticeCreateResponse> createNotice(NoticeCreateRequest parameter,
                                                           List<MultipartFile> fileList) throws IOException {
        if (parameter == null) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }
        M_NOTICE entity = noticeMapper.toEntity(parameter);
        saveFile(fileList, entity.getNoticeSequenceNumber());
        noticeRepository.save(entity);

        return ItemResponse.<NoticeCreateResponse>builder()
                .status(messageConfig.getCode(NormalCode.CREATE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.CREATE_SUCCESS))
                .item(noticeMapper.toCreateResponse(entity))
                .build();
    }

    @Transactional
    public ItemResponse<Long> deleteNotice(NoticeDeleteRequest parameter) {
        //M_NOTICE entity = noticeRepository.findById(parameter.noticeSequenceNumber())
        //        .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));
        //entity.getFileList().forEach(data -> {
        //    NormalFileRemover fileRemover = new NormalFileRemover(data.getFilePath(),
        //            data.getSaveFileName());
        //    fileRemover.remove();
        //});
        //noticeRepository.delete(entity);
        return ItemResponse.<Long>builder()
                .status(messageConfig.getCode(NormalCode.DELETE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.DELETE_SUCCESS))
                .item(1L)
                .build();
    }
    public void saveFile(List<MultipartFile> fileList, Integer noticeSequence)
            throws ServiceException, IOException {
        FileStoragePathEnum storagePathEnum = FileStoragePathEnum.NOTICE;
        List<FileUploadResponse> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fileList)) {
            List<L_FILE> saveEntityList = new ArrayList<>();
            for (MultipartFile multipartFile : fileList) {
                String saveFileName = UUID.randomUUID().toString();
                fileHandler.uploadFileWithFileName(multipartFile, saveFileName, storagePathEnum, null);

                String fileExtension = fileValidator.getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                Integer fileSequence = fileRepository.getMaxFileSequenceNumber();
                L_FILE entity = L_FILE.builder()
//                        .fileSequenceNumber(fileSequence + 1)
                        .actualFileName(FilenameUtils.getBaseName(multipartFile.getOriginalFilename()))
                        .saveFileName(saveFileName)
                        .filePath(storagePathEnum.getStoragePath(null))
                        .fileExtension(fileExtension)
                        .fileSize(multipartFile.getSize())
                        .bulletinBoardSequenceNumber(noticeSequence)
                        .bulletinBoardDivision("01").build();

                saveEntityList.add(entity);
//                list.add(new FileUploadResponse(fileSequence, multipartFile.getOriginalFilename()));
            }
            fileRepository.saveAll(saveEntityList);
        }
    }


}
