package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.key.L_WLFR_BTN_KEY;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class L_WLFR_BTN {

    /* 키 */
    @EmbeddedId
    private L_WLFR_BTN_KEY key;

    @Column(name = "BTN_NM")
    private String buttonName;

    @Column(name = "LINK_URL")
    private String linkUniformResourceLocator;
}
