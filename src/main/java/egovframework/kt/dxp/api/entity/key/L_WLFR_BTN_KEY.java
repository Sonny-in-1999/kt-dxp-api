package egovframework.kt.dxp.api.entity.key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class L_WLFR_BTN_KEY implements Serializable {

    /* 복지 순번 */
    @Column(name = "WLFR_SQNO")
    private Integer welfareSequenceNumber;

    /* 버튼 순번 */
    @Column(name = "BTN_SQNO")
    private Integer buttonSequenceNumber;
}
