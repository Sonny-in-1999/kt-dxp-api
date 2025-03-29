package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.converter.LocalDateTimeConverter;
import egovframework.kt.dxp.api.entity.key.M_USER_MENU_KEY;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 마스터 사용자 관심 메뉴 Entity<br />
 *
 * @author MINJI
 * @since 2024-10-22<br />
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@NamedEntityGraph(
        name = "M_USER_MENU_GRAPH",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("menu")
        }
)
public class M_USR_MENU {

    @EmbeddedId
    @SearchableField(columnPath = {"key.userId", "key.menuId"})
    private M_USER_MENU_KEY key;

    @Setter
    @Column(name = "SORT_SQNO")
    private Integer sortSequenceNumber;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USR_ID")
    private M_USR user;

    @MapsId("menuId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID")
    private M_MENU menu;

    @Builder
    public M_USR_MENU(M_USER_MENU_KEY key, M_USR user, M_MENU menu, Integer sortSequenceNumber) {
        this.key = key;
        this.sortSequenceNumber = sortSequenceNumber;
        this.user = user;
        this.menu = menu;
    }

}