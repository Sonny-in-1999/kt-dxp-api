package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.code.QSTN_TYPE;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_SURV_QSTN_KEY;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class L_SURV_QSTN {

    @EmbeddedId
    private L_SURV_QSTN_KEY key;

    @Size(max = 50)
    @NotNull
    @Column(name = "QSTN", nullable = false, length = 50)
    private String question;

    @Size(max = 3)
    @NotNull
    @Column(name = "QSTN_TYPE", nullable = false, length = 3)
    private String questionType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ESNTL_YN", nullable = false, length = 3)
    private UseYn essentialYn;

    @Column(name = "SORT_SQNO")
    private Integer sortQuestionSequenceNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SURV_SQNO", nullable = false,insertable = false, updatable = false)
    private L_SURV surveySequenceNumber;

    @Column(name = "MIN_SELECT_CNT", nullable = false, length = 36)
    private Integer minSelectCount;

    @Column(name = "MAX_SELECT_CNT", nullable = false, length = 36)
    private Integer maxSelectCount;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SURV_SQNO", referencedColumnName = "SURV_SQNO", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "QSTN_SQNO", referencedColumnName = "QSTN_SQNO", nullable = false, insertable = false, updatable = false)
    })
    private List<L_SURV_QSTN_ITEM> surveyQuestionItemList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SURV_SQNO", referencedColumnName = "SURV_SQNO", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "QSTN_SQNO", referencedColumnName = "QSTN_SQNO", nullable = false, insertable = false, updatable = false)
    })
    private List<L_SURV_USR> surveyUserList = new ArrayList<>();

    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "QSTN_TYPE", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'QSTN_TYPE'", referencedColumnName = "GRP_CD_ID")),
    })
    private QSTN_TYPE questionTypeCode;


}