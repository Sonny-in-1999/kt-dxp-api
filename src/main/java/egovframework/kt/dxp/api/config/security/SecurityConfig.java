package egovframework.kt.dxp.api.config.security;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : SecurityConfig
 * Description   :
 * Creation Date : 2024-10-22
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-22, Juyoung Chae, 최초작성
 ******************************************************************************************/

import egovframework.kt.dxp.api.common.CommonVariables;
import egovframework.kt.dxp.api.config.jwt.JwtAccessDeniedHandler;
import egovframework.kt.dxp.api.config.jwt.JwtAuthenticationEntryPoint;
import egovframework.kt.dxp.api.config.jwt.JwtFilter;
import egovframework.kt.dxp.api.config.jwt.TokenProvider;
import egovframework.kt.dxp.api.domain.authority.AuthTokenService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 인증처리와 401,403 에러 처리, 암호화 Security Filter
 *
 * @author GEONLEE
 * @since 2023-01-11
 * 2023-02-09 LCH 테스트용 파일 다운로드 권한 설정.<br />
 * 2023-03-21 GEONLEE preview AllPermit 추가<br />
 * 2023-04-08 GEONLEE antMatchers request CmmnVar 에서 배열로 가져와서 처리<br />
 * 2023-04-10 GEONLEE 코드 정리<br />
 * 2023-05-11 GEONLEE 생성자 주입 방식으로 변경 (코드 검증 때문)<br />
 * 2023-08-18 GEONLEE WebSecurityCustomizer 주석처리, 이유는 메서드 주석 참고<br />
 * 2023-10-17 GEONLEE Springboot 3.x update 로 인한 변경 처리<br />
 * 2024-03-15 GEONLEE spring 6.2 부터 apply 메서드 deprecated 로 방식 변경<br />
 * 2024-03-28 GEONLEE - JwtFilter 파라미터 수정 messageConfig 제외<br />
 * 2024-04-26 GEONLEE - passwordEncoder 변경, sha-256 + salt key<br />
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final AuthTokenService authTokenService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        return new DelegatingPasswordEncoder("SHA-256", encoders);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .authorizeHttpRequests(auth -> {
                    auth
                            .antMatchers(CommonVariables.IGNORE_URIS).permitAll()
                            .antMatchers(CommonVariables.SWAGGER_URIS).permitAll()
                            .anyRequest().authenticated();
                })
                .exceptionHandling(c ->
                        c.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler)
                ).sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(tokenProvider, authTokenService), UsernamePasswordAuthenticationFilter.class);
//                .apply(new JwtSecurityConfig(tokenProvider, messageConfig)); /*spring 6.2 deprecated*/
        return http.build();
    }
}