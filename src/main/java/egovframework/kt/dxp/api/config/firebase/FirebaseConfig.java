package egovframework.kt.dxp.api.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

/**
 * @author GEONLEE
 * @since 2024-09-11
 */
@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.service-account}")
    private String serviceAccount;

    @PostConstruct
    private void init() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = "account/ch-city-firebase-adminsdk-1iqvk-" + this.serviceAccount + ".json";
        log.info("Firebase Setting file : {}", path);
        try (InputStream input = classLoader.getResourceAsStream(path)) {
            if (input != null) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(input))
                        .build();
                FirebaseApp.initializeApp(options);
                log.info("Firebase init successfully.");
            }
        } catch (Exception e) {
            log.info("Firebase no init. already exists");
        }
    }
}
