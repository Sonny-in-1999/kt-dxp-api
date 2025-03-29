package egovframework.kt.dxp.api.entity.key;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class M_MOBILE_ID_KEY implements Serializable {

    private static final long serialVersionUID = -8383238456484870019L;
    @Size(max = 36)
    @NotNull
    @Column(name = "USR_ID", nullable = false, length = 36)
    private String userId;

    @Size(max = 50)
    @NotNull
    @Column(name = "TRXCODE", nullable = false, length = 50)
    private String trxCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        M_MOBILE_ID_KEY entity = (M_MOBILE_ID_KEY) o;
        return Objects.equals(this.trxCode, entity.trxCode) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trxCode, userId);
    }

}