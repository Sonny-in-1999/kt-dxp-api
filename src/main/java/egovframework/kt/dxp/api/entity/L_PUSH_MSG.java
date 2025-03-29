package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.DefaultSort;
import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.common.request.enumeration.SortDirection;
import egovframework.kt.dxp.api.entity.code.MSG_TYPE;
import egovframework.kt.dxp.api.entity.enumeration.TransmissionDivision;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_PUSH_MSG_KEY;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

/**
 * 이력 푸시 메세지 Entity <br />
 *
 * @author MINJI
 * @since 2024-10-18<br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@NamedEntityGraph(
        name = "L_PUSH_MSG_GRAPH",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("messageTypeCode")
        }
)
@DefaultSort(columnName = "createDate", direction = SortDirection.DESC)
public class L_PUSH_MSG {

    /* L_PUSH_MSG key */
    @EmbeddedId
    @SearchableField(columnPath = {"key.userId", "key.createDate", "key.messageDivision"})
    private L_PUSH_MSG_KEY key;

    /* 제목 */
    @Column(name = "TITLE")
    @SearchableField
    private String title;

    /* 메시지 */
    @Column(name = "MSG")
    @SearchableField
    private String message;

    /* 링크 */
    @Column(name = "LINK")
    @SearchableField
    private String link;

    /* 전송 요청 일시 */
    @Column(name = "TRSM_RQST_DT")
    @SearchableField
    private LocalDateTime transmissionRequestDate;

    /* 전송 여부 */
    @Column(name = "TRSM_DIV")
    @SearchableField
    @Enumerated(EnumType.STRING)
    private TransmissionDivision transmissionDivision;

    /* 알람 확인 여부 */
    @Column(name = "ALARM_CHCK_YN")
    @Enumerated(EnumType.STRING)
    private UseYn alarmCheckYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USR_ID", insertable = false, updatable = false)
    private M_USR user;

    /* 메뉴 표출 유형 코드 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "MSG_TYPE", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'MSG_TYPE'", referencedColumnName = "GRP_CD_ID")),
    })
    private MSG_TYPE messageTypeCode;


    @Builder
    public L_PUSH_MSG(L_PUSH_MSG_KEY key, String title, String message, String link, LocalDateTime transmissionRequestDate, TransmissionDivision transmissionDivision, UseYn alarmCheckYn, MSG_TYPE messageType) {
        this.key = key;
        this.title = title;
        this.message = message;
        this.link = link;
        this.transmissionRequestDate = transmissionRequestDate;
        this.transmissionDivision = transmissionDivision;
        this.alarmCheckYn = alarmCheckYn;
        this.messageTypeCode = messageType;
    }
    /* 전송 여부 수정 */
    public void updateTransmissionYn(TransmissionDivision transmissionYn) {
        this.transmissionDivision = transmissionYn;
    }

    /* 알람 확인 여부 수정 */
    public void updateAlarmCheckYn(UseYn alarmCheckYn) {
        this.alarmCheckYn = alarmCheckYn;
    }

}
