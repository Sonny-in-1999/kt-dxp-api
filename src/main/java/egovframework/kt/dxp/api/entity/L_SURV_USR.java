package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.key.L_SURV_USR_KEY;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

/**
 * 이력 설문 사용자 Entity<br />
 *
 * @author MINJI
 * @since 2024-10-21<br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class L_SURV_USR {

    /* 키 */
    @EmbeddedId
    @SearchableField(columnPath = {"key.surveySequenceNumber", "key.questionSequenceNumber",
            "key.itemSequenceNumber", "key.userId"})
    private L_SURV_USR_KEY key;

    /* 답변 */
    @Lob
    @Column(name = "ANS")
    private String answer;

    /* 생성 일시 */
    @Column(name = "CRT_DT")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Builder
    public L_SURV_USR(Integer surveySequenceNumber, Integer questionSequenceNumber, Integer itemSequenceNumber, String userId, String answer) {
        L_SURV_USR_KEY key = L_SURV_USR_KEY.builder()
                .surveySequenceNumber(surveySequenceNumber)
                .questionSequenceNumber(questionSequenceNumber)
                .itemSequenceNumber(itemSequenceNumber)
                .userId(userId)
                .build();
        this.answer = answer;
        this.key = key;
    }
}