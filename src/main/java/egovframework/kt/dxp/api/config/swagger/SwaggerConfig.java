package egovframework.kt.dxp.api.config.swagger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author GEONLEE
 * @since 2024-09-11
 */
@Configuration
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
@EnableSwagger2
public class SwaggerConfig {

    private final String ACCESS_TOKEN_REFERENCE  = "Access Token(Bearer)";
    private final String REFRESH_TOKEN_REFERENCE = "Refresh Token";

    private final String ACCESS_TOKEN_HEADER_KEY = "Authorization";
    private final String REFRESH_TOKEN_HEADER_KEY = "RefreshToken";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("egovframework.kt.dxp.api.domain"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(accessTokenApiKey(), refreshTokenApiKey()))
                .securityContexts(Arrays.asList(securityContext()))
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "KT DX플랫폼 API",
                "API 설명",
                "1.0",
                "Terms of service",
                new Contact("이름", "웹사이트 URL", "이메일"),
                "License of API", "API license URL", Collections.emptyList());
    }

    private ApiKey accessTokenApiKey() {
        return new ApiKey(ACCESS_TOKEN_REFERENCE, ACCESS_TOKEN_HEADER_KEY, "header");
    }

    private ApiKey refreshTokenApiKey() {
        return new ApiKey(REFRESH_TOKEN_REFERENCE, REFRESH_TOKEN_HEADER_KEY, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                              .securityReferences(defaultAuth())
                              .forPaths(PathSelectors.any())
                              .build();
    }

    private List<SecurityReference> defaultAuth() {
        return Arrays.asList(
                new SecurityReference(ACCESS_TOKEN_REFERENCE, new AuthorizationScope[0]),
                new SecurityReference(REFRESH_TOKEN_REFERENCE, new AuthorizationScope[0])
        );
    }
}
