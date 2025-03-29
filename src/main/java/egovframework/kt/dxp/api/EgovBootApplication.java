package egovframework.kt.dxp.api;

import egovframework.kt.dxp.api.common.jpa.querydsl.repository.JpaDynamicDslRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = "egovframework.kt.dxp.api.entity")
//@EnableJpaRepositories(repositoryBaseClass = JpaDynamicDslRepositoryImpl.class)
@EnableJpaRepositories(basePackages = {
        //"egovframework.kt.dxp.api.domain.*"
        "egovframework.kt.dxp.api.domain.alarm"
        , "egovframework.kt.dxp.api.domain.dong"
        , "egovframework.kt.dxp.api.domain.terms"
        , "egovframework.kt.dxp.api.domain.notice"
        , "egovframework.kt.dxp.api.domain.vote"
        , "egovframework.kt.dxp.api.domain.myPage"
        , "egovframework.kt.dxp.api.domain.interestMenu"
        , "egovframework.kt.dxp.api.domain.survey"
        , "egovframework.kt.dxp.api.domain.pushMessage"
        , "egovframework.kt.dxp.api.domain.user"
        , "egovframework.kt.dxp.api.domain.proposal"
        , "egovframework.kt.dxp.api.domain.code"
        , "egovframework.kt.dxp.api.domain.main"
        , "egovframework.kt.dxp.api.domain.welfare"
        , "egovframework.kt.dxp.api.domain.file"
        , "egovframework.kt.dxp.api.domain.citizen"
    }
, repositoryBaseClass = JpaDynamicDslRepositoryImpl.class
)
public class EgovBootApplication extends SpringBootServletInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EgovBootApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EgovBootApplication.class, args);
        LOGGER.info("Swagger : {}", "http://localhost:13340/kt-dxp/swagger-ui/index.html");
    }

}
