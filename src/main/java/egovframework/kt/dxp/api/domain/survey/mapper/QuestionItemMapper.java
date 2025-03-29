package egovframework.kt.dxp.api.domain.survey.mapper;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.survey.record.QuestionSearchResponse;
import egovframework.kt.dxp.api.entity.L_SURV_QSTN_ITEM;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface QuestionItemMapper {
    QuestionItemMapper INSTANCE = Mappers.getMapper(QuestionItemMapper.class);
    @Mappings({
            @Mapping(target = "itemSequenceNumber", source = "key.itemSequenceNumber"),
            @Mapping(target = "itemTypeValue", expression= "java(entity.getItemType() != null ? entity.getItemTypeCode().getItemTypeCodeValue() : null)")
    })
    QuestionSearchResponse toSearchResponse(L_SURV_QSTN_ITEM entity);


    List<QuestionSearchResponse> toSearchResponseList(List<L_SURV_QSTN_ITEM> entity);


}
