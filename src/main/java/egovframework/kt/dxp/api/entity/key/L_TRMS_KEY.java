package egovframework.kt.dxp.api.entity.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * [이력 약관] Entity Key<br />
 *
 * @author BITNA
 * @since 2024-10-18<br />
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class L_TRMS_KEY implements Serializable {

    /* 약관 유형 */
    @Column(name = "TRMS_TYPE")
    private String termsType;

    /* 약관 시작 일시 */
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRMS_START_DT")
    private LocalDateTime termsStartDate;


}
