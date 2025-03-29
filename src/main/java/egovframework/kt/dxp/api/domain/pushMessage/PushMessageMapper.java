package egovframework.kt.dxp.api.domain.pushMessage;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.config.firebase.record.MessageRequest;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSaveResponse;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSearchResponse;
import egovframework.kt.dxp.api.entity.L_PUSH_MSG;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceController
 * Description   : 알림 조회 Mapper
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 ******************************************************************************************/
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface PushMessageMapper {

    PushMessageMapper INSTANCE = Mappers.getMapper(PushMessageMapper.class);

    @Mappings({
            @Mapping(target = "createDate"     , expression = "java(Converter.localDateTimeToString(entity.getKey().getCreateDate(), DateType.YYYYMMDDHHMMSS_FORMAT))"),
            @Mapping(target = "messageType"    , source     = "key.messageType"),
            @Mapping(target = "messageTypeName", expression = "java(entity.getMessageTypeCode() != null ? entity.getMessageTypeCode().getMessageTypeName() : null)"),
            @Mapping(target = "elapsedTime"    , expression = "java(Converter.getElapsedTime(entity.getKey().getCreateDate()))")
    })
    PushMessageSearchResponse toSearchResponse(L_PUSH_MSG entity);
    List<PushMessageSearchResponse> toSearchResponseList(List<L_PUSH_MSG> entity);

    @Mappings({
            @Mapping(target = "messageType", expression = "java(entity.getMessageTypeCode() != null ? entity.getMessageTypeCode().getMessageTypeName() : null)"),
            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getKey().getCreateDate(), DateType.YYYYMMDDHHMMSS_FORMAT))"),
            @Mapping(target = "transmissionRequestDate", expression = "java(Converter.localDateTimeToString(entity.getTransmissionRequestDate(), DateType.YYYYMMDDHHMMSS_FORMAT))")
    })
    PushMessageSaveResponse toSaveResponse(L_PUSH_MSG entity);

    @Mappings({
            @Mapping(target = "userId", source = "key.userId"),
            @Mapping(target = "messageType", source = "key.messageType"),
            @Mapping(target = "pushToken", source = "user.pushKey"),
    })
    MessageRequest toSendMessageRequest(L_PUSH_MSG entity);
    List<MessageRequest> toSendMessageRequestList(List<? extends L_PUSH_MSG> entity);

}
