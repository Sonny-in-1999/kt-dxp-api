package egovframework.kt.dxp.api.domain.user;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.M_USR;
import java.util.Optional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface UserRepository extends JpaDynamicRepository<M_USR, String> {
    Optional<M_USR> findOneByUserId(String userId);

    Optional<M_USR> findByCertificationId(String ci);

    Optional<M_USR> findByMobilePhoneNumber(String mobilePhoneNumber);

    Optional<M_USR> findOneByUserIdAndAccessToken(String userId, String accessToken);

    Optional<M_USR> findOneByUserIdAndRefreshToken(String userId, String refreshToken);

    Integer countByBirthDateBetween(String startDate, String endDate);
    Integer countByBirthDateBetweenAndGenderType(String startDate, String endDate, String genderType);
}