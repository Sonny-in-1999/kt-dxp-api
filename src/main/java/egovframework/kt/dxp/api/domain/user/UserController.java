package egovframework.kt.dxp.api.domain.user;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "[CDA-USR] 사용자 조회", description = "[담당자: Juyoung]")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/v1/user/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-USR-001] 사용자 조회")
    public ResponseEntity<ItemResponse<Boolean>> getSearchMyPageList(HttpServletRequest httpServletRequest, @RequestBody UserValidateEncodedRequest request) {
        return ResponseEntity.ok()
                .body(userService.validateUser(httpServletRequest, request));
    }
}
