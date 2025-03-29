package egovframework.kt.dxp.api.entity.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 이력 푸시 메세지 Entity Key<br />
 *
 * @author MINJI
 * @since 2024-10-18<br />
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class L_PUSH_MSG_KEY implements Serializable {

    /* 사용자 아이디 */
    @Column(name = "USR_ID")
    private String userId;

    /* 생성 일시 */
    @Column(name = "CRT_DT")
    private LocalDateTime createDate;

    /* 메시지 구분 */
    @Column(name = "MSG_TYPE")
    private String messageType;
}
