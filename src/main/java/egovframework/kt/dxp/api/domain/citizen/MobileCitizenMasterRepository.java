package egovframework.kt.dxp.api.domain.citizen;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.M_MOBILE_ID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.M_MOBILE_ID_KEY;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Repository
@DependsOn("applicationContextHolder")
public interface MobileCitizenMasterRepository extends JpaDynamicRepository<M_MOBILE_ID, M_MOBILE_ID_KEY> {
    Optional<M_MOBILE_ID> findTopByKeyUserIdAndIssuedStatusCodeOrderByCreateDateDesc(String userId, String statusCode);
    Optional<M_MOBILE_ID> findTopByKeyUserIdAndIdentityCodeAndIssuedStatusCodeOrderByCreateDateDesc(String userId, String identityCode, String statusCode);
    Optional<M_MOBILE_ID> findTopByKeyUserIdAndIdentityCodeAndIssuedStatusCodeAndApprovalTypeCodeOrderByCreateDateDesc(String userId, String identityCode, String statusCode, String approvalTypeCode);
    Optional<M_MOBILE_ID> findByKeyUserIdAndKeyTrxCode(String userId, String trxCode);
    List<M_MOBILE_ID> findByKeyUserIdAndKeyTrxCodeIn(String userId, List<String> trxCode);

    List<M_MOBILE_ID> findByKeyUserIdAndIssuedStatusCodeOrderByBookmarkYnDescDisplaySequenceAscCreateDateDesc(
            String userId,
            String statusCode
    );
    List<M_MOBILE_ID> findByKeyUserIdAndIssuedStatusCodeAndValidDateGreaterThanEqualOrderByBookmarkYnDescDisplaySequenceAscCreateDateDesc(
            String userId,
            String statusCode,
            LocalDateTime currentDateTime
    );

    List<M_MOBILE_ID> findByKeyUserIdAndIssuedStatusCodeInOrderByBookmarkYnDescDisplaySequenceDescCreateDateDesc(String userId, List<String> statusList);

    List<M_MOBILE_ID> findByKeyUserIdAndIssuedStatusCodeOrderByBookmarkYnDescCreateDateDescDisplaySequenceDesc(String userId, String statusCode);
}