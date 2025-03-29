package egovframework.kt.dxp.api.domain.authority;

import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.authority.record.AuthTokenResponse;
import egovframework.kt.dxp.api.domain.authority.record.AuthValidResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

@Api(tags = "[CDA-LOG] 토큰 유효성", description = "[담당자: Juyoung Chae]")
public class AuthTokenController {

    private final MessageConfig messageConfig;
    private final AuthTokenService authTokenService;

    /** 토큰 유효성 검증 */
    @PostMapping("/v1/valid/authenticate")
    @ApiOperation(value = "[CDA-LOG-004] JWT Access Token 유효성 검증용", notes = "Header에 Token을 추출하여 유효한지 확인한다.")
    public ResponseEntity<ItemResponse<AuthValidResponse>> validAuthorize(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok()
                             .body(authTokenService.validAuthorize(httpServletRequest));
    }

    /** 토큰 재발행 */
    @PostMapping("/v1/valid/authenticate/reissue")
    @ApiOperation(value = "[CDA-LOG-005] JWT Access Token 재발급", notes = "Header에 Refresh Token을 추출하여 유효하면 Access Token을 재발행한다.")
    public ResponseEntity<ItemResponse<AuthTokenResponse>> reIssueToken(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok()
                             .body(authTokenService.reIssueToken(httpServletRequest));
    }

}
