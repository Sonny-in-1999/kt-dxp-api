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
public class L_MOBILE_ID_KEY implements Serializable {

    private static final long serialVersionUID = -3747261124677852066L;
    @Size(max = 36)
    @NotNull
    @Column(name = "USR_ID", nullable = false, length = 36)
    private String userId;

    @Size(max = 50)
    @NotNull
    @Column(name = "TRXCODE", nullable = false, length = 50)
    private String trxCode;

    @NotNull
    @Column(name = "PROC_SQNO", nullable = false)
    private Integer processSequenceNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        L_MOBILE_ID_KEY entity = (L_MOBILE_ID_KEY) o;
        return Objects.equals(this.trxCode, entity.trxCode) &&
                Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.processSequenceNumber, entity.processSequenceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trxCode, userId, processSequenceNumber);
    }

}