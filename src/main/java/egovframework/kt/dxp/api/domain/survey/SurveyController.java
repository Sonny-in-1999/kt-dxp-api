package egovframework.kt.dxp.api.domain.survey;

import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.survey.record.AnswerSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.QuestionSearchRequest;
import egovframework.kt.dxp.api.domain.survey.record.SurveyDetailSearchRequest;
import egovframework.kt.dxp.api.domain.survey.record.SurveyDetailSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.SurveyFileSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.SurveySearchRequest;
import egovframework.kt.dxp.api.domain.survey.record.SurveyUserCreateRequest;
import egovframework.kt.dxp.api.domain.survey.record.TotalSurveySearchResponse;
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
@Api(tags = "[CDA-SER] 설문 참여", description = "[담당자: MINJI]")
// @SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping(value = "/v1/survey/list/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-014] 설문 목록 조회", notes = """
            # Searchable Field
            - surveyTabDivisionCode [전체[01], 진행[02], 종료[03]]
            - surveySubDivisionCode [전체[01], 참여가능[02], 참여한 설문[03], 미참여[04]]
            """)
    public ResponseEntity<GridResponse<TotalSurveySearchResponse>> getSearchSurveyList(
            @RequestBody @Valid SurveySearchRequest request) {
        return ResponseEntity.ok()
                .body(surveyService.getSearchSurveyList(request));
    }

    @ApiOperation(value = "[CDA-SER-015] 설문 상세 조회")
    @PostMapping(value = "/v1/survey/detail/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<SurveyDetailSearchResponse>> getDetailSearchSurvey(
            @RequestBody @Valid SurveyDetailSearchRequest request) {
        return ResponseEntity.ok().body(surveyService.getDetailSearchSurvey(request));
    }

//    @PostMapping(value = "/v1/survey/image/search", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "[CDA-SER-016] 설문 이미지 조회", notes = """
//            # Searchable Field
//             - fileName [파일명]
//             - fileExtension [파일 확장자]
//             - image [base64 image]
//            """)
//    public ResponseEntity<ItemsResponse<SurveyFileSearchResponse>> getProposalFile(@RequestBody @Valid SurveyDetailSearchRequest request) {
//        return ResponseEntity.ok()
//                .body(surveyService.getImageFile(request));
//    }

    @ApiOperation(value = "[CDA-SER-017] 설문 답변 조회")
    @PostMapping(value = "/v1/survey/answer/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GridResponse<AnswerSearchResponse>> getAnswerSearchSurvey(
            @RequestBody @Valid QuestionSearchRequest request) {
        return ResponseEntity.ok().body(surveyService.getAnswerSearchSurvey(request));
    }

    @ApiOperation(value = "[CDA-SER-018] 설문 참여 추가")
    @PostMapping(value = "/v1/survey/user/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Long>> createSurveyUser(
            @RequestBody @Valid SurveyUserCreateRequest request) {
        return ResponseEntity.ok().body(surveyService.createSurveyUser(request));
    }

}
