package egovframework.kt.dxp.api.entity.key;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 마스터 공지사항 Entity Key<br />
 *
 * @author MINJI
 * @since 2024-10-15<br />
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class L_NOTICE_USR_KEY implements Serializable {

    /* 사용자 아이디 */
    @Column(name = "USR_ID")
    private String userId;

    /* 공지 순번 */
    @Column(name = "NOTICE_SQNO")
    private Integer noticeSequenceNumber;


}
