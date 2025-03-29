package egovframework.kt.dxp.api.domain.proposal;

import egovframework.kt.dxp.api.common.MaskingUtils;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.proposal.record.ProposalComboResponse;
import egovframework.kt.dxp.api.domain.proposal.record.ProposalCreateResponse;
import egovframework.kt.dxp.api.domain.proposal.record.ProposalDetailSearchResponse;
import egovframework.kt.dxp.api.domain.proposal.record.ProposalSearchResponse;
import egovframework.kt.dxp.api.entity.C_PRPS;
import egovframework.kt.dxp.api.entity.M_CD;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 정책 제안 Mapper
 *
 * @author BITNA
 * @apiNote 2024-10-31 BITNA DB 컬럼 변경 result -> answer
 * 2024-11-18 BITNA deleteAuthYn 정책구분코드가 제안[01]일 경우만 Y
 * @since 2024-10-29<br />
 */
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class, CommonUtils.class, MaskingUtils.class})
public interface ProposalMapper {
    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    /**
     * 정책 제안 Entity to SearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return ProposalSearchResponse
     * @author BITNA
     * @since 2024-10-29생성<br />
     */
    @Mappings({
            @Mapping(target = "createDate", expression = "java(\"02\".equals(entity.getProposalProgressDivision()) ? Converter.localDateTimeToString(entity.getAnswerCreateDate(), DateType.YYYYMMDD_FORMAT) : Converter.localDateTimeToString(entity.getCreateDate(), DateType.YYYYMMDD_FORMAT))"),
            //@Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getCreateDate(), DateType.YYYYMMDD_FORMAT))"),
            @Mapping(target = "proposalDivision", source = "proposalDivisionCode.proposalDivisionName"),
            @Mapping(target = "proposalProgressDivision", source = "proposalProgressDivisionCode.proposalProgressDivisionName"),
            @Mapping(target = "createUserName", expression = "java( MaskingUtils.name(entity.getCreateUserInfo() != null ? entity.getCreateUserInfo().getUserName() : null) )"),
    })
    ProposalSearchResponse toSearchResponse(C_PRPS entity);

    List<ProposalSearchResponse> toSearchResponseList(List<C_PRPS> entityList);

    /**
     * 정책 상세 Entity to ProposalDetailSearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return ProposalDetailSearchResponse
     * @author BITNA
     * @since 2024-10-29생성<br />
     */
    @Mappings({
            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getCreateDate(), DateType.YYYYMMDDHHMM_FORMAT))"),
            @Mapping(target = "updateDate", expression = "java(Converter.localDateTimeToString(entity.getUpdateDate(), DateType.YYYYMMDDHHMM_FORMAT))"),
            @Mapping(target = "proposalDivision", source = "proposalDivisionCode.proposalDivisionName"),
            @Mapping(target = "proposalProgressDivision", source = "proposalProgressDivisionCode.proposalProgressDivisionName"),
            @Mapping(target = "createUserName", expression = "java( MaskingUtils.name(entity.getCreateUserInfo() != null ? entity.getCreateUserInfo().getUserName() : null) )"),
            @Mapping(target = "deleteAuthYn", expression = "java( getDeleteAuthYn(entity) )"),
            @Mapping(target = "answerCreateDate", expression = "java(Converter.localDateTimeToString(entity.getAnswerCreateDate(), DateType.YYYYMMDDHHMM_FORMAT))"),
            @Mapping(target = "answerCreateUserName", expression = "java( entity.getAnswerCreateUserInfo() != null ? entity.getAnswerCreateUserInfo().getUserName() : null )"),
    })
    ProposalDetailSearchResponse toDetailSearchResponse(C_PRPS entity);

    default String getDeleteAuthYn(C_PRPS entity) {
        //정책진행구분 코드가 제안[01]일 때만 삭제 가능
        return "01".equals(entity.getProposalProgressDivision()) && entity.getCreateUserId().equals(CommonUtils.getUserId()) ? "Y" : "N";
    }

    /**
     * 정책 제안 Entity to ProposalCreateResponse 변환 메서드<br />
     *
     * @param entity 저장한 엔티티
     * @return ProposalCreateResponse 추가 응답 record
     * @author BITNA
     * @since 2024-10-29<br />
     */
    @Mappings({
            @Mapping(target = "proposalSequenceNumber", source = "proposalSequenceNumber")
    })
    ProposalCreateResponse toCreateResponse(C_PRPS entity);

    /**
     * 정책 제안 M_CD Entity to ProposalComboResponse 변환 메서드<br />
     *
     * @param entity 저장한 엔티티
     * @return ProposalComboResponse 구분 코드 콤보 response
     * @author BITNA
     * @since 2024-10-30<br />
     */
    @Mappings({
            @Mapping(target = "proposalDivision", source = "key.codeId"),
            @Mapping(target = "proposalDivisionName", source = "codeName")
    })
    ProposalComboResponse toComboResponse(M_CD entity);

    List<ProposalComboResponse> toComboResponseList(List<M_CD> entity);
}
