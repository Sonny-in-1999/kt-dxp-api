package egovframework.kt.dxp.api.entity.code;

import egovframework.kt.dxp.api.entity.key.M_CD_KEY;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "M_CD")
public class WLFR_DIV {

    /* 키 */
    @EmbeddedId
    private M_CD_KEY key;

    /* 복지구분 명 */
    @Column(name = "CD_NM")
    private String welfareDivisionName;
}
