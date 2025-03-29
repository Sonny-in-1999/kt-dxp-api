package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.key.L_VOTE_ITEM_KEY;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;


/**
 * [이력 투표 항목] Entity<br />
 *
 * @author BITNA
 * @since 2024-10-31<br />
 */

@Getter
@Setter
@Entity
public class L_VOTE_ITEM {

    /* 키 */
    @EmbeddedId
    private L_VOTE_ITEM_KEY key;

    /* 정렬 순번 */
    @Column(name = "SORT_SQNO")
    private Integer sortSequenceNumber;

    /* 항목 */
    @Column(name = "ITEM")
    private String item;

//    @OneToOne(fetch = FetchType.LAZY)
//    @NotFound(action = NotFoundAction.IGNORE)
//    @JoinColumns({
//            @JoinColumn(name = "VOTE_SQNO", referencedColumnName = "VOTE_SQNO", insertable = false, updatable = false),
//            @JoinColumn(name = "ITEM_SQNO", referencedColumnName = "ITEM_SQNO", insertable = false, updatable = false)
//    })
//    private S_VOTE_RSLT voteResult;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @NotFound
//    @JoinColumn(name = "VOTE_SQNO", referencedColumnName = "VOTE_SQNO")
//    @MapsId("voteSequenceNumber")
//    private L_VOTE vote;
}
