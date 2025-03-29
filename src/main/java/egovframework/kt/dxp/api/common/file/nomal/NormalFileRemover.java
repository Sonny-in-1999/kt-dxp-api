package egovframework.kt.dxp.api.common.file.nomal;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.file.FileRemover;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalFileRemover implements FileRemover {

    private final static Logger LOGGER = LoggerFactory.getLogger(NormalFileRemover.class);

    private final String saveFileName;
    private final String savePath;

    public NormalFileRemover(String savePath, String saveFileName) {
        this.savePath = savePath;
        this.saveFileName = saveFileName;
    }

    @Override
    public void remove() {
        LOGGER.info("File Remove. -> " + saveFileName);

        Path filePath = Paths.get(getFilePath());
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            LOGGER.error("File Remove Failed. -> " + saveFileName);
            throw new ServiceException(ErrorCode.SERVICE_ERROR, "파일 삭제에 실패하였습니다.");
        }
    }

    private String getFilePath() {
        return this.savePath.concat("/").concat(this.saveFileName);
    }
}

