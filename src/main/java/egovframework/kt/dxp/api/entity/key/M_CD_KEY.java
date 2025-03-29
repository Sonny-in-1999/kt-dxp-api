package egovframework.kt.dxp.api.entity.key;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.Hibernate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Embeddable
public class M_CD_KEY implements Serializable {

    private static final long serialVersionUID = -94622356292802825L;
    @Size(max = 36)
    @NotNull
    @Column(name = "GRP_CD_ID", nullable = false, length = 36)
    private String groupCodeId;

    @Size(max = 36)
    @NotNull
    @Column(name = "CD_ID", nullable = false, length = 36)
    private String codeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        M_CD_KEY entity = (M_CD_KEY) o;
        return Objects.equals(this.groupCodeId, entity.groupCodeId) &&
                Objects.equals(this.codeId, entity.codeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupCodeId, codeId);
    }

}