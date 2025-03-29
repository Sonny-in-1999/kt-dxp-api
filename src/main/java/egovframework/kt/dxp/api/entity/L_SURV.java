package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class L_SURV extends BaseEntity {

    @Id
    @Column(name = "SURV_SQNO", nullable = false)
    private Integer surveySequenceNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Lob
    @Column(name = "CONTS")
    private String contents;

    @Column(name = "REQ_TIME")
    private Integer requiredTime;

    @NotNull
    @Column(name = "PCPT_START_AGE")
    private Integer participationStartAge;

    @NotNull
    @Column(name = "PCPT_END_AGE", nullable = false)
    private Integer participationEndAge;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "MALE_ABLE_YN", nullable = false)
    private UseYn maleAbleYn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "FEMALE_ABLE_YN", nullable = false)
    private UseYn femaleAbleYn;

    @NotNull
    @SearchableField
    @Column(name = "START_DT", nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @SearchableField
    @Column(name = "END_DT", nullable = false)
    private LocalDateTime endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "END_YN", nullable = false)
    @Setter
    private UseYn endYn;

    @Size(max = 36)
    @NotNull
    @CreatedBy
    @Column(name = "CRTUSR_ID", nullable = false, length = 36)
    private String createUserId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURV_SQNO")
    private List<L_SURV_QSTN> surveyQuestionList = new ArrayList<>();

//    @OneToOne
//    @NotFound(action = NotFoundAction.IGNORE)
//    @JoinColumnsOrFormulas(value = {
//            @JoinColumnOrFormula(column = @JoinColumn(name = "SURV_SQNO", referencedColumnName = "BBS_SQNO", insertable = false, updatable = false)),
//            @JoinColumnOrFormula(formula = @JoinFormula(value = "'03'", referencedColumnName = "BBS_DIV")),
//    })
//    private L_FILE file;

}