package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.M_MOBILE_ID_KEY;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class M_MOBILE_ID extends BaseEntity {

    @EmbeddedId
    private M_MOBILE_ID_KEY key;

    //@MapsId
    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "USR_ID", nullable = false)
    //private M_USR userId;
    @Size(max = 3)
    @Column(name = "IDENTITY_CD", nullable = false, length = 3)
    private String identityCode;

    @Size(max = 200)
    @Column(name = "ADDR", length = 200)
    private String address;

    @Column(name = "CC_CTZ_YN", length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn chuncheonYn;

    @Column(name = "BKMK_YN", length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn bookmarkYn;

    @Setter
    @Column(name = "DSPL_SQNO")
    private Integer displaySequence;

    @Size(max = 20)
    @Column(name = "IDENTITY_TITLE_CD", length = 20)
    private String identityTitleCode;

    @Size(max = 100)
    @Column(name = "IDENTITY_NM", length = 100)
    private String identityName;

    @Size(max = 20)
    @NotNull
    @Column(name = "APRV_TYPE_CD", nullable = false, length = 20)
    private String approvalTypeCode;

    @Size(max = 20)
    @NotNull
    @Column(name = "ISSUED_STTS_CD", nullable = false, length = 20)
    private String issuedStatusCode;

    @Convert(disableConversion = true)
    @Column(name = "APRV_DT")
    private LocalDateTime approvalDate;

    @Convert(disableConversion = true)
    @Column(name = "VALID_DT")
    private LocalDateTime validDate;

    //@Size(max = 100)
    //@Column(name = "USR_NM", length = 100)
    //private String userName;

    //@Size(max = 512)
    //@Column(name = "CERT_ID", length = 512)
    //private String certificationId;

    @Size(max = 30)
    @Column(name = "HP_NO", length = 30)
    private String mobilePhoneNumber;

    //@Size(max = 8)
    //@Column(name = "BRDT", length = 8)
    //private String birthDate;

    @Size(max = 200)
    @Column(name = "RMRK")
    private String remark;

    @Size(max = 36)
    @Column(name = "PROC_USR_ID", length = 36)
    private String processUserId;

    @Builder
    public M_MOBILE_ID(M_MOBILE_ID_KEY key, String identityCode, String address, UseYn chuncheonYn,
            UseYn bookmarkYn, Integer displaySequence, String identityTitleCode, String identityName,
            String approvalTypeCode, String issuedStatusCode, LocalDateTime approvalDate,
            LocalDateTime validDate, String mobilePhoneNumber, String remark, String processUserId) {
        this.key = key;
        this.identityCode = identityCode;
        this.address = address;
        this.chuncheonYn = chuncheonYn;
        this.bookmarkYn = bookmarkYn;
        this.displaySequence = displaySequence;
        this.identityTitleCode = identityTitleCode;
        this.identityName = identityName;
        this.approvalTypeCode = approvalTypeCode;
        this.issuedStatusCode = issuedStatusCode;
        this.approvalDate = approvalDate;
        this.validDate = validDate;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.remark = remark;
        this.processUserId = processUserId;
    }

    //@Convert(disableConversion = true)
    //@Column(name = "CRT_DT")
    //private LocalDateTime createDate;
    //
    //@Convert(disableConversion = true)
    //@Column(name = "UPDT_DT")
    //private LocalDateTime updateDate;

    //@Builder
    //public M_MOBILE_ID(M_MOBILE_ID_KEY key) {
    //    this.key              = key;
    //    this.chuncheonYn      = UseYn.N;
    //    this.bookmarkYn       = UseYn.N;
    //    this.approvalTypeCode = "AUTO";
    //    this.statusCode       = Status.REQUESTED.name();
    //    this.processUserId    = "SYSTEM";
    //}

    public void updateMobileIdInformation(
            String address,
            UseYn chuncheonYn,
            String identityName,
            String identityTitleCode,
            String issuedStatusCode,
            LocalDateTime approvalDate,
            LocalDateTime validDate,
            String processUserId,
            String mobilePhoneNumber
            ) {
        this.address = address;
        this.chuncheonYn = chuncheonYn;
        this.identityName = identityName;
        this.identityTitleCode = identityTitleCode;
        this.issuedStatusCode = issuedStatusCode;
        this.approvalDate = approvalDate;
        this.validDate = validDate;
        this.processUserId = processUserId;
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public void updateBookmark(M_MOBILE_ID_KEY key, UseYn bookmarkYn) {
        this.key = key;
        this.bookmarkYn = bookmarkYn;
    }

    public void updateBookmarkSequence(M_MOBILE_ID_KEY key, Integer displaySequence) {
        this.key = key;
        this.displaySequence = displaySequence;
    }

    public void deleteMobileIdInformation(String issuedStatusCode) {
        this.issuedStatusCode = issuedStatusCode;
    }

    public void updateMobileIdOrder(UseYn bookmarkYn, Integer displaySequence) {
        this.bookmarkYn = bookmarkYn;
        this.displaySequence = displaySequence;
    }

    public void updateMobileIdStatus(String issuedStatusCode) {
        this.issuedStatusCode = issuedStatusCode;
    }

    public void updateRemark(String remark) {
        this.remark = remark;
    }
}