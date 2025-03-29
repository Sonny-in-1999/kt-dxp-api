package egovframework.kt.dxp.api.domain.authority;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.entity.M_USR;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.DependsOn;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : CustomUserDetailsService
 * Description   :  
 * Creation Date : 2024-10-23
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-23, Juyoung Chae, 최초작성
 ******************************************************************************************/
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private static final String NO_AUTH = "AUTH000000";
    private final UserRepository userRepository;
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findOneByUserId(userId).map(user -> createUser(userId, user))
                                 .orElseThrow(() -> new UsernameNotFoundException(userId + " -> not found."));
    }

    /**
     * Security User 정보를 생성한다.
     *
     * @author GEONLEE
     * @since 2023-03-06<br />
     * 2023-06-05 GEONLEE - 기존에 복수권한을 갖을 수 있던 코드에서 단일 권한으로 수정<br />
     * 2024-03-27 GEONLEE - BadCredentialsException -> UnauthorizedException 으로 변경<br />
     * BadCredentialsException 은 JwtAuthenticationEntryPoint 로 전달되기 때문에 불필요 로직을 타게 됨.<br />
     **/
    private User createUser(String userId, M_USR mUsr) {
        if (mUsr.getCertificationId() == null) {
            throw new ServiceException(ErrorCode.FORBIDDEN, "");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + "USER"));

        LOGGER.info(userId + " / Authority : {}", grantedAuthorities);
        return new User(
                mUsr.getUserId(),
                mUsr.getPassword(),
                grantedAuthorities
        );
    }
}