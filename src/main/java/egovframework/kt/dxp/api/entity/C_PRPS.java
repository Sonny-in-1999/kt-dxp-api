package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.code.PRPS_DIV;
import egovframework.kt.dxp.api.entity.code.PRPS_PRGRS_DIV;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * [현황 정책] Entity<br />
 *
 * @author BITNA
 * @apiNote 2024-10-31 BITNA DB 컬럼 변경 result -> answer
 * @since 2024-10-29<br />
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class C_PRPS extends BaseEntity {

    /* 제안 순번 */
    @Id
    @SearchableField
    @Column(name = "PRPS_SQNO")
    private Long proposalSequenceNumber;

    /* 제안 구분 */
    @Column(name = "PRPS_DIV")
    @SearchableField
    private String proposalDivision;

    /* 제안 진행 구분 */
    @Column(name = "PRPS_PRGRS_DIV")
    @SearchableField
    private String proposalProgressDivision;

    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "PRPS_DIV", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'PRPS_DIV'", referencedColumnName = "GRP_CD_ID")),
    })
    @SearchableField(columnPath = "proposalDivisionCode.proposalDivisionName")
    private PRPS_DIV proposalDivisionCode;

    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "PRPS_PRGRS_DIV", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'PRPS_PRGRS_DIV'", referencedColumnName = "GRP_CD_ID")),
    })
    @SearchableField(columnPath = "proposalProgressDivisionCode.proposalProgressDivisionName")
    private PRPS_PRGRS_DIV proposalProgressDivisionCode;

    /* 제목 */
    @Column(name = "TITLE")
    private String title;

    /* 배경 내용 */
    @Column(name = "BKGD_CONTS")
    private String backgroundContents;

    /* 제안 내용 */
    @Column(name = "PRPS_CONTS")
    private String proposalContents;

    /* 기대 효과 */
    @Column(name = "EXPT_EFFCT")
    private String expectEffect;

    /* 생성자 아이디 */
    @Column(name = "CRTUSR_ID")
    private String createUserId;

    /* 답변 */
    @Column(name = "ANS")
    private String answer;

    /* 답변 생성 일시 */
    @Column(name = "ANS_CRT_DT")
    private LocalDateTime answerCreateDate;

    /* 답변자 아이디 */
    @Column(name = "ANS_CRTUSR_ID")
    private String answerCreateUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "CRTUSR_ID", referencedColumnName = "USR_ID", insertable = false, updatable = false)
    private M_USR createUserInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "ANS_CRTUSR_ID", referencedColumnName = "USR_ID", insertable = false, updatable = false)
    private M_USR answerCreateUserInfo;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumnsOrFormulas(value = {
//            @JoinColumnOrFormula(column = @JoinColumn(name = "PRPS_SQNO", referencedColumnName = "BBS_SQNO", insertable = false, updatable = false)),
//            @JoinColumnOrFormula(formula = @JoinFormula(value = "'02'", referencedColumnName = "BBS_DIV")),
//    })
//    private L_FILE fileList;

    @Builder
    public C_PRPS(Long proposalSequenceNumber, String proposalDivision, String title,
                  String backgroundContents, String proposalContents, String expectEffect,
                  String createUserId) {
        this.proposalSequenceNumber = proposalSequenceNumber;
        this.proposalDivision = proposalDivision;
        this.proposalProgressDivision = "01"; // 진행구분코드 제안[01]
        this.title = title;
        this.backgroundContents = backgroundContents;
        this.proposalContents = proposalContents;
        this.expectEffect = expectEffect;
        this.createUserId = createUserId;
    }
}
