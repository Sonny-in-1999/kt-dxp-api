package egovframework.kt.dxp.api.entity.key;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class L_SURV_QSTN_ITEM_KEY implements Serializable {

    @NotNull
    @Column(name = "SURV_SQNO", nullable = false)
    private Integer surveySequenceNumber;

    @NotNull
    @Column(name = "QSTN_SQNO", nullable = false)
    private Integer questionSequenceNumber;

    @NotNull
    @Column(name = "ITEM_SQNO", nullable = false)
    private Integer itemSequenceNumber;


}