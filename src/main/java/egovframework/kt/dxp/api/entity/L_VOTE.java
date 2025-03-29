package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.code.PRPS_PRGRS_DIV;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GEONLEE
 * @apiNote 2024-10-31 BITNA L_VOTE_ITEM join
 * 2024-11-06 BITNA selectLimitCount -> min/maxSelectCount 컬럼변경
 * 2024-11-20 BITNA file join 추가
 * @since 2024-10-30
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "L_VOTE")
public class L_VOTE {

    @Id
    @Column(name = "VOTE_SQNO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteSequenceNumber;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTS")
    private String contents;

    @Column(name = "MIN_SELECT_CNT")
    private Integer minSelectCount;

    @Column(name = "MAX_SELECT_CNT")
    private Integer maxSelectCount;

//    @Column(name = "PCPT_LIMIT_CNT")
//    private Integer participationLimitCount;

    @Column(name = "PCPT_START_AGE")
    private Integer participationStartAge;

    @Column(name = "PCPT_END_AGE")
    private Integer participationEndAge;

    @Column(name = "MALE_ABLE_YN")
    @Enumerated(EnumType.STRING)
    private UseYn maleAbleYn;

    @Column(name = "FEMALE_ABLE_YN")
    @Enumerated(EnumType.STRING)
    private UseYn femaleAbleYn;

    @Column(name = "START_DT")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDate;

    @Column(name = "END_DT")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endDate;

    @Column(name = "END_YN")
    @Enumerated(EnumType.STRING)
    @Setter
    private UseYn endYn;

    @Column(name = "CRTUSR_ID")
    private String createUserId;

    @Column(name = "UPDTUSR_ID")
    private String updateUserId;

    @Column(name = "CRT_DT")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Column(name = "UPDT_DT")
    @Setter
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "VOTE_SQNO", referencedColumnName = "VOTE_SQNO", insertable = false, updatable = false)
    private List<L_VOTE_ITEM> voteItemList = new ArrayList<>();

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "VOTE_SQNO", referencedColumnName = "BBS_SQNO", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'05'", referencedColumnName = "BBS_DIV")),
    })
    private L_FILE file;

//    @OneToMany(mappedBy = "vote")
//    @NotFound(action = NotFoundAction.IGNORE)
//    private List<L_VOTE_ITEM> voteItemList = new ArrayList<>();

}
