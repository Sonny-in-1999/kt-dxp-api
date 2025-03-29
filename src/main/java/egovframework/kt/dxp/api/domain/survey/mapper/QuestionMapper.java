package egovframework.kt.dxp.api.domain.survey.mapper;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.survey.record.QuestionItemSearchResponse;
import egovframework.kt.dxp.api.entity.L_SURV_QSTN;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class,
        QuestionItemMapper.class, SurveyMapper.class})
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mappings({
            @Mapping(target = "questionSequenceNumber", source = "key.questionSequenceNumber"),
            @Mapping(target = "questionTypeValue", expression = "java(entity.getQuestionType() != null ? entity.getQuestionTypeCode().getQuestionTypeCodeValue() : null)"),
            @Mapping(target = "answer", expression = "java(SurveyMapper.INSTANCE.toSearchResponseList(entity.getSurveyUserList()))"),
            @Mapping(target = "itemList", expression = "java(QuestionItemMapper.INSTANCE.toSearchResponseList(entity.getSurveyQuestionItemList()))")
    })
    QuestionItemSearchResponse toSearchResponse(L_SURV_QSTN entity);


    List<QuestionItemSearchResponse> toSearchResponseList(List<L_SURV_QSTN> entity);


}
