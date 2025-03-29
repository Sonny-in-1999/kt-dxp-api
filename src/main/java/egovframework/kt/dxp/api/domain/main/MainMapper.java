package egovframework.kt.dxp.api.domain.main;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuResponse;
import egovframework.kt.dxp.api.domain.main.record.BannerSearchResponse;
import egovframework.kt.dxp.api.domain.main.record.MainSearchResponse;
import egovframework.kt.dxp.api.entity.M_BANNER;
import egovframework.kt.dxp.api.entity.M_USR_MENU;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface MainMapper {
    MainMapper INSTANCE = Mappers.getMapper(MainMapper.class);

    @Mappings({
            @Mapping(target = "menuId", source = "menu.menuId"),
            @Mapping(target = "menuName", source = "menu.menuName"),
            @Mapping(target = "menuUniformResourceLocator", source = "menu.menuUniformResourceLocator")
    })
    InterestMenuResponse toSearchResponse(M_USR_MENU entity);

    List<InterestMenuResponse> toMenuSearchResponseList(List<M_USR_MENU> entityList);

    BannerSearchResponse toSearchResponse(M_BANNER entity);

    List<BannerSearchResponse> toBannerSearhResponseList(List<M_BANNER> entityList);

}
