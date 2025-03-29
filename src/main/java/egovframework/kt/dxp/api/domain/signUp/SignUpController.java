package egovframework.kt.dxp.api.domain.signUp;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.nice.record.LoginCheckResponse;
import egovframework.kt.dxp.api.domain.nice.record.UserLoginResponse;
import egovframework.kt.dxp.api.domain.signUp.record.SignUpCustomRequest;
import egovframework.kt.dxp.api.domain.signUp.record.SignUpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "[CDA-JOI] 회원가입", description = "[담당자: Juyoung Chae]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class SignUpController {
    private final SignUpService signUpService;

    @ApiOperation(value = "[CDA-JOI-001] 회원 정보 추가", notes = "회원 정보를 추가합니다.")
    @PostMapping(value = "/v1/signup/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<UserLoginResponse>> createMemberInformation(@RequestBody @Valid SignUpRequest parameter) {
        LoginCheckResponse loginCheckResponse = this.signUpService.createSignUp(parameter);
        return ResponseEntity.ok()
                             .body(ItemResponse.<UserLoginResponse>builder()
                                               .status(loginCheckResponse.code())
                                               .message(loginCheckResponse.message())
                                               .item(loginCheckResponse.userLoginResponse())
                                               .build());
    }

    @ApiOperation(value = "[CDA-JOI-001] 회원 정보 추가", notes = "회원 정보를 추가합니다.")
    @PostMapping(value = "/v1/signup/custom/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<UserLoginResponse>> createCustomMemberInformation(@RequestBody @Valid SignUpCustomRequest parameter) {
        LoginCheckResponse loginCheckResponse = this.signUpService.createCustomSignUp(parameter);
        return ResponseEntity.ok()
                             .body(ItemResponse.<UserLoginResponse>builder()
                                               .status(loginCheckResponse.code())
                                               .message(loginCheckResponse.message())
                                               .item(loginCheckResponse.userLoginResponse())
                                               .build());
    }

    @ApiOperation(value = "[CDA-JOI-001] 회원 로그인", notes = "회원 로그인.")
    @PostMapping(value = "/v1/signup/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<UserLoginResponse>> customLogin(@RequestBody SignUpCustomRequest parameter) {
        LoginCheckResponse loginCheckResponse = this.signUpService.customLogin(parameter);
        return ResponseEntity.ok()
                             .body(ItemResponse.<UserLoginResponse>builder()
                                               .status(loginCheckResponse.code())
                                               .message(loginCheckResponse.message())
                                               .item(loginCheckResponse.userLoginResponse())
                                               .build());
    }

    @ApiOperation(value = "[CDA-JOI-002] 회원 정보 삭제", notes = "회원 정보를 삭제합니다.")
    @PostMapping(value = "/v1/signup/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Long>> deleteMemberInformation() {
        //TODO: Request parameter 받지 않고 토큰에서 ID 추출하여 삭제하도록 수정
        return ResponseEntity.ok()
                             .body(signUpService.deleteMemberInformation());
    }

}
