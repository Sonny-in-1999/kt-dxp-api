package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.code.ITEM_TYPE;
import egovframework.kt.dxp.api.entity.code.QSTN_TYPE;
import egovframework.kt.dxp.api.entity.key.L_SURV_QSTN_ITEM_KEY;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

@Getter
@Setter
@Entity
public class L_SURV_QSTN_ITEM {

    @EmbeddedId
    private L_SURV_QSTN_ITEM_KEY key;

    @Size(max = 50)
    @NotNull
    @Column(name = "ITEM", nullable = false, length = 50)
    private String item;

    @Size(max = 3)
    @NotNull
    @Column(name = "ITEM_TYPE", nullable = false, length = 3)
    private String itemType;

    @Column(name = "SORT_SQNO")
    private Integer sortSequenceNumber;

    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "ITEM_TYPE", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'ITEM_TYPE'", referencedColumnName = "GRP_CD_ID")),
    })
    private ITEM_TYPE itemTypeCode;

    //@OneToOne( fetch = FetchType.LAZY, optional = false)
    //@JoinColumns({
    //        @JoinColumn(name = "SURV_SQNO", referencedColumnName = "SURV_SQNO", nullable = false),
    //        @JoinColumn(name = "QSTN_SQNO", referencedColumnName = "QSTN_SQNO", nullable = false),
    //        @JoinColumn(name = "ITEM_SQNO", referencedColumnName = "ITEM_SQNO", nullable = false)
    //})
    //private S_SURV_RSLT statisticsSurvey;

}