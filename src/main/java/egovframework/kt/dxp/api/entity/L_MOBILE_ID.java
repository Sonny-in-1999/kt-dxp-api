package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.key.L_MOBILE_ID_KEY;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class L_MOBILE_ID extends BaseEntity {


    @EmbeddedId
    private L_MOBILE_ID_KEY key;

    @Size(max = 3)
    @Column(name = "IDENTITY_CD", nullable = false, length = 3)
    private String identityCode;

    @Size(max = 20)
    @Column(name = "IDENTITY_TITLE_CD", length = 20)
    private String identityTitleCode;

    @Size(max = 100)
    @Column(name = "IDENTITY_NM", length = 100)
    private String identityName;

    @Size(max = 20)
    @NotNull
    @Column(name = "ISSUED_STTS_CD", nullable = false, length = 20)
    private String statusCode;

    @Size(max = 200)
    @Column(name = "RMRK")
    private String remark;

    @Size(max = 36)
    @Column(name = "PROC_USR_ID", length = 36)
    private String processUserId;

    @Builder
    public L_MOBILE_ID(L_MOBILE_ID_KEY key, String identityCode, String identityTitleCode, String identityName,
            String statusCode, String remark, String processUserId) {
        this.key = key;
        this.identityCode = identityCode;
        this.identityTitleCode = identityTitleCode;
        this.identityName = identityName;
        this.statusCode = statusCode;
        this.remark = remark;
        this.processUserId = processUserId;
    }
}