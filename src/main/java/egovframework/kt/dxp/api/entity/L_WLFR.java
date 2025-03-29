package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.code.WLFR_DIV;
import egovframework.kt.dxp.api.entity.code.WLFR_DTL_DIV;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "L_WLFR")
public class L_WLFR extends BaseEntity {

    /* 복지 순번 */
    @Id
    @Column(name = "WLFR_SQNO")
    private Integer welfareSequenceNumber;

    /* 복지 구분 */
    @Column(name = "WLFR_DIV")
    private String welfareDivision;

    /* 복지 상세 구분 */
    @Column(name = "WLFR_DTL_DIV")
    private String welfareDetailDivision;

    /* 제목 */
    @Column(name = "TITLE")
    private String title;

    /* 내용 */
    @Column(name = "CONTS")
    private String contents;

//    /* 대표 이미지 */
//    @Column(name = "MAIN_IMG")
//    @Lob
//    private byte[] mainImage;

    /* 생성자 아이디 */
    @Column(name = "CRTUSR_ID")
    private String createUserId;

    /* 수정자 아이디 */
    @Column(name = "UPDTUSR_ID")
    private String updateUserId;

    /* 파일 목록 */
//    @OneToMany(mappedBy = "welfare", cascade = CascadeType.REMOVE)
//    private List<L_FILE> fileList = new ArrayList<>();

    /* 복지 정책 버튼 목록 */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "WLFR_SQNO", referencedColumnName = "WLFR_SQNO", insertable = false, updatable = false)
    private List<L_WLFR_BTN> welfareButtonList = new ArrayList<>();

    /* 복지 정책 조회 유저 목록 -> 조회수(중복 방지) */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "WLFR_SQNO", referencedColumnName = "WLFR_SQNO", insertable = false, updatable = false)
    private List<L_WLFR_USR> welfareUserList = new ArrayList<>();

    /* 복지구분 코드 */
    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "WLFR_DIV", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'WLFR_DIV'", referencedColumnName = "GRP_CD_ID")),
    })
    private WLFR_DIV welfareDivisionCode;

    /* 복지구분 상세 코드 -> 생애주기별 */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "WLFR_DTL_DIV", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'LIFE_CYCLE_DIV'", referencedColumnName = "GRP_CD_ID")),
    })
    private WLFR_DTL_DIV welfareDetailDivisionCode;
}
