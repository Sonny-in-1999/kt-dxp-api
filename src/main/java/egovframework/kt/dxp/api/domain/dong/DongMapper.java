package egovframework.kt.dxp.api.domain.dong;

import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.domain.dong.record.DongSearchResponse;
import egovframework.kt.dxp.api.entity.M_DONG;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {Converter.class, DateType.class})
public interface DongMapper {

    DongMapper INSTANCE = Mappers.getMapper(DongMapper.class);

    @Mappings({
            @Mapping(target = "dongCreateDate", expression = "java(Converter.localDateTimeToString(entity.getDongCreateDate().atStartOfDay(), DateType.YYYYMMDD_FORMAT))")
    })
    DongSearchResponse toSearchResponse(M_DONG entity);

    List<DongSearchResponse> toSearchResponseList(List<M_DONG> entityList);
}
