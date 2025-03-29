package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_VER_KEY;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이력 버전 Entity <br />
 *
 * @author MINJI
 * @since 2024-10-21 <br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class L_VER {

    /* L_VER key */
    @EmbeddedId
    @SearchableField(columnPath = {"key.versionId", "key.createDate"})
    private L_VER_KEY key;

    /*  변경 내용 */
    @Column(name = "CHG_CONTS")
    @SearchableField
    private String changeContents;

    @Column(name = "OS_TYPE", insertable = false, updatable = false)
    @SearchableField
    private String operatingSystemType;

    @Size(max = 1)
    @Column(name = "UPDT_VER_YN", length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn updateVersionYn;
}
