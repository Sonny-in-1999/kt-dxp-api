package egovframework.kt.dxp.api.domain.notification;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.notification.record.NotificationModifyRequest;
import egovframework.kt.dxp.api.domain.notification.record.NotificationModifyResponse;
import egovframework.kt.dxp.api.domain.notification.record.NotificationSearchResponse;
import egovframework.kt.dxp.api.entity.C_ALARM;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 알림 설정 Mapper
 *
 * @author BITNA
 * @apiNote 2024-10-28 BITNA getUserId 추가
 * @since 2024-10-21<br />
 */
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class, CommonUtils.class})
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    /**
     * 알림 설정 Entity to SearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return NotificationSearchResponse
     * @author BITNA
     * @since 2024-10-21생성<br />
     */
    @Mappings({
            @Mapping(target = "updateDate", expression = "java(Converter.localDateTimeToString(entity.getUpdateDate(), DateType.YYYYMMDD_FORMAT))")
    })
    NotificationSearchResponse toSearchResponse(C_ALARM entity);

    /**
     * 알림 설정 List<Entity> to List<NotificationSearchResponse> 변환 메서드<br />
     * MapStruct 에서 toSearchResponse 메서드 를 활용 하기 때문에 따로 매핑 불필요.
     *
     * @param entityList 조회한 entity list
     * @return NotificationSearchResponse list
     * @author BITNA
     * @since 2024-10-21<br />
     */
    List<NotificationSearchResponse> toSearchResponseList(List<C_ALARM> entityList);

    /**
     * 알림 설정 Entity to NotificationModifyResponse 변환 메서드<br />
     *
     * @param entity 수정한 엔티티
     * @return NotificationModifyResponse 수정 응답 record
     * @author BITNA
     * @since 2024-10-21<br />
     */
    @Mappings({
            @Mapping(target = "updateDate", expression = "java(Converter.localDateTimeToString(entity.getUpdateDate(), DateType.YYYYMMDD_FORMAT))")
    })
    NotificationModifyResponse toModifyResponse(C_ALARM entity);

    /**
     * NotificationUpdateRequest 를 알림 설정 entity 에 update 하는 메서드
     *
     * @author BITNA
     * @since 2024-10-21<br />
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
//            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "userId", expression = "java( CommonUtils.getUserId() )"),
            //@Mapping(target = "nightPushAlarmYn", source = "nightPushAlarmYn"),
            @Mapping(target = "actAlarmYn", source = "actAlarmYn"),
            @Mapping(target = "noticeAlarmYn", source = "noticeAlarmYn"),
            @Mapping(target = "rewardAlarmYn", source = "rewardAlarmYn"),
            @Mapping(target = "updateDate", expression = "java(Converter.getCurrentLocalDateTime())")
    })
    C_ALARM updateFromRequest(NotificationModifyRequest alramModifyRequest, @MappingTarget C_ALARM entity);
}
