package egovframework.kt.dxp.api.entity.key;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이력 버전 Entity Key<br />
 *
 * @author MINJI
 * @since 2024-10-21<br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class L_VER_KEY implements Serializable {

    /* 버전 아이디 */
    @Column(name = "VER_ID")
    private String versionId;

    /* 생성 일시 */
    @Column(name = "CRT_DT")
    private LocalDateTime createDate;
}
