package egovframework.kt.dxp.api.entity.key;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 마스터 사용자 관심 메뉴 Entity Key<br />
 *
 * @author MINJI
 * @since 2024-10-22<br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class M_USER_MENU_KEY implements Serializable {

    /* 사용자 아이디 */
    @Column(name = "USR_ID")
    private String userId;

    /* 메뉴 아이디 */
    @Column(name = "MENU_ID")
    private String menuId;

}