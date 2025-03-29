package egovframework.kt.dxp.api.entity.code;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.key.M_CD_KEY;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "M_CD")
public class QSTN_TYPE {
    /* 키 */
    @EmbeddedId
    @SearchableField(columnPath = {"key.groupCodeId", "key.codeId"})
    private M_CD_KEY key;

    /* 코드 명 */
    @Column(name = "CD_NM")
    @SearchableField
    private String questionTypeCodeValue;
}
