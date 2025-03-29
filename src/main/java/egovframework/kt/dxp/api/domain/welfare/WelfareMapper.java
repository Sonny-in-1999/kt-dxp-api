package egovframework.kt.dxp.api.domain.welfare;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.file.FileMapper;
import egovframework.kt.dxp.api.domain.welfare.record.*;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.L_WLFR;
import egovframework.kt.dxp.api.entity.L_WLFR_BTN;
import egovframework.kt.dxp.api.entity.M_CD;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class, FileMapper.class, Collections.class})
public interface WelfareMapper {

    WelfareMapper INSTANCE = Mappers.getMapper(WelfareMapper.class);


    /* 복지 정책 상세 매퍼 */
    @Mappings({
            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getCreateDate(), DateType.YYYYMMDD_FORMAT))"),
            @Mapping(target = "searchCount", expression = "java(entity.getWelfareUserList().size())"),
            @Mapping(target = "welfareDivisionName", expression = "java(entity.getWelfareDivisionCode().getWelfareDivisionName())"),
            @Mapping(target = "welfareDetailDivisionName", expression = "java(entity.getWelfareDetailDivisionCode() != null ? entity.getWelfareDetailDivisionCode().getWelfareDetailDivisionName() : null)"),
//            @Mapping(target = "mainImage", expression = "java(AbstractImageFilePreviewer.generateOriginalImageFromBytes(entity.getMainImage()))"),
            @Mapping(target = "fileCount", expression = "java(fileList == null ? 0: fileList.size())"),
            @Mapping(target = "fileList", expression = "java(FileMapper.INSTANCE.toSearchResponseList(fileList))"),
            @Mapping(target = "mainImageFile", expression = "java(FileMapper.INSTANCE.toSearchResponseList(mainImageFileList))"),
            @Mapping(target = "buttonList", expression = "java(WelfareMapper.INSTANCE.toWelfareButtonResponseList(entity.getWelfareButtonList()))")
    })
    WelfareDetailSearchResponse toSearchDetailResponse(L_WLFR entity, List<L_FILE> fileList, List<L_FILE> mainImageFileList);

    /* 복지 정책 목록 매퍼 */
    @Mappings({
            @Mapping(target = "searchCount", expression = "java(entity.getWelfareUserList().size())"),
            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getCreateDate(), DateType.YYYYMMDD_FORMAT))"),
            @Mapping(target = "welfareDivisionName", expression = "java(entity.getWelfareDivisionCode().getWelfareDivisionName())"),
            @Mapping(target = "welfareDetailDivisionName", expression = "java(entity.getWelfareDetailDivisionCode() != null ? entity.getWelfareDetailDivisionCode().getWelfareDetailDivisionName() : null)"),
            @Mapping(target = "mainImageFile", expression = "java(Collections.singletonList(FileMapper.INSTANCE.toSearchResponseList(fileList)))")
    })
    WelfareListSearchResponse toSearchListResponse(L_WLFR entity, L_FILE fileList);
    default List<WelfareListSearchResponse> toSearchListResponseList(List<L_WLFR> entityList, List<L_FILE> fileList) {
        List<WelfareListSearchResponse> responseList = new ArrayList<>();

        // fileList를 Map으로 변환 (BulletinBoardSequenceNumber가 키가 되는 맵)
        Map<Integer, L_FILE> fileMap = new HashMap<>();
        for (L_FILE file : fileList) {
            fileMap.put(file.getBulletinBoardSequenceNumber(), file);
        }

        // entityList를 순회하며 Map에서 해당 값을 조회
        for (L_WLFR entity : entityList) {
            L_FILE file = fileMap.get(entity.getWelfareSequenceNumber());

            if (file != null) {
                responseList.add(toSearchListResponse(entity, file));
            } else {
                responseList.add(toSearchListResponse(entity, null));  // 일치하는 파일이 없으면 null을 넘김
            }
        }
        return responseList;
    }

    /* 복지 정책 세부 분류 콤보 목록 래퍼 */
    @Mappings({
            @Mapping(target = "welfareDetailDivision", source = "key.codeId"),
            @Mapping(target = "welfareDetailDivisionName", source = "codeName")
    })
    WelfareComboResponse toComboResponse(M_CD entity);
    List<WelfareComboResponse> toComboResponseList(List<M_CD> entityList);


    /* 복지 정책 분류 코드 목록 래퍼 */
    @Mappings({
            @Mapping(target = "welfareDivision", source = "key.codeId"),
            @Mapping(target = "welfareDivisionName", source = "codeName")
    })
    WelfareCodeResponse toCodeResponse(M_CD entity);

    List<WelfareCodeResponse> toCodeResponseList(List<M_CD> entityList);


//    /* 복지 정책 상세 조회 파일 목록 매퍼 */
//    @Mappings({
//            @Mapping(target = "bulletinBoardDivision", source = "bulletinBoardDivision"),
//            @Mapping(target = "saveFileName", source = "saveFileName"),
//            @Mapping(target = "actualFileName", source = "actualFileName"),
//    })
//    WelfareFileSearchResponse toSearchFileResponseList(L_FILE entityList);
//
//    List<WelfareFileSearchResponse> toSearchFileResponseList(List<L_FILE> entityList);


    /* 복지 정책 상세 조회 버튼 목록 매퍼 */
    @Mappings({
            @Mapping(target = "buttonSequenceNumber", source = "key.buttonSequenceNumber"),
            @Mapping(target = "buttonName", source = "buttonName"),
            @Mapping(target = "linkUniformResourceLocator", source = "linkUniformResourceLocator")
    })
    WelfareButtonSearchResponse toWelfareButtonResponse(L_WLFR_BTN entity);

    List<WelfareButtonSearchResponse> toWelfareButtonResponseList(List<L_WLFR_BTN> entityList);
}
