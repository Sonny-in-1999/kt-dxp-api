package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class M_DONG {

    /* 동 코드 */
    @Id
    @SearchableField
    @Column(name = "DONG_CD")
    private String dongCode;

    /* 동 명 */
    @SearchableField
    @Column(name = "DONG_NM")
    private String dongName;

    /* 동 생성일시 */
    @SearchableField
    @Column(name = "DONG_CRT_DT")
    private LocalDate dongCreateDate;

    @Builder
    public M_DONG(String dongCode, String dongName, LocalDate createDate) {
        this.dongCode = dongCode;
        this.dongName = dongName;
        this.dongCreateDate = createDate;
    }

}