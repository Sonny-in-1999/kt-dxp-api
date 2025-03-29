package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.key.L_VOTE_USR_KEY;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 이력 투표 사용자 Entity<br />
 *
 * @author MINJI
 * @apiNote 2024-11-04 Builder 추가
 * @since 2024-10-21<br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class L_VOTE_USR {

    /* 키 */
    @EmbeddedId
    @SearchableField(columnPath = {"key.voteSequenceNumber", "key.itemSequenceNumber", "key.userId"})
    private L_VOTE_USR_KEY key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USR_ID", referencedColumnName = "USR_ID", insertable = false, updatable = false)
    private M_USR user;

    /* 생성 일시 */
    @Column(name = "CRT_DT")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Builder()
    public L_VOTE_USR(Integer voteSequenceNumber, Integer itemSequenceNumber, String userId) {
        L_VOTE_USR_KEY key = L_VOTE_USR_KEY.builder()
                .voteSequenceNumber(voteSequenceNumber)
                .itemSequenceNumber(itemSequenceNumber)
                .userId(userId)
                .build();
        this.key = key;
    }

}