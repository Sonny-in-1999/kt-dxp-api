package egovframework.kt.dxp.api.domain.survey.mapper;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.file.FileMapper;
import egovframework.kt.dxp.api.domain.survey.record.AnswerSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.QuestionItemSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.QuestionSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.SurveyDetailSearchResponse;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.L_SURV;
import egovframework.kt.dxp.api.entity.L_SURV_QSTN;
import egovframework.kt.dxp.api.entity.L_SURV_QSTN_ITEM;
import egovframework.kt.dxp.api.entity.L_SURV_USR;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class,
        QuestionMapper.class, FileMapper.class})
public interface SurveyMapper {

    SurveyMapper INSTANCE = Mappers.getMapper(SurveyMapper.class);

    @Mappings({
            @Mapping(target = "itemSequenceNumber", source = "key.itemSequenceNumber")
    })
    AnswerSearchResponse toSearchResponse(L_SURV_USR entity);


    List<AnswerSearchResponse> toSearchResponseList(List<L_SURV_USR> entity);

    @Mappings({
            @Mapping(target = "questionList", expression = "java(SurveyMapper.INSTANCE.toQuestionSearchResponseList(entity.getSurveyQuestionList()))"),
    @Mapping(target = "startDate", expression = "java(Converter.localDateTimeToString(entity.getStartDate(), DateType.YYYYMMDD_FORMAT))"),
    @Mapping(target = "endDate", expression = "java(Converter.localDateTimeToString(entity.getEndDate(), DateType.YYYYMMDD_FORMAT))"),
    @Mapping(target = "ageDescription", expression = "java(Converter.getAgeDescription(entity.getParticipationStartAge(), entity.getParticipationEndAge()))"),
    @Mapping(target = "limitTime", expression = "java(Converter.getRemainingDays(entity.getStartDate(), entity.getEndDate()))"),
    @Mapping(target = "fileList", expression = "java(FileMapper.INSTANCE.toSearchResponseList(fileList))"),
    @Mapping(target = "itemCount", expression = "java(entity.getSurveyQuestionList().size())")
    })
    SurveyDetailSearchResponse toSearchResponse(L_SURV entity, List<L_FILE> fileList);

    // Question
    @Named("toQuestionSearchResponse")
    @Mappings({
            @Mapping(target = "questionSequenceNumber", source = "key.questionSequenceNumber"),
            @Mapping(target = "questionTypeValue", expression = "java(entity.getQuestionType() != null ? entity.getQuestionTypeCode().getQuestionTypeCodeValue() : null)"),
            @Mapping(target = "answer", expression = "java(SurveyMapper.INSTANCE.toSearchResponseList(entity.getSurveyUserList()))"),
            @Mapping(target = "itemList", source = "surveyQuestionItemList", qualifiedByName = "toQuestionItemSearchResponseList")
    })
    QuestionItemSearchResponse toQuestionSearchResponse(L_SURV_QSTN entity);

    @IterableMapping(qualifiedByName = "toQuestionSearchResponse")
    @Named("toQuestionSearchResponseList")
    List<QuestionItemSearchResponse> toQuestionSearchResponseList(List<L_SURV_QSTN> entity);

    // item
    @Named("toQuestionItemSearchResponse")
    @Mappings({
            @Mapping(target = "itemSequenceNumber", source = "key.itemSequenceNumber"),
            @Mapping(target = "itemTypeValue", expression= "java(entity.getItemType() != null ? entity.getItemTypeCode().getItemTypeCodeValue() : null)")
    })
    QuestionSearchResponse toQuestionItemSearchResponse(L_SURV_QSTN_ITEM entity);

    @IterableMapping(qualifiedByName = "toQuestionItemSearchResponse")
    @Named("toQuestionItemSearchResponseList")
    List<QuestionSearchResponse> toQuestionItemSearchResponseList(List<L_SURV_QSTN_ITEM> entity);

}
