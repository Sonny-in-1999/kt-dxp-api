package egovframework.kt.dxp.api.domain.accountManagement;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountInformationSearchResponse;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountManagementModifyResponse;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountManagementSearchResponse;
import egovframework.kt.dxp.api.entity.M_USR;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : AccountManagementMapper
 * Description   :  계정정보 관리 Mapper
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 *                 2 - 2024-10-28, BITNA, 계정정보 조회 추가
 ******************************************************************************************/
@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class, CommonUtils.class})
public interface AccountManagementMapper {

    AccountManagementMapper INSTANCE = Mappers.getMapper(AccountManagementMapper.class);

    /**
     * 사용자 Entity to SearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return AccountManagementSearchResponse
     * @author MINJI
     * @since 2024-10-18<br />
     */
    @Mapping(target = "dongCodeName", expression = "java(entity.getDong().getDongName())")
    AccountManagementSearchResponse toSearchResponse(M_USR entity);

    /**
     * 사용자 Entity to SearchResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return AccountManagementSearchResponse
     * @author MINJI
     * @since 2024-10-18<br />
     */
    @Mapping(target = "dongCodeName", expression = "java(entity.getDong().getDongName())")
    AccountManagementModifyResponse toModifyResponse(M_USR entity);

    /**
     * 사용자 Entity to toSearchAccountInformationResponse 변환 메서드
     *
     * @param entity 조회한 entity
     * @return AccountInformationSearchResponse
     * @author BITNA
     * @since 2024-10-28<br />
     */
    @Mapping(target = "mobilePhoneNumber", expression = "java( CommonUtils.maskingMobile(entity.getMobilePhoneNumber()) )")
    @Mapping(target = "birthDate", expression = "java( CommonUtils.formatBirthDate(entity.getBirthDate()) )")
    AccountInformationSearchResponse toSearchAccountInformationResponse(M_USR entity);

}
