package egovframework.kt.dxp.api.domain.notice;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.file.FileMapper;
import egovframework.kt.dxp.api.domain.manageNotice.record.NoticeCreateRequest;
import egovframework.kt.dxp.api.domain.manageNotice.record.NoticeCreateResponse;
import egovframework.kt.dxp.api.domain.notice.model.NoticeListImpl;
import egovframework.kt.dxp.api.domain.notice.record.NoticeDetailSearchResponse;
import egovframework.kt.dxp.api.domain.notice.record.NoticeSearchResponse;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.M_NOTICE;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 공지사항 Mapper
 *
 * @author MINJI
 * @since 2024-10-15
 */
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class, FileMapper.class})
public interface NoticeMapper {

    NoticeMapper INSTANCE = Mappers.getMapper(NoticeMapper.class);

    /**
     * 공지사항 data to SearchResponse 변환 메서드
     *
     * @param data 조회한 data
     * @return NoticeSearchResponse
     * @author MINJI
     * @since 2024-10-15<br />
     */
    @Mappings({
            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(data.getCreateDate(), DateType.YYYYMMDD_FORMAT))"),
            @Mapping(target = "elapsedTime", expression = "java(Converter.getElapsedTime(data.getCreateDate()))")
    })
    NoticeSearchResponse toSearchResponse(NoticeListImpl data);

    /**
     * 공지사항 List<data> to List<NoticeSearchResponse> 변환 메서드<br />
     *
     * @param dataList 조회한 data list
     * @return NoticeSearchResponse list
     * @author MINJI
     * @since 2024-10-15<br />
     */
    List<NoticeSearchResponse> toSearchResponseList(List<NoticeListImpl> dataList);

    /**
     * 공지사항 Entity to DetailSearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return NoticeSearchResponse
     * @author MINJI
     * @since 2024-10-15<br />
     */
    @Mappings({
            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getCreateDate(), DateType.YYYYMMDD_FORMAT))"),
            @Mapping(target = "updateDate", expression = "java(Converter.localDateTimeToString(entity.getUpdateDate(), DateType.YYYYMMDD_FORMAT))"),
            @Mapping(target = "noticeDivisionName", expression = "java(entity.getNoticeDivisionCode().getNoticeDivisionName())"),
            @Mapping(target = "fileCount", expression = "java(file == null ? 0: file.size())"),
            @Mapping(target = "fileList", expression = "java(FileMapper.INSTANCE.toSearchResponseList(file))")
    })
    NoticeDetailSearchResponse toDetailSearchResponse(M_NOTICE entity, List<L_FILE> file);

    M_NOTICE toEntity(NoticeCreateRequest noticeCreateRequest);


    NoticeCreateResponse toCreateResponse(M_NOTICE entity);


}
