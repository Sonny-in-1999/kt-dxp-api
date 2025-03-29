package egovframework.kt.dxp.api.domain.vote;

import java.util.List;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.vote.record.VoteDetailSearchResponse;
import egovframework.kt.dxp.api.domain.vote.record.VoteItemSearchResponse;
import egovframework.kt.dxp.api.domain.vote.record.VoteSearchResponse;
import egovframework.kt.dxp.api.entity.L_VOTE;
import egovframework.kt.dxp.api.entity.L_VOTE_ITEM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 투표 Mapper
 *
 * @author BITNA
 * @since 2024-10-31<br />
 */
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface VoteMapper {
    VoteMapper INSTANCE = Mappers.getMapper(VoteMapper.class);

//    /**
//     * 투표 Entity to SearchResponse 변환 메서드
//     *
//     * @param entity 조회한 entity
//     * @return VoteSearchResponse
//     * @author BITNA
//     * @since 2024-10-31생성<br />
//     */
//    @Mappings({
//            @Mapping(target = "startDate", expression = "java(Converter.localDateTimeToString(entity.getStartDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "endDate", expression = "java(Converter.localDateTimeToString(entity.getEndDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "limitTime", expression = "java( Converter.getElapsedTime(entity.getEndDt()) )"),
//            @Mapping(target = "")
//    })
//    VoteDetailSearchResponse toSearchResponse(L_VOTE entity);

    /**
     * 투표 List<Entity> to List<VoteItemSearchResponse> 변환 메서드<br />
     * MapStruct 에서 toSearchResponse 메서드 를 활용 하기 때문에 따로 매핑 불필요.
     *
     * @param entityList 조회한 entity
     * @return VoteItemSearchResponse
     * @author BITNA
     * @since 2024-10-31<br />
     */
    @Mappings({
            @Mapping(target = "itemSequenceNumber", source = "key.itemSequenceNumber"),
//            @Mapping(target = "selectedCount", source = "voteResult.selectedCount"),
//            @Mapping(target = "itemSelectedRate", source = "voteResult.itemSelectedRate")
    })
    VoteItemSearchResponse toVoteItemSearchResponse(L_VOTE_ITEM entity);
    List<VoteItemSearchResponse> toVoteItemSearchResponseList(List<L_VOTE_ITEM> entity);

//    /**
//     * 투표 Entity to VoteCreateResponse 변환 메서드<br />
//     *
//     * @param entity 저장한 엔티티
//     * @return VoteCreateResponse 추가 응답 record
//     * @author BITNA
//     * @since 2024-10-31<br />
//     */
//    @Mappings({
//            @Mapping(target = "startDate", expression = "java(Converter.localDateTimeToString(entity.getStartDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "endDate", expression = "java(Converter.localDateTimeToString(entity.getEndDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "crtDt", expression = "java(Converter.localDateTimeToString(entity.getCrtDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "updtDt", expression = "java(Converter.localDateTimeToString(entity.getUpdtDt(), DateType.YYYYMMDD_FORMAT))")
//    })
//    VoteCreateResponse toCreateResponse(L_VOTE entity);
//
//    /**
//     * 투표 Entity to VoteModifyResponse 변환 메서드<br />
//     *
//     * @param entity 수정한 엔티티
//     * @return VoteModifyResponse 수정 응답 record
//     * @author BITNA
//     * @since 2024-10-31<br />
//     */
//    @Mappings({
//            @Mapping(target = "startDate", expression = "java(Converter.localDateTimeToString(entity.getStartDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "endDate", expression = "java(Converter.localDateTimeToString(entity.getEndDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "crtDt", expression = "java(Converter.localDateTimeToString(entity.getCrtDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "updtDt", expression = "java(Converter.localDateTimeToString(entity.getUpdtDt(), DateType.YYYYMMDD_FORMAT))")
//    })
//    VoteModifyResponse toModifyResponse(L_VOTE entity);
//
//    /**
//     * VoteCreateRequest to 투표 Entity 변환 메서드<br />
//     * 복수 건을 저장하고 싶을 경우에는 toEntityCreateList 를 추가<br />
//     * List<L_VOTE> toEntityCreateResponseList(List<VoteCreateRequest> VoteCreateRequest);
//     *
//     * @author BITNA
//     * @since 2024-10-31<br />
//     */
//    @Mappings({
//            @Mapping(target = "startDate", dateFormat = "yyyy-MM-dd"),
//            @Mapping(target = "endDate", dateFormat = "yyyy-MM-dd"),
//            @Mapping(target = "updtDt", dateFormat = "yyyy-MM-dd")
//    })
//    L_VOTE toEntity(VoteCreateRequest VoteCreateRequest);
//
//    /**
//     * VoteUpdateRequest 를 투표 entity 에 update 하는 메서드
//     *
//     * @author BITNA
//     * @since 2024-10-31<br />
//     */
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
//    @Mappings({
//            @Mapping(target = "startDate", dateFormat = "yyyy-MM-dd"),
//            @Mapping(target = "endDate", dateFormat = "yyyy-MM-dd"),
//            @Mapping(target = "updtDt", dateFormat = "yyyy-MM-dd")
//    })
//    L_VOTE updateFromRequest(VoteModifyRequest VoteModifyRequest, @MappingTarget L_VOTE entity);
}
