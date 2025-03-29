package egovframework.kt.dxp.api.common.file.nomal;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.file.FileUploader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class NormalFileUploader implements FileUploader {

    private final static Logger LOGGER = LoggerFactory.getLogger(NormalFileUploader.class);

    private final MultipartFile multipartFile;
    @Getter
    private final String fileName;
    @Getter
    private final String originFileName;
    @Getter
    private final String saveFileName;
    @Getter
    private final String savePath;
    @Getter
    private final Long fileSize;

    private final String extension;
    private final String fileNameWithOutExtension;

    public NormalFileUploader(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        this.fileName = multipartFile.getName();
        this.originFileName = multipartFile.getOriginalFilename();
        this.savePath = getSaveFilePath();
        this.fileSize = multipartFile.getSize();
        this.extension = (originFileName.lastIndexOf(".") > 0)
                ? originFileName.substring(originFileName.lastIndexOf("."))
                : "";
        this.fileNameWithOutExtension =  originFileName.replace(this.extension, "");
        this.saveFileName = getSaveFileNameWithUUID(this.fileNameWithOutExtension).concat(this.extension);
    }

    @Override
    public void upload() throws ServiceException {
        LOGGER.info("File upload. -> " + saveFileName);
        Path filePath = Paths.get(getFilePath());
        try {
            this.multipartFile.transferTo(filePath);
        } catch (IOException e) {
            LOGGER.error("File upload Failed. -> " + saveFileName);
            throw new ServiceException(ErrorCode.SERVICE_ERROR);
        }
    }

    private String getFilePath() {
        return this.savePath.concat("/").concat(this.saveFileName);
    }
}