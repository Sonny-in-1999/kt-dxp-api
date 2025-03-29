package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class M_BANNER extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BANNER_SQNO")
    private Integer bannerSequenceNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "TITLE")
    private String title;

    @Size(max = 100)
    @Column(name = "LINK_URL")
    private String linkUniformResourceLocator;

    @Column(name = "SORT_SQNO")
    private Integer sortSequenceNumber;

    @Size(max = 1)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "DSPL_YN")
    private UseYn displayYn;

    @Size(max = 36)
    @NotNull
    @CreatedBy
    @Column(name = "CRTUSR_ID")
    private String createUsrId;

    @Size(max = 36)
    @NotNull
    @LastModifiedBy
    @Column(name = "UPDTUSR_ID")
    private String updateUserId;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "BANNER_SQNO", referencedColumnName = "BBS_SQNO", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'07'", referencedColumnName = "BBS_DIV")),
    })
    private L_FILE file;
}