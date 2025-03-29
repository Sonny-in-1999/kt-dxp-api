package egovframework.kt.dxp.api.entity.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * 이력 투표 사용자 Entity key<br />
 *
 * @author MINJI
 * @since 2024-10-21<br />
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class L_VOTE_USR_KEY implements Serializable {

    /* 투표 순번 */
    @Column(name = "VOTE_SQNO")
    private Integer voteSequenceNumber;

    /* 아이템 순번 */
    @Column(name = "ITEM_SQNO")
    private Integer itemSequenceNumber;

    /* 사용자 아이디 */
    @Column(name = "USR_ID")
    private String userId;

}