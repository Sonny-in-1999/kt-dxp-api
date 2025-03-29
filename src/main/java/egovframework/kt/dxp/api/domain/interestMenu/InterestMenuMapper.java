package egovframework.kt.dxp.api.domain.interestMenu;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuCreateRequest;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuCreateResponse;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuDeleteRequest;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuResponse;
import egovframework.kt.dxp.api.domain.interestMenu.record.MenuSearchResponse;
import egovframework.kt.dxp.api.entity.M_MENU;
import egovframework.kt.dxp.api.entity.M_USR_MENU;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : InterestMenuMapper
 * Description   :  관심메뉴 Mapper
 * Creation Date : 2024-10-22
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-22, MinJi Chae, 최초작성
 ******************************************************************************************/
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface InterestMenuMapper {

    InterestMenuMapper INSTANCE = Mappers.getMapper(InterestMenuMapper.class);

    /**
     * 메뉴 List<Entity> to List<MenuSearchResponse> 변환 메서드<br />
     *
     * @param entityList 조회한 entity list
     * @return MenuSearchResponse list
     * @author MINJI
     * @since 2024-10-22<br />
     */
    List<MenuSearchResponse> toMenuSearchResponseList(List<M_MENU> entityList);

    /**
     * 메뉴 Entity to SearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return MenuSearchResponse
     * @author MINJI
     * @since 2024-10-22<br />
     */
    @Mappings({
            @Mapping(target = "menuId", source = "menu.menuId"),
            @Mapping(target = "menuName", source = "menu.menuName"),
            @Mapping(target = "menuUniformResourceLocator", source = "menu.menuUniformResourceLocator")
    })
    InterestMenuResponse toSearchResponse(M_USR_MENU entity);

    /**
     * 메뉴 List<Entity> to List<MenuSearchResponse> 변환 메서드<br />
     *
     * @param entityList 조회한 entity list
     * @return MenuSearchResponse list
     * @author MINJI
     * @since 2024-10-22<br />
     */
    List<InterestMenuResponse> toSearchResponseList(List<M_USR_MENU> entityList);

    /**
     * 관심메뉴 Entity to SearchResponse 변환 메서드
     *
     * @return entity
     * @author MINJI
     * @since 2024-10-22<br />
     */
    @Mappings({
            @Mapping(target = "key.userId", source = "userId")
    })
    M_USR_MENU toEntity(InterestMenuCreateRequest interestmenuCreateRequest);

    /**
     * 관심메뉴 Entity to SearchResponse 변환 메서드
     *
     * @return entity
     * @author MINJI
     * @since 2024-10-22<br />
     */
    @Mappings({
            @Mapping(target = "key.userId", source = "userId")
    })
    M_USR_MENU toEntity(InterestMenuDeleteRequest interestmenuDeleteRequest);


    /**
     * 관심메뉴 Entity to SearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return InterestMenuCreateResponse
     * @author MINJI
     * @since 2024-10-22<br />
     */
    @Mappings({
            @Mapping(target = "menuId", source = "menu.menuId"),
            @Mapping(target = "menuName", source = "menu.menuName")
    })
    InterestMenuCreateResponse toCreateResponse(M_USR_MENU entity);


}
