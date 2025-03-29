package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TB_TRX_INFO{

    @Id
    @Size(max = 50)
    @Column(name = "TRXCODE", nullable = false, length = 50)
    private String trxcode;

    @Size(max = 50)
    @NotNull
    @Column(name = "SVC_CODE", nullable = false, length = 50)
    private String svcCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "MODE", nullable = false, length = 50)
    private String mode;

    @Size(max = 100)
    @Column(name = "NONCE", length = 100)
    private String nonce;

    @Size(max = 100)
    @Column(name = "ZKP_NONCE", length = 100)
    private String zkpNonce;

    @Size(max = 1)
    @NotNull
    @Column(name = "VP_VERIFY_RESULT", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn vpVerifyResult;

    @Lob
    @Column(name = "VP")
    private String vp;

    @Size(max = 4)
    @NotNull
    @Column(name = "TRX_STS_CODE", nullable = false, length = 4)
    private String trxStsCode;

    @Column(name = "PROFILE_SEND_DT")
    private LocalDateTime profileSendDt;

    @Column(name = "IMG_SEND_DT")
    private LocalDateTime imgSendDt;

    @Column(name = "VP_RECEPT_DT")
    private LocalDateTime vpReceptDt;

    @Size(max = 4000)
    @Column(name = "ERROR_CN", length = 4000)
    private String errorCn;

    @NotNull
    @Column(name = "REG_DT", nullable = false)
    private LocalDateTime regDt;

    @Column(name = "UDT_DT")
    private LocalDateTime udtDt;
}