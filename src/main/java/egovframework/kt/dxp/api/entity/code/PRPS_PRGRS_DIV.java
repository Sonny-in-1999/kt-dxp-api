package egovframework.kt.dxp.api.entity.code;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.key.M_CD_KEY;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : TRMS_TYPE
 * Description   :  
 * Creation Date : 2024-10-17
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-30, BITNA, 최초작성
 ******************************************************************************************/
@Getter
@Setter
@Entity
@Table(name = "M_CD")
public class PRPS_PRGRS_DIV {
    /* 키 */
    @EmbeddedId
    @SearchableField(columnPath = {"key.groupCodeId", "key.codeId"})
    private M_CD_KEY key;

    /* 코드 명 */
    @Column(name = "CD_NM")
    @SearchableField
    private String proposalProgressDivisionName;

}
