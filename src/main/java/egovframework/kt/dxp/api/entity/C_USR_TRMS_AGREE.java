package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.C_USR_TRMS_AGREE_KEY;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class C_USR_TRMS_AGREE {

    @EmbeddedId
    private C_USR_TRMS_AGREE_KEY key;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USR_ID", nullable = false)
    private M_USR mUsr;

    /* 동의 여부           */
    @NotNull
    @Column(name = "AGRE_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn agreementYn;

    /* 생성 일시           */
    @NotNull
    @Column(name = "CRT_DT", nullable = false)
    private LocalDateTime createDate;

    @Builder
    public C_USR_TRMS_AGREE(C_USR_TRMS_AGREE_KEY key, UseYn agreementYn,
            LocalDateTime createDate
            , M_USR mUsr) {
        this.key = key;
        this.agreementYn = agreementYn;
        this.createDate = createDate;
        this.mUsr = mUsr;
    }
}