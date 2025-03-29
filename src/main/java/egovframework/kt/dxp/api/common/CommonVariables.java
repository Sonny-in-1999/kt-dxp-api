package egovframework.kt.dxp.api.common;

import com.google.gson.Gson;
import egovframework.kt.dxp.api.common.contextHolder.ApplicationContextHolder;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : CommonVariables
 * Description   : 공통으로 사용할 변수만 설정
 * Creation Date : 2024-10-18
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-18, Juyoung Chae, 최초작성
 ******************************************************************************************/

/**
 * 공통 static class<br />
 * 공통으로 사용할 변수만 설정, method 는 commonUtils 로 이동<br />
 *
 * @author GEONLEE
 * @since 2022-11-07<br />
 * 2023-08-01 GEONLEE - 안쓰는 STATIC 정리<br />
 * 2023-10-17 GEONLEE - 클래스 명칭 변경 CmmnVar to CommonVariables<br />
 * 2024-03-29 GEONLEE - 대문자 변경 및 CONTEXT_PATH 추가<br />
 * 2024-04-02 MinJi - 디폴트 권한 id 추가<br />
 * 2024-10-24 BITNA - ignoreUri(약관 조회) 추가
 */
public class CommonVariables {
    /*SecurityConfig, JWTFilter 에서 사용*/
    public static final String[] IGNORE_URIS = {"/v1/user/validate","/v1/file/download", "/v1/dong/search", "/v1/nice/verification/**", "/v1/signup/login", "/v1/signup/custom/create", "/v1/signup/create", "/v1/version/latest", "/v1/login", "/v1/logout", "/v1/public-key", "/favicon.ico", "/error", "/v1/terms/search","/v1/terms/type/search", "/v1/terms/date/search", "/v1/terms/recent/search", "/v1/mileage/search"};
    //public static final String[] IGNORE_URIS = {"/v1/dong/search", "/v1/nice/verification/**", "/v1/signup/create", "/v1/version/latest", "/v1/login", "/v1/logout", "/v1/public-key", "/favicon.ico", "/error", "/v1/terms/search","/v1/terms/type/search", "/v1/terms/date/search", "/v1/terms/recent/search"};
    /*SecurityConfig 에서 사용*/
    public static final String[] SWAGGER_URIS = {"index.html", "swagger-ui.html", "/swagger-resources/**", "/swagger-ui/**", "/api-docs/**", "/v2/api-docs/**", "/webjars/**", "/swagger.json"};
    public static final String CONTEXT_PATH = CommonVariables.getPropertyValue("server.servlet.context-path");
    public static final String FILE_BASE_PATH = CommonVariables.getPropertyValue("file.base-path");
    public static final String FILE_UPLOAD_PATH = CommonVariables.getPropertyValue("file.upload-path");
    public static Gson GSON = new Gson();
    public static final String[] DEFAULT_AUTH_ID = {"AD_01"};   //default 권한 id
    /**
     * Property 값에 접근하는 @Value 는 spring bean 에서 사용하는 용도이므로, static class 나 다른 곳에서 property 값이 필요할 때 활용한다.
     *
     * @param key property key
     * @return string value
     * @author GEON LEE
     * @since 2023-08-08<br />
     * 2024-07-10 commonUtil -> variable 패키지로 이동
     */
    public static String getPropertyValue(String key) {
        return ApplicationContextHolder.getContext().getEnvironment().getProperty(key);
    }
}