package egovframework.kt.dxp.api.domain.notice;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.notice.record.NoticeDetailCreateRequest;
import egovframework.kt.dxp.api.domain.notice.record.NoticeDetailSearchRequest;
import egovframework.kt.dxp.api.entity.L_NOTICE_USR;
import egovframework.kt.dxp.api.entity.key.L_NOTICE_USR_KEY;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 이력 공지 사용자 Mapper
 *
 * @author MINJI
 * @since 2024-10-16
 */
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface NoticeUserMapper {

    NoticeUserMapper INSTANCE = Mappers.getMapper(NoticeUserMapper.class);

    /**
     * 추가 시 키 추출용 메서드<br />
     *
     * @param noticeDetailSearchRequest 추가 요청 record
     * @return entity key
     * @author MINJI
     * @since 2024-10-17<br />
     */
    L_NOTICE_USR_KEY toEntityKey(NoticeDetailSearchRequest noticeDetailSearchRequest);

    /**
     * NoticeDetailCreateRequest to 이력 공지 사용자 Entity 변환 메서드<br />
     *
     * @author MINJI
     * @since  2024-10-17<<br />
     */
    @Mappings({
            @Mapping(target = "key.userId", source = "userId"),
            @Mapping(target = "key.noticeSequenceNumber", source = "noticeSequenceNumber")
    })
    L_NOTICE_USR toEntity(NoticeDetailCreateRequest noticeDetailCreateRequest);
}
