package egovframework.kt.dxp.api.domain.nice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceResDTO
 * Description   :  
 * Creation Date : 2024-10-18
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-18, Juyoung Chae, 최초작성
 ******************************************************************************************/
@Getter
@Setter
@ApiModel(value = "NiceResDTO - Nice 인코딩 데이터 리턴 DTO")
@Deprecated
public class NiceResDTO {
    @ApiModelProperty(value = "Nice 로 부터 전달받은 인코딩 데이터")
    private String encodeData;
}
