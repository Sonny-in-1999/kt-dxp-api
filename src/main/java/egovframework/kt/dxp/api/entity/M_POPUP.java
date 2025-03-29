package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.code.POPUP_TYPE;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class M_POPUP extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POPUP_SQNO", nullable = false)
    private Integer popupSequenceNumber;

    @Size(max = 3)
    @NotNull
    @Column(name = "POPUP_TYPE", nullable = false, length = 3)
    private String popupType;

    @Size(max = 100)
    @NotNull
    @Column(name = "POPUP_TITLE", nullable = false, length = 100)
    private String popupTitle;

    @Size(max = 100)
    @Column(name = "LINK_URL", length = 100)
    private String linkUrl;

    @NotNull
    @Column(name = "SORT_SQNO", nullable = false)
    private Integer sortSequenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "DSPL_YN")
    private UseYn displayYn;

    @NotNull
    @Column(name = "CLICK_CNT", nullable = false)
    private Integer clickCnt;

    @Size(max = 36)
    @NotNull
    @CreatedBy
    @Column(name = "CRTUSR_ID", nullable = false, length = 36)
    private String createUserId;

    @Size(max = 36)
    @NotNull
    @LastModifiedBy
    @Column(name = "UPDTUSR_ID", nullable = false, length = 36)
    private String updateUserId;

    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "POPUP_TYPE", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'POPUP_TYPE'", referencedColumnName = "GRP_CD_ID")),
    })
    private POPUP_TYPE popupTypeCode;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "POPUP_SQNO", referencedColumnName = "BBS_SQNO", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'04'", referencedColumnName = "BBS_DIV")),
    })
    private L_FILE file;

    public void updateClickCnt(Integer clickCnt) {
        this.clickCnt = clickCnt;
    }
}