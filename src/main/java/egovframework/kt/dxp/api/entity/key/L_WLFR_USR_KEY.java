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
public class L_WLFR_USR_KEY implements Serializable {

    /* 복지 순번 */
    @Column(name = "WLFR_SQNO")
    private Integer welfareSequenceNumber;

    /* 사용자 아이디 */
    @Column(name = "USR_ID")
    private String userId;
}
