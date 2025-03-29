package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 마스터 사용자 Entity<br />
 *
 * @author MINJI
 * @since 2024-10-22<br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class M_MENU extends BaseEntity {

    /* 메뉴 아이디 */
    @Id
    @Column(name = "MENU_ID")
    private String menuId;

    /* 메뉴 명 */
    @Column(name = "MENU_NM")
    private String menuName;

    /* 정렬 순번 */
    @Column(name = "SORT_SQNO")
    private Integer sortSequenceNumber;

    /* 상위 메뉴 아이디 */
    @Column(name = "UPPER_MENU_ID")
    private String upperMenuId;

    /* 메뉴 URL */
    @Column(name = "MENU_URL")
    private String menuUniformResourceLocator;

    /* 사용 여부 */
    @Column(name = "USE_YN")
    @Enumerated(EnumType.STRING)
    private UseYn useYn;

    /* 비고 */
    @Column(name = "RMRK")
    private String remark;

    @Column(name = "MENU_DIV")
    private String menuDivision;

}