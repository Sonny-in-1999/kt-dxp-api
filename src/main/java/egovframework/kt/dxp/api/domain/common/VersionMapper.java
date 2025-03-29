//package egovframework.kt.dxp.api.domain.common;
//
//
//import egovframework.kt.dxp.api.common.converter.Converter;
//import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
//import egovframework.kt.dxp.api.domain.common.record.VersionResponse;
//import egovframework.kt.dxp.api.entity.L_VER;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Mappings;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
//public interface VersionMapper {
//
//    VersionMapper INSTANCE = Mappers.getMapper(VersionMapper.class);
//
//
//    @Mappings({
//            @Mapping(target = "latestVersion", source = "key.versionId"),
//            @Mapping(target = "createDate", expression = "java(Converter.localDateTimeToString(entity.getKey().getCreateDate(), DateType.YYYYMMDD_FORMAT))")
//    })
//    VersionResponse toResponse(L_VER entity);
//}
