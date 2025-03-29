package egovframework.kt.dxp.api.entity.key;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이력 설문 사용자 Entity key<br />
 *
 * @author MINJI
 * @since 2024-10-21<br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class L_SURV_USR_KEY implements Serializable {

    /* 설문 순번 */
    @Column(name = "SURV_SQNO")
    private Integer surveySequenceNumber;

    /* 질문 순번 */
    @Column(name = "QSTN_SQNO")
    private Integer questionSequenceNumber;

    /* 아이템 순번 */
    @Column(name = "ITEM_SQNO")
    private Integer itemSequenceNumber;

    /* 사용자 아이디 */
    @Column(name = "USR_ID")
    private String userId;


}