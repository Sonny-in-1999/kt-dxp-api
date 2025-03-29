package egovframework.kt.dxp.api.domain.proposal;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.file.*;
import egovframework.kt.dxp.api.common.file.nomal.NormalFileRemover;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.request.DynamicRequest;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.code.CodeRepository;
import egovframework.kt.dxp.api.domain.file.FileRepository;
import egovframework.kt.dxp.api.domain.proposal.record.*;
import egovframework.kt.dxp.api.entity.C_PRPS;
import egovframework.kt.dxp.api.entity.L_FILE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 정책 제안 Service
 *
 * @author BITNA
 * @apiNote 2024-10-31 BITNA mapper -> builder 변환, max file size 지정
 * @apiNote 2024-11-01 BITNA fileList null check
 *                           totalSize 추가
 *                           정책조회 조건 추가(답변완료 +90일 이상 데이터 표출x)
 *          2024-11-15 BITNA 목록조회 nodata error 삭제
 * @since 2024-10-29<br />
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class ProposalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProposalService.class);
    private static final String PRPS_BBS_DIV = "02";
    private static final String PRPS_PRGS_DIV = "01";
    private static final long MAX_FILE_SIZE = 10 * 2024 * 2024;
    private final AbstractImageFilePreviewer imageFilePreviewer = new AbstractImageFilePreviewer();
    private final FilePreviewService filePreviewService = new FilePreviewService();
    private final AbstractFileHandler fileHandler;
    private final FileValidator fileValidator = new FileValidator();

    private final ProposalRepository proposalRepository;
    private final FileRepository fileRepository;
    private final CodeRepository codeRepository;
    private static final ProposalMapper proposalMapper = ProposalMapper.INSTANCE;
    private final MessageConfig messageConfig;

    /**
     * 정책 제안 정보 리스트 조회
     *
     * @param parameter 정책 제안 조회 조건
     * @return ProposalSearchResponse 정책 제안 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-29<br />
     */
    public ItemsResponse<ProposalSearchResponse> getProposalList(DynamicRequest parameter) {
        List<C_PRPS> proposalList = proposalRepository.findDynamic(parameter);
        List<C_PRPS> pageList = null;

        proposalList = proposalList.stream()
                .filter(entity -> entity.getAnswerCreateDate() == null
                        || entity.getAnswerCreateDate().isAfter(Converter.getCurrentLocalDateTime().minusDays(90))).toList();

        PageRequest pageRequest = PageRequest.of(parameter.pageNo(), parameter.pageSize());
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), proposalList.size());
        Page<C_PRPS> dtoPage = new PageImpl<>(proposalList.subList(start, end), pageRequest, proposalList.size());

        pageList = new ArrayList<C_PRPS>(dtoPage.getContent());

        return ItemsResponse.<ProposalSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(proposalMapper.toSearchResponseList(pageList))
                .totalSize((long) proposalList.size())
                .build();
    }

    /**
     * 정책 제안 상세정보 조회
     *
     * @param parameter 정책 제안 조회 조건
     * @return ProposalSearchResponse 정책 제안 상세정보 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-29<br />
     */
    public ItemResponse<ProposalDetailSearchResponse> getProposalDetail(ProposalSearchRequest parameter) {
        C_PRPS cPrps = proposalRepository.findById(parameter.proposalSequenceNumber()).orElseThrow(() ->
                new ServiceException(ErrorCode.NO_DATA));

        return ItemResponse.<ProposalDetailSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(proposalMapper.toDetailSearchResponse(cPrps))
                .build();
    }

    /**
     * 정책 제안 상세 - 이미지 조회
     *
     * @param parameter 정책 제안 조회 조건
     * @return ProposalSearchResponse 정책 제안 상세정보 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-29<br />
     */
    public ItemsResponse<ProposalFileSearchResponse> getProposalFile(ProposalSearchRequest parameter) {

        List<L_FILE> fileEntityList = fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumber(PRPS_BBS_DIV, parameter.proposalSequenceNumber().intValue());

        List<ProposalFileSearchResponse> fileSearchResponseList = fileEntityList.stream().map(entity -> {
//            String imageByte;
//            try {
//                imageByte = filePreviewService.getPreview(entity.getFilePath(), CommonUtils.combineFileNameAndExtension(entity.getSaveFileName(), entity.getFileExtension()));
//            } catch(IOException e) {
//                imageByte = null;
//            }

            return ProposalFileSearchResponse.builder()
                    .bulletinBoardDivision(PRPS_BBS_DIV)
                    .saveFileName(entity.getSaveFileName())
                    .fileName(entity.getActualFileName())
                    .fileExtension(entity.getFileExtension())
//                    .image(imageByte)
                    .build();
        }).toList();

        return ItemsResponse.<ProposalFileSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(fileSearchResponseList)
                .build();
    }

    /**
     * 정책 제안 정보 추가
     *
     * @param parameter 정책 제안 추가 요청 정보
     * @return ProposalCreateResponse 생성 된 정책 제안 정보 응답
     * @author BITNA
     * @since 2024-10-29<br />
     */
    @Transactional
    public ItemResponse<ProposalCreateResponse> createProposal(ProposalCreateRequest parameter, List<MultipartFile> fileList) {

        //TODO: 제안 구분 코드 유효성 검사
        List<String> proposalDivisionList = getProposalDivisionCombo().items().stream().map(response -> response.proposalDivision()).toList();
        if (!proposalDivisionList.contains(parameter.proposalDivision())) {
            LOGGER.error("Invalid parameter - proposalDivision: {}", parameter.proposalDivision());
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        Long maxSqeunceNumber = proposalRepository.getMaxProposalSequenceNumber() + 1;
        ProposalCreateRequest newRequest = parameter.setProposalSequenceNumber(maxSqeunceNumber);


        C_PRPS createdRequestEntity = C_PRPS.builder()
                .proposalSequenceNumber(maxSqeunceNumber)
                .proposalDivision(parameter.proposalDivision())
                .title(parameter.title())
                .backgroundContents(parameter.backgroundContents())
                .proposalContents(parameter.proposalContents())
                .expectEffect(parameter.expectEffect())
                .createUserId(CommonUtils.getUserId())
                .build();


        //TODO: file 저장
        if (fileList != null && fileList.size() > 0) {
            fileList.forEach(file -> {
                try {
                    saveFile(newRequest, file);
                } catch(IOException e) {
                    log.info("IOException", e);
                    throw new ServiceException(ErrorCode.SERVICE_ERROR);
                }
            });
        }

        C_PRPS createdEntity = proposalRepository.save(createdRequestEntity);

        return ItemResponse.<ProposalCreateResponse>builder()
                .status(messageConfig.getCode(NormalCode.CREATE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.CREATE_SUCCESS))
                .item(proposalMapper.toCreateResponse(createdEntity))
                .build();
    }

    private void saveFile(ProposalCreateRequest param, MultipartFile file) throws IOException {
        // TODO: fileSize check
        // 파일 크기 체크 로직(yml: max-file-size)
        if (file.getSize() > MAX_FILE_SIZE) {
            LOGGER.error("File size is too large. maxFileSize: {}, fileSize: {}", MAX_FILE_SIZE, file.getSize());
            throw new ServiceException(ErrorCode.SERVICE_ERROR);
        }
        String saveFileName = UUID.randomUUID().toString();

        FileStoragePathEnum storageProposal  = FileStoragePathEnum.PROPOSAL;
        FileStoragePathEnum storageThumbnail = FileStoragePathEnum.THUMBNAIL;

        fileHandler.uploadFileWithThumbnail(file, saveFileName, storageProposal, storageThumbnail, null);

        Long proposalSequenceNumber = param.proposalSequenceNumber();
//        String os = System.getProperty("os.name").toLowerCase();
//        String path = Path.of(CommonVariables.getPropertyValue("file.path"),
//                        "proposal",
//                        Converter.getCurrentDateTimeString(DateType.YYYYMMDD_FORMAT))
//                .toString();
//        String fileName = getFileNm(file);
////        String saveFileName = getRandomNm();
//        String ext = getExt(file);

        String fileExtension = fileValidator.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
//        Path saveFilePath = null;
//        if (os.contains("linux")) {
//            saveFilePath = Path.of(File.separator + path, saveFileName + "." + ext);
//        } else if (os.contains("win")) {
//            saveFilePath = Path.of("C:", path, saveFileName + "." + ext);
//        } else { //linux도 윈도우도 아닐경우. 그냥 상대경로로 저장.
//            saveFilePath = Path.of(path, saveFileName + "." + ext);
//        }
//
//        String saveFilePathDir = String.valueOf(saveFilePath.getParent());
//        String saveFilePathStr = String.valueOf(saveFilePath);
//        File dir = new File(saveFilePathDir);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        try (FileOutputStream outputStream = new FileOutputStream(saveFilePathStr)) {
//            outputStream.write(file.getBytes());
//        } catch (IOException e) {
//            LOGGER.error(e.getMessage(), e);
//            throw new ServiceException(ErrorCode.SERVICE_ERROR);
//        }

        // 파일 DB 저장
        L_FILE entity = L_FILE.builder()
                .actualFileName(FilenameUtils.getBaseName(file.getOriginalFilename()))
                .saveFileName(saveFileName)
                .filePath(storageProposal.getStoragePath(null))
                .thumbnailFilePath(storageThumbnail.getStoragePath(null))
                .fileExtension(fileExtension)
                .fileSize(file.getSize())
                .bulletinBoardSequenceNumber(proposalSequenceNumber.intValue())
                .fileDivision("C")
                .bulletinBoardDivision(PRPS_BBS_DIV).build();
        fileRepository.saveAndFlush(entity);
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);

        // 파일이 존재하는지 확인 후 삭제
        if (file.exists()) {
            if (!file.delete()) {
                LOGGER.error("Failed to delete file: {}", filePath);
                throw new ServiceException(ErrorCode.SERVICE_ERROR);
            }
        } else {
            LOGGER.error("File to delete does not exist: {}", filePath);
//            throw new ServiceException(ErrorCode.SERVICE_ERROR);
        }
    }

    /**
     * 파일의 이름을 UUID로 바꿈. 이름 중복방지.
     */
    private String getRandomNm() {
        return UUID.randomUUID().toString();
    }

    /**
     * 파일의 확장자를 return
     */
    private String getExt(MultipartFile file) {
        List<String> split = Arrays.asList(String.valueOf(file.getOriginalFilename()).split("\\."));
        return split.size() == 0 ? "" : split.get(split.size() - 1);
    }

    /**
     * 파일의 이름을 return
     */
    private String getFileNm(MultipartFile file) {
        List<String> split = Arrays.asList(String.valueOf(file.getOriginalFilename()).split("\\."));
        String now = Converter.getCurrentDateTimeString(DateType.YYYYMMDDHHMMSS);
        return split.size() == 0 ? now : split.get(0);
    }

    /**
     * 정책제안 구분 코드 콤보
     *
     * @return 정책제안 구분 코드 콤보
     * @author BITNA
     * @since 2024-10-30<br />
     */
    public ItemsResponse<ProposalComboResponse> getProposalDivisionCombo() {

        List<ProposalComboResponse> proposalDivisionList = proposalMapper.toComboResponseList(codeRepository.findByKeyGroupCodeId("PRPS_DIV"))
                .stream().sorted(Comparator.comparing(ProposalComboResponse::proposalDivision)).collect(Collectors.toList());

        return ItemsResponse.<ProposalComboResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(proposalDivisionList)
                .build();
    }

    /**
     * 정책 제안 정보 삭제
     *
     * @param parameter 정책 제안 삭제 요청 정보
     * @return Long 삭제된 데이터 개수
     * @author BITNA
     * @since 2024-10-29<br />
     */
    @Transactional
    public ItemResponse<Long> deleteProposal(ProposalDeleteRequest parameter) {
        C_PRPS entity = proposalRepository.findById(parameter.proposalSequenceNumber())
                .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_PARAMETER));

        // 삭제 권한 조회(본인글만 삭제 가능)
        String userId = CommonUtils.getUserId();
        if (!userId.equals(entity.getCreateUserId())) {
            LOGGER.error("No permission. createUser: {}, userId: {}", entity.getCreateUserId(), userId);
            throw new ServiceException(ErrorCode.NOT_AUTHENTICATION);
        }

        // 제안 진행 코드가 제안[01]일 때만 삭제 가능
        if (!entity.getProposalProgressDivision().equals(PRPS_PRGS_DIV)) {
            LOGGER.error("Cannot be deleted Data. proposalProgressDivision: {}", entity.getProposalProgressDivision());
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        //file 삭제
        List<L_FILE> fileList = fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumber(PRPS_BBS_DIV
                , parameter.proposalSequenceNumber().intValue());
        fileList.forEach(file -> new NormalFileRemover(file.getFilePath(),
                CommonUtils.combineFileNameAndExtension(file.getActualFileName(), file.getFileExtension())));
        fileRepository.deleteAll(fileList);

        proposalRepository.delete(entity);

        return ItemResponse.<Long>builder()
                .status(messageConfig.getCode(NormalCode.DELETE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.DELETE_SUCCESS))
                .item(1L)
                .build();
    }


}
