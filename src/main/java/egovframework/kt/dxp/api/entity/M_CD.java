package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.M_CD_KEY;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
public class M_CD {

    @EmbeddedId
    private M_CD_KEY key;

    @MapsId("groupCodeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GRP_CD_ID", nullable = false)
    private M_CD_GRP grpCd;

    @Size(max = 50)
    @NotNull
    @Column(name = "CD_NM", nullable = false, length = 50)
    private String codeName;

    @Size(max = 50)
    @Column(name = "DESCR", length = 50)
    private String description;

    @Column(name = "SORT_SQNO")
    private Integer sortSequenceNumber;

    @Size(max = 1)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "USE_YN", nullable = false, length = 1)
    private UseYn useYn;
}