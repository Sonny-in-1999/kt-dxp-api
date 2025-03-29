package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.key.L_NOTICE_USR_KEY;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 이력 공지사항 사용자 Entity<br />
 *
 * @author MINJI
 * @since 2024-10-15<br />
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@EntityListeners(AuditingEntityListener.class)
public class L_NOTICE_USR {

    /* L_NOTICE_USR의 key */
    @EmbeddedId
    @SearchableField(columnPath = {"key.userId", "key.noticeSequenceNumber"})
    private L_NOTICE_USR_KEY key;

    /* 생성 일시 */
    @Column(name = "CRT_DT")
    @CreatedDate
    @SearchableField
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_SQNO", referencedColumnName = "NOTICE_SQNO", insertable = false, updatable = false)
    private M_NOTICE notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USR_ID", insertable = false, updatable = false)
    private M_USR user;

}
