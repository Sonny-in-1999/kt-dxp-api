package egovframework.kt.dxp.api.common.file;


import static egovframework.kt.dxp.api.common.CommonVariables.CONTEXT_PATH;
import static egovframework.kt.dxp.api.common.CommonVariables.FILE_BASE_PATH;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface FileUploader {

    Logger LOGGER = LoggerFactory.getLogger(FileUploader.class);

    void upload() throws ServiceException;

    /**
     * 현재 날짜 String return
     *
     * @author GEONLEE
     * @since 2024-06-03
     */
    default String getCurrentDateString() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    default String getSaveFilePath() throws ServiceException {
        String saveDirectory = FILE_BASE_PATH
                .concat(CONTEXT_PATH).concat("/")
                .concat(getCurrentDateString());
        Path saveDirectoryPath = Paths.get(saveDirectory);
        if (!Files.exists(saveDirectoryPath)) {
            try {
                Files.createDirectories(saveDirectoryPath);
            } catch (IOException e) {
                throw new ServiceException(ErrorCode.SERVICE_ERROR);
            }
        }
        return saveDirectory;
    }

    default String getSaveFileNameWithUUID(String originName) {
        return originName.concat("_" + UUID.randomUUID());
    }
}
