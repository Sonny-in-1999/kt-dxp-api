package egovframework.kt.dxp.api.common.file.nomal;

import egovframework.kt.dxp.api.common.file.FileDownloader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalFileDownloader implements FileDownloader {

    private final static Logger LOGGER = LoggerFactory.getLogger(NormalFileDownloader.class);
    private final String extension;
    private final String fileNameWithOutExtension;
    private final String saveFileName;
    private final String savePath;
    private final String actualFileName;
    private HttpServletResponse downloadResponse;

    public NormalFileDownloader(HttpServletResponse downloadResponse, String originFileName, String saveFileName, String savePath) {
        this.downloadResponse = downloadResponse;
        this.actualFileName = originFileName;
        this.saveFileName = saveFileName;
        this.savePath = savePath;

        this.extension = (originFileName.lastIndexOf(".") > 0)
                ? originFileName.substring(originFileName.lastIndexOf("."))
                : "";
        this.fileNameWithOutExtension =  originFileName.replace(this.extension, "");
    }

    public NormalFileDownloader(String originFileName, String saveFileName, String savePath) {
        this.actualFileName = originFileName;
        this.saveFileName = saveFileName;
        this.savePath = savePath;

        this.extension = (originFileName.lastIndexOf(".") > 0)
                ? originFileName.substring(originFileName.lastIndexOf("."))
                : "";
        this.fileNameWithOutExtension =  originFileName.replace(this.extension, "");
    }
    @Override
    public void download() {
        if(Files.exists(Paths.get(getFilePath()))) {
            LOGGER.info("File download. -> " + getFilePath());

            File downloadFile = Paths.get(getFilePath()).toFile();
            this.setResponseHeader(this.downloadResponse, this.fileNameWithOutExtension, this.extension);

            try (FileInputStream fileInputStream = new FileInputStream(downloadFile);
                    OutputStream outputStream = this.downloadResponse.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } catch (IOException e) {
                LOGGER.error("File Download Failed. -> " + getFilePath());
            }
        }
        else {
            LOGGER.error("File Not Exist. -> " + getFilePath());
        }
    }

    private String getFilePath() {
        return this.savePath.concat("/").concat(this.saveFileName);
    }

}

