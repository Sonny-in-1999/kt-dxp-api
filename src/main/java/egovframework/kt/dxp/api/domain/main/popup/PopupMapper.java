package egovframework.kt.dxp.api.domain.main.popup;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.main.popup.record.PopupSearchResponse;
import egovframework.kt.dxp.api.entity.M_POPUP;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface PopupMapper {
    PopupMapper INSTANCE = Mappers.getMapper(PopupMapper.class);

    @Mappings({
            @Mapping(target = "popupTypeValue", expression = "java(entity.getPopupType() != null ? entity.getPopupTypeCode().getPopupTypeCodeValue() : null)"),

    })
    PopupSearchResponse toSearchResponse(M_POPUP entity);

    List<PopupSearchResponse> toSearchResponseList(List<M_POPUP> entityList);
}
