package egovframework.kt.dxp.api.entity;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
public class M_CD_GRP {

    @Id
    @Size(max = 36)
    @Column(name = "GRP_CD_ID", nullable = false, length = 36)
    private String groupCodeId;

    @Size(max = 50)
    @NotNull
    @Column(name = "GRP_CD_NM", nullable = false, length = 50)
    private String groupCodeName;

    @Size(max = 50)
    @Column(name = "DESCR", length = 50)
    private String description;

    @Size(max = 1)
    @NotNull
    @Column(name = "UPDT_ABLE_YN", nullable = false, length = 1)
    private String updateYn;

    @Size(max = 1)
    @NotNull
    @Column(name = "USE_YN", nullable = false, length = 1)
    private String useYn;

    @OneToMany(mappedBy = "grpCd")
    private Set<M_CD> mCds = new LinkedHashSet<>();

}