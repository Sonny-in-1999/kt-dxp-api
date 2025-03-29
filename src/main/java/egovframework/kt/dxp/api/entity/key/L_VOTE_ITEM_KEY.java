package egovframework.kt.dxp.api.entity.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


/**
 * [이력 투표 항목] Entity Key<br />
 *
 * @author BITNA
 * @since 2024-10-31<br />
 */

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class L_VOTE_ITEM_KEY implements Serializable {

    /* 투표 순번 */
    @Column(name = "VOTE_SQNO")
    private Integer voteSequenceNumber;

    /* 항목 순번 */
    @Column(name = "ITEM_SQNO")
    private Integer itemSequenceNumber;


}
