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
public class POPUP_TYPE {

    @EmbeddedId
    private M_CD_KEY key;

    @Column(name = "CD_NM")
    @SearchableField
    private String popupTypeCodeValue;
}
