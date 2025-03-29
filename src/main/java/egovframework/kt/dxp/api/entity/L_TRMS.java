package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.DefaultSort;
import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.common.request.enumeration.SortDirection;
import egovframework.kt.dxp.api.entity.code.TRMS_TYPE;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_TRMS_KEY;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * [이력 약관] Entity<br />
 *
 * @author BITNA
 * @since 2024-10-18<br />
 */

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "L_TRMS")
@DefaultSort(columnName = {"termsStartDate"}, direction = {SortDirection.DESC})
public class L_TRMS {

    /* 키 */
    @EmbeddedId
    @SearchableField(columnPath = {"key.termsType", "key.termsStartDate"})
    private L_TRMS_KEY key;

    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "TRMS_TYPE", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'TRMS_TYPE'", referencedColumnName = "GRP_CD_ID")),
    })
    @SearchableField(columnPath = "termsTypeCode.termsType")
    private TRMS_TYPE termsTypeCode;

    /* 내용 */
    @Column(name = "CONTS")
    private String contents;

    /* 정렬 순번 */
    @Column(name = "SORT_SQNO")
    private String sortSequence;

    /* 필수 여부 */
    @Column(name = "ESNTL_YN")
    @Enumerated(EnumType.STRING)
    private UseYn essentialYn;

    /* 사용 여부 */
    @Column(name = "USE_YN")
    @Enumerated(EnumType.STRING)
    private UseYn useYn;

    /* 생성 일시 */
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRT_DT", updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    /* 수정 일시 */
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDT_DT")
    private LocalDateTime updateDate;


}
