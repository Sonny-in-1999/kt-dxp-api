package egovframework.kt.dxp.api.config.jwt.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : JwtValidation
 * Description   :  
 * Creation Date : 2024-10-22
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-22, Juyoung Chae, 최초작성
 ******************************************************************************************/

/**
 * Jwt token valid process 에서 사용하는 dto
 *
 * @author GEONLEE
 * @since 2024-03-29<br />
 */
@Getter
@Setter
@AllArgsConstructor
public class JwtValidation {
    private boolean valid;
    private String userId;
    private String accessToken;
}
