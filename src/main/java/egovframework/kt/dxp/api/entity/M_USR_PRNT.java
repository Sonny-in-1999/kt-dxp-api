package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.base.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity()
public class M_USR_PRNT extends BaseEntity {

    @Id
    @Size(max = 36)
    @Column(name = "USR_ID", nullable = false, length = 36)
    private String userId;

    @Size(max = 50)
    @NotNull
    @Column(name = "PRNT_NM", nullable = false, length = 50)
    private String parentName;

    @Size(max = 30)
    @NotNull
    @Column(name = "PRNT_HP_NO", nullable = false, length = 30)
    private String parentMobilePhoneNumber;

    @OneToOne
    @JoinColumn(name = "USR_ID")
    private M_USR mUsr;

    @Builder
    public M_USR_PRNT(String userId, String parentName, String parentMobilePhoneNumber) {
        this.userId = userId;
        this.parentName = parentName;
        this.parentMobilePhoneNumber = parentMobilePhoneNumber;
    }

    public void deleteUserParentInformation() {
        this.parentName = "";
        this.parentMobilePhoneNumber = "";
    }
}