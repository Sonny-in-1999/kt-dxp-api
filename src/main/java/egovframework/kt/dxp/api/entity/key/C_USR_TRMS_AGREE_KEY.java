package egovframework.kt.dxp.api.entity.key;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Embeddable
public class C_USR_TRMS_AGREE_KEY implements Serializable {

    private static final long serialVersionUID = -4881029195867866923L;
    /* 사용자 아이디       */
    @Size(max = 36)
    @NotNull
    @Column(name = "USR_ID", nullable = false, length = 36)
    private String userId;

    /* 약관 시작 일시      */
    @NotNull
    @Column(name = "TRMS_START_DT", nullable = false)
    private LocalDateTime termsStartDate;

    /* 약관 유형           */
    @Size(max = 3)
    @NotNull
    @Column(name = "TRMS_TYPE", nullable = false, length = 3)
    private String termsType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        C_USR_TRMS_AGREE_KEY entity = (C_USR_TRMS_AGREE_KEY) o;
        return Objects.equals(this.termsStartDate, entity.termsStartDate) &&
                Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.termsType, entity.termsType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(termsStartDate, userId, termsType);
    }

}