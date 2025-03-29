package egovframework.kt.dxp.api.domain.file;

import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.notice.record.FileSearchResponse;
import egovframework.kt.dxp.api.entity.L_FILE;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", imports = CommonUtils.class)
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(target = "fileSize", expression = "java( CommonUtils.convertBytes(entityList.getFileSize()) )")
    FileSearchResponse toSearchResponseList(L_FILE entityList);
    List<FileSearchResponse> toSearchResponseList(List<L_FILE> entityList);
}
