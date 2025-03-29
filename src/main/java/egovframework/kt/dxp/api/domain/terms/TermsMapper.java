package egovframework.kt.dxp.api.domain.terms;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.terms.record.TermsGroupSearchResponse;
import egovframework.kt.dxp.api.domain.terms.record.TermsSearchResponse;
import egovframework.kt.dxp.api.entity.L_TRMS;
import egovframework.kt.dxp.api.entity.M_CD;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 약관 Mapper
 *
 * @author BITNA
 * @apiNote 2024-11-05 BITNA 약관 유형별로 조회가능하도록 변경
 * 2024-11-06 BITNA 유형 조회 cd테이블로 변경
 * @since 2024-10-18<br />
 */
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface TermsMapper {
    TermsMapper INSTANCE = Mappers.getMapper(TermsMapper.class);

    /**
     * 약관 Entity to SearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return TermsSearchResponse
     * @author BITNA
     * @since 2024-10-18생성<br />
     */
    @Mappings({
            @Mapping(target = "termsType", source = "key.codeId"),
            @Mapping(target = "termsTypeName", source = "codeName"),
            @Mapping(target = "essentialYn", source = "description")
    })
    TermsGroupSearchResponse toGroupSearchResponse(M_CD entity);

    List<TermsGroupSearchResponse> toGroupSearchResponseList(List<M_CD> entity);

    /**
     * 약관 Entity to SearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return TermsSearchResponse
     * @author BITNA
     * @since 2024-10-18생성<br />
     */
    @Mappings({
            @Mapping(target = "termsType", source = "termsTypeCode.termsType"),
            @Mapping(target = "termsStartDate", expression = "java(Converter.localDateTimeToString(entity.getKey().getTermsStartDate(), DateType.YYYYMMDD_FORMAT))"),
            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getCreateDate(), DateType.YYYYMMDD_FORMAT))"),
            @Mapping(target = "updateDate", expression = "java(Converter.localDateTimeToString(entity.getUpdateDate(), DateType.YYYYMMDD_FORMAT))")
    })
    TermsSearchResponse toSearchResponse(L_TRMS entity);

    /**
     * 약관 List<Entity> to List<TermsSearchResponse> 변환 메서드<br />
     * MapStruct 에서 toSearchResponse 메서드 를 활용 하기 때문에 따로 매핑 불필요.
     *
     * @param entityList 조회한 entity list
     * @return TermsSearchResponse list
     * @author BITNA
     * @since 2024-10-18<br />
     */
    List<TermsSearchResponse> toSearchResponseList(List<L_TRMS> entityList);

//    /**
//     * 약관 Entity to TermsCreateResponse 변환 메서드<br />
//     *
//     * @param entity 저장한 엔티티
//     * @return TermsCreateResponse 추가 응답 record
//     * @author BITNA
//     * @since 2024-10-18<br />
//     */
//    @Mappings({
//            @Mapping(target = "termsType", source = "key.termsType"),
//            @Mapping(target = "termsStartDate", expression = "java(Converter.localDateTimeToString(entity.getKey().gettermsStartDate(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getCrtDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "updateDate", expression = "java(Converter.localDateTimeToString(entity.getUpdtDt(), DateType.YYYYMMDD_FORMAT))")
//    })
//    TermsCreateResponse toCreateResponse(L_TERMS entity);

//    /**
//     * 약관 Entity to TermsModifyResponse 변환 메서드<br />
//     *
//     * @param entity 수정한 엔티티
//     * @return TermsModifyResponse 수정 응답 record
//     * @author BITNA
//     * @since 2024-10-18<br />
//     */
//    @Mappings({
//            @Mapping(target = "termsType", source = "key.termsType"),
//            @Mapping(target = "termsStartDate", expression = "java(Converter.localDateTimeToString(entity.getKey().gettermsStartDate(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getCrtDt(), DateType.YYYYMMDD_FORMAT))"),
//            @Mapping(target = "updateDate", expression = "java(Converter.localDateTimeToString(entity.getUpdtDt(), DateType.YYYYMMDD_FORMAT))")
//    })
//    TermsModifyResponse toModifyResponse(L_TERMS entity);
//
//    /**
//     * 추가 시 키 추출용 메서드<br />
//     *
//     * @param termsCreateRequest 추가 요청 record
//     * @return entity key
//     * @author BITNA
//     * @since 2024-10-18<br />
//     */
//    L_TERMS_KEY toEntityKey(TermsCreateRequest termsCreateRequest);
//
//    /**
//     * 수정 시 키 추출용 메서드<br />
//     *
//     * @param termsModifyRequest 수정 요청 record
//     * @return entity key
//     * @author BITNA
//     * @since 2024-10-18<br />
//     */
//    L_TERMS_KEY toEntityKey(TermsModifyRequest termsModifyRequest);
//
//    /**
//     * 삭제 시 키 추출용 메서드<br />
//     *
//     * @param termsDeleteRequest 수정 요청 record
//     * @return entity key
//     * @author BITNA
//     * @since 2024-10-18<br />
//     */
//    L_TERMS_KEY toEntityKey(TermsDeleteRequest termsDeleteRequest);
//
//    /**
//     * TermsCreateRequest to 약관 Entity 변환 메서드<br />
//     * 복수 건을 저장하고 싶을 경우에는 toEntityCreateList 를 추가<br />
//     * List<L_TERMS> toEntityCreateResponseList(List<TermsCreateRequest> termsCreateRequest);
//     *
//     * @author BITNA
//     * @since 2024-10-18<br />
//     */
//    @Mappings({
//            @Mapping(target = "key.termsType", source = "termsType"),
//            @Mapping(target = "key.termsStartDate", dateFormat = "yyyy-MM-dd"),
//            @Mapping(target = "updateDate", dateFormat = "yyyy-MM-dd")
//    })
//    L_TERMS toEntity(TermsCreateRequest termsCreateRequest);
//
//    /**
//     * TermsUpdateRequest 를 약관 entity 에 update 하는 메서드
//     *
//     * @author BITNA
//     * @since 2024-10-18<br />
//     */
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
//    @Mappings({
//            @Mapping(target = "key.termsType", source = "termsType"),
//            @Mapping(target = "key.termsStartDate", dateFormat = "yyyy-MM-dd"),
//            @Mapping(target = "updateDate", dateFormat = "yyyy-MM-dd")
//    })
//    L_TERMS updateFromRequest(TermsModifyRequest termsModifyRequest, @MappingTarget L_TERMS entity);
}
