package egovframework.kt.dxp.api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import egovframework.kt.dxp.api.common.jpa.annotations.DefaultSort;
import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.common.request.enumeration.SortDirection;
import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.code.NOTICE_DIV;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 마스터 공지사항 Entity <br />
 *
 * @author MINJI
 * @apiNote 2024-10-29 BITNA alarmCheckYn 주석처리
 * @since 2024-10-15 <br />
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
//@Entity(name = "M_NOTICE")
@Entity()
@NamedEntityGraph(
        name = "M_NOTICE_GRAPH",
        attributeNodes = {
                @NamedAttributeNode("noticeDivisionCode")
        }
)
@DefaultSort(columnName = "createDate", direction = SortDirection.DESC)
@EntityListeners(AuditingEntityListener.class)
public class M_NOTICE extends BaseEntity {

    /* 공지 순번 */
    @Id
    @Column(name = "NOTICE_SQNO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeSequenceNumber;

    /* 공지 구분 */
    @Column(name = "NOTICE_DIV")
    private String noticeDivision;

    /* 제목 */
    @Column(name = "TITLE")
    private String title;

    /* 내용 */
    @Column(name = "CONTS")
    private String contents;

    /* 생성자 아이디 */
    @Column(name = "CRTUSR_ID")
    @CreatedBy
    private String createUserId;

    /* 수정자 아이디 */
    @Column(name = "UPDTUSR_ID")
    @LastModifiedBy
    private String updateUserId;

    @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<L_NOTICE_USR> lNoticeUsrList;

    @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<L_FILE> lFileList;

    /* 공지구분 코드 */
    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "NOTICE_DIV", referencedColumnName = "CD_ID", insertable = false, updatable = false)),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'NOTICE_DIV'", referencedColumnName = "GRP_CD_ID")),
    })
    @SearchableField(columnPath = "noticeDivisionCode.noticeDivisionName")
    private NOTICE_DIV noticeDivisionCode;

}
