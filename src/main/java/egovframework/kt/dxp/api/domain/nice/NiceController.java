package egovframework.kt.dxp.api.domain.nice;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.nice.record.LoginCheckResponse;
import egovframework.kt.dxp.api.domain.nice.record.NiceCheckRequest;
import egovframework.kt.dxp.api.domain.nice.record.NiceCheckResponse;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeRequest;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeResponse;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeResponse.NiceDecodeResponseData;
import egovframework.kt.dxp.api.domain.nice.record.UserLoginResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceController
 * Description   :
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 * 비지니스 로직 서비스로 이동, 컨트롤러에서는 용청/응답 처리 
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-LOG] Nice 본인인증 관련 API", description = "[담당자: Juyoung Chae]")
@RequiredArgsConstructor
public class NiceController {

    private final NiceService niceService;

    @ApiOperation(value = "[CDA-LOG-001] Nice 본인 인증 인코딩 데이터 요청", notes = "Nice 본인 인증 인코딩 데이터를 요청할 때 사용한다.")
    @PostMapping(value = "/v1/nice/verification/enc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<String>> requestEncodingData(@RequestBody @Valid NiceCheckRequest parameter) {
        NiceCheckResponse niceCheckResponse = this.niceService.getNiceEncodedData(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<String>builder()
                        .status(niceCheckResponse.code())
                        .message(niceCheckResponse.message())
                        .item(niceCheckResponse.cipherData())
                        .build());
    }

    @ApiOperation(value = "[CDA-LOG-002] Nice 본인인증 데이터 복호화 요청", notes = "Nice 본인인증 데이터 복호화에 사용한다.")
    @PostMapping(value = "/v1/nice/verification/dec", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<NiceDecodeResponseData>> authorizeSuccess(@RequestBody @Valid NiceDecodeRequest parameter) {
        NiceDecodeResponse niceDecodeResponse = this.niceService.getNiceDecodedData(parameter);

        return ResponseEntity.ok()
                .body(ItemResponse.<NiceDecodeResponseData>builder()
                        .status(niceDecodeResponse.code())
                        .message(niceDecodeResponse.message())
                        .item(niceDecodeResponse.responseData())
                        .build());
    }

    @ApiOperation(value = "[CDA-LOG-003] 본인 인증 데이터 복호화 및 로그인 처리", notes = "로그인/회원가입 시 활용한다.")
    @PostMapping(value = "/v1/nice/verification/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<UserLoginResponse>> loginCheck(@RequestBody @Valid NiceDecodeRequest parameter) {
        LoginCheckResponse loginCheckResponse = this.niceService.loginCheck(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<UserLoginResponse>builder()
                        .status(loginCheckResponse.code())
                        .message(loginCheckResponse.message())
                        .item(loginCheckResponse.userLoginResponse())
                        .build());
    }

    @ApiOperation(value = "[CDA-LOG-006] 로그아웃", notes = "사용자 로그아웃 API")
    @PostMapping(value = "/v1/nice/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Long>> logout(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok()
                             .body(niceService.logout(httpServletRequest));
    }

}
