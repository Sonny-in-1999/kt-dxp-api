package egovframework.kt.dxp.api.domain.signUp;

import egovframework.kt.dxp.api.common.MaskingUtils;
import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.LoginCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.config.jwt.TokenProvider;
import egovframework.kt.dxp.api.config.jwt.record.TokenResponse;
import egovframework.kt.dxp.api.domain.nice.NiceService;
import egovframework.kt.dxp.api.domain.nice.record.LoginCheckResponse;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeDataDTO;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeRequest;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeResponse;
import egovframework.kt.dxp.api.domain.nice.record.UserLoginResponse;
import egovframework.kt.dxp.api.domain.signUp.record.SignUpCustomRequest;
import egovframework.kt.dxp.api.domain.signUp.record.SignUpRequest;
import egovframework.kt.dxp.api.domain.signUp.record.SignUpTermsRequest;
import egovframework.kt.dxp.api.domain.terms.TermsRepository;
import egovframework.kt.dxp.api.domain.user.UserParentRepository;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.domain.user.enumeration.Gender;
import egovframework.kt.dxp.api.entity.C_ALARM;
import egovframework.kt.dxp.api.entity.C_USR_TRMS_AGREE;
import egovframework.kt.dxp.api.entity.L_TRMS;
import egovframework.kt.dxp.api.entity.M_USR;
import egovframework.kt.dxp.api.entity.M_USR_PRNT;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.C_USR_TRMS_AGREE_KEY;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : SIgnUpService
 * Description   :  
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 ******************************************************************************************/
@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class SignUpService {

    private final MessageConfig messageConfig;
    private final UserRepository userRepository;
    private final UserParentRepository userParentRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final NiceService niceService;
    private final TermsRepository termsRepository;

    @Transactional
    public LoginCheckResponse createSignUp(SignUpRequest parameter) {
        LocalDateTime date = LocalDateTime.now();
        String uuid = UUID.randomUUID().toString();

        //TODO: niceMyInformationData: 암호화된 정보 복호화 하기
        NiceDecodeRequest myDecodeRequest = NiceDecodeRequest.builder()
                                                             .encodedData(parameter.myEncryptionData())
                                                             .operatingSystemType(parameter.operatingSystemType())
                                                             .pushKey(parameter.pushKey())
                                                             .build();

        // 회원 정보 유무 판단
        LoginCheckResponse loginCheckResponse = niceService.loginCheck(myDecodeRequest);
        if (LoginCode.LOGIN_SUCCESS.code().equals(loginCheckResponse.code())) {
            //TODO: 회원이면 로그인 진행
            return loginCheckResponse;
        }

        NiceDecodeResponse niceDecodeResponse = niceService.getNiceDecodedData(myDecodeRequest);
        NiceDecodeDataDTO niceDecodedData = niceDecodeResponse.decodedData();

        String encoderCertificationId = passwordEncoder.encode(niceDecodedData.getCi());

        M_USR mUser = M_USR.builder()
                           // 본인 정보 영역
                           .userId(uuid)
                           .userName(niceDecodedData.getName())
                           .password(encoderCertificationId)
                           .mobilePhoneNumber(niceDecodedData.getMobilePhoneNumber())
                           .genderType(Gender.getGender(niceDecodedData.getGender()).name())
                           .birthDate(niceDecodedData.getBirthDate())
                           // 맞춤 정보 영역
                           .dongCode(parameter.dongCode())
                           .childrenCount(parameter.childrenCount())
                           // 인증 영역
                           .certificationId(niceDecodedData.getCi())
                           .operatingSystemType(parameter.operatingSystemType())
                           //.operatingSystemType(niceDecodedData.getMobileCompany())
                           .pushKey(parameter.pushKey())
                           // 로그인 관련
                           //.recentLoginDateAndTime()
                           //.recentPasswordChangeDateAndTime()
                           //.accessToken()
                           //.refreshToken()
                           .build();

        C_ALARM cAlarm = C_ALARM.builder()
                                .userId(mUser.getUserId())
                                .actAlarmYn(parameter.signUpAlarmData().actAlarmYn())
                                .noticeAlarmYn(parameter.signUpAlarmData().noticeAlarmYn())
                                .rewardAlarmYn(parameter.signUpAlarmData().rewardAlarmYn())
                                .updateDate(date)
                                .mUsr(mUser)
                                .build();

        List<C_USR_TRMS_AGREE_KEY> cUsrTermsAgreeKeyList = new ArrayList<>();
        List<C_USR_TRMS_AGREE> cUsrTermsAgreeList        = new ArrayList<>();
        Iterator<SignUpTermsRequest> signUpTermsRequestIterator = parameter.signUpTermsList()
                                                                           .iterator();
        while (signUpTermsRequestIterator.hasNext()) {
            SignUpTermsRequest signUpTermsRequest = signUpTermsRequestIterator.next();
            L_TRMS termsEntity = termsRepository.findTopByKeyTermsTypeAndKeyTermsStartDateLessThanEqualAndUseYnOrderByKeyTermsStartDateDesc(signUpTermsRequest.termsType(),
                                                Converter.getCurrentLocalDateTime(), UseYn.Y).orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA)
            );

            C_USR_TRMS_AGREE_KEY cUsrTermsAgreeKey = C_USR_TRMS_AGREE_KEY.builder()
                                                                         .userId(mUser.getUserId())
                                                                         .termsStartDate(termsEntity.getKey().getTermsStartDate())
                                                                         .termsType(signUpTermsRequest.termsType())
                                                                         .build();

            C_USR_TRMS_AGREE cUsrTermsAgree = C_USR_TRMS_AGREE.builder()
                                                              .key(cUsrTermsAgreeKey)
                                                              .agreementYn(signUpTermsRequest.agreementYn())
                                                              .createDate(date)
                                                              .mUsr(mUser)
                                                              .build();
            cUsrTermsAgreeKeyList.add(cUsrTermsAgreeKey);
            cUsrTermsAgreeList.add(cUsrTermsAgree);
        }

        //TODO: 14세 미만 여부 판단 후 부모 정보 데이터 존재 여부 확인 후 부모가 성인인지 판단
        //TODO: UNDER_14 - 만 14세 미만 여부 판단
        //TODO: 14세 미만이면 부모 Entity에 값 세팅
        if (Boolean.TRUE.equals(loginCheckResponse.userLoginResponse().isChildren())) {
            //TODO: 값 존재 여부 확인 - 존재하면 진행 parameter.parentEncryptionData()
            Optional<String> parentEncryptionData = Optional.ofNullable(parameter.parentEncryptionData());
            if(parentEncryptionData.get().isEmpty()) {
                throw new ServiceException(ErrorCode.INVALID_PARAMETER);
            }

            NiceDecodeRequest parentDecodeRequest = NiceDecodeRequest.builder()
                                                                     .encodedData(parameter.parentEncryptionData())
                                                                     .build();
            NiceDecodeResponse niceParentDecodeResponse = niceService.getNiceDecodedData(parentDecodeRequest);
            NiceDecodeDataDTO niceParentDecodedData = niceParentDecodeResponse.decodedData();

            //부모 정보도 성인인지 확인해야하나?
            if (!Boolean.TRUE.equals(Boolean.parseBoolean(niceParentDecodedData.getIsAdult()))) {
                throw new ServiceException(ErrorCode.INVALID_PARAMETER);
            }

            M_USR_PRNT mUsrPrnt = M_USR_PRNT.builder()
                                            .userId(mUser.getUserId())
                                            .parentName(niceParentDecodedData.getName())
                                            .parentMobilePhoneNumber(niceParentDecodedData.getMobilePhoneNumber())
                                            .build();
            mUser.addUsrPrnt(mUsrPrnt);
        }

        mUser.addAlram(cAlarm);
        mUser.addTermsAgree(cUsrTermsAgreeList);
        userRepository.save(mUser);

        return niceService.loginCheck(myDecodeRequest);
    }

    @Transactional
    public ItemResponse<Long> deleteMemberInformation() {

        String userId = CommonUtils.getUserId();
        M_USR mUsr = userRepository.findById(userId)
                                   .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA, userId + " doesn't exist."));
        log.info("Delete user information: {}", userId);

        Optional<M_USR_PRNT> mUsrPrnt = userParentRepository.findOneByUserId(userId);
        if (!mUsrPrnt.isEmpty()) {
            log.info("Delete user Parent information");
            mUsr.getMUsrPrnt().deleteUserParentInformation();
        }

        mUsr.getCAlarm().deleteAlarmInformation();
        mUsr.deleteUserInformation();
        userRepository.save(mUsr);
        //TODO: 토큰 만료 로직 추가 작성해야함
        SecurityContextHolder.clearContext();

        return ItemResponse.<Long>builder()
                           .status(messageConfig.getCode(NormalCode.DELETE_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.DELETE_SUCCESS))
                           .item(1L)
                           .build();
    }

    @Transactional
    public LoginCheckResponse createCustomSignUp(SignUpCustomRequest parameter) {
        LocalDateTime date = LocalDateTime.now();
        String uuid = UUID.randomUUID().toString();
        String encoderCertificationId = passwordEncoder.encode(parameter.password());

        M_USR mUser = M_USR.builder()
                           // 본인 정보 영역
                           .userId(uuid)
                           .userName(parameter.userName())
                           .password(encoderCertificationId)
                           .mobilePhoneNumber(parameter.mobilePhoneNumber())
                           //.genderType(Gender.M.name())
                           .genderType(parameter.genderType())
                           .birthDate(parameter.birthDate())
                           // 맞춤 정보 영역
                           .dongCode("10500")
                           .childrenCount(0)
                           // 인증 영역
                           .certificationId(parameter.password())
                           .operatingSystemType(parameter.operatingSystemType())
                           //.operatingSystemType(niceDecodedData.getMobileCompany())
                           //.pushKey(parameter.pushKey())
                           // 로그인 관련
                           //.recentLoginDateAndTime()
                           //.recentPasswordChangeDateAndTime()
                           //.accessToken()
                           //.refreshToken()
                           .build();
        userRepository.saveAndFlush(mUser);

        //로그인 성공 정보 전송
        /*5. 사용자 권한 체크*/
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uuid, parameter.password());
        Authentication authentication = authenticationManagerBuilder.getObject()
                                                                    .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        /*6. JWT 토큰 생성*/
        TokenResponse tokenResponse = tokenProvider.createToken(authentication, false, mUser);
        mUser.updateUserToken(tokenResponse.token(), tokenResponse.refreshToken());

        /* 로그인 시간 추가 */
        LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        mUser.setRecentLoginDate(currentLocalDateTime);

        userRepository.save(mUser);

        //return niceService.loginCheck(myDecodeRequest);
        return LoginCheckResponse.builder()
                                 .code(LoginCode.LOGIN_SUCCESS.code())
                                 .message(messageConfig.getMessage(LoginCode.LOGIN_SUCCESS))
                                 .userLoginResponse(
                                         UserLoginResponse.builder()
                                                          .userId(uuid)
                                                          .userName(
                                                                  MaskingUtils.maskName(parameter.userName()))
                                                          .mobilePhoneNumber(MaskingUtils.maskPhoneNumber(parameter.mobilePhoneNumber()))
                                                          .birthDate(parameter.birthDate())
                                                          .isChildren(CommonUtils.isUnderAge(parameter.birthDate(), 24))
                                                          .gender(Gender.getGender(parameter.genderType()))
                                                          .accessToken(tokenResponse.token())
                                                          .refreshToken(tokenResponse.refreshToken())
                                                          .build()
                                 )
                                 .build();
    }


    @Transactional
    public LoginCheckResponse customLogin(SignUpCustomRequest parameter) {
        LocalDateTime date = LocalDateTime.now();
        //String uuid = UUID.randomUUID().toString();
        //String encoderCertificationId = passwordEncoder.encode(parameter.password());
        //회원 가입여부 확인
        Optional<M_USR> optionalUser = userRepository.findByCertificationId(parameter.password());
        if (optionalUser.isEmpty()) {
            return LoginCheckResponse.builder()
                                     .code(LoginCode.NOT_REGISTERED.code())
                                     .message(messageConfig.getMessage(LoginCode.NOT_REGISTERED))
                                     .userLoginResponse(
                                             UserLoginResponse.builder()
                                                              .build())
                                     .build();
        }

        if (optionalUser.get().getPassword().isEmpty()) {
            log.info("Password is empty");
            String encoderCertificationId = passwordEncoder.encode(parameter.password());
            optionalUser.get().updatePasswordInformation(encoderCertificationId);
            userRepository.save(optionalUser.get());
        }

        //로그인 성공 정보 전송
        /*5. 사용자 권한 체크*/
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(optionalUser.get().getUserId(), optionalUser.get().getCertificationId());
        Authentication authentication = authenticationManagerBuilder.getObject()
                                                                    .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        /*6. JWT 토큰 생성*/
        TokenResponse tokenResponse = tokenProvider.createToken(authentication, false, optionalUser.get());
        optionalUser.get().updateUserToken(tokenResponse.token(), tokenResponse.refreshToken());

        /* 로그인 시간 추가 */
        LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        optionalUser.get().setRecentLoginDate(currentLocalDateTime);

        //return niceService.loginCheck(myDecodeRequest);
        // TODO. Juyoung Chae [Token 생성] 응답 정보에 Access Token, Refresh Token 정보 추가
        return LoginCheckResponse.builder()
                                 .code(LoginCode.LOGIN_SUCCESS.code())
                                 .message(messageConfig.getMessage(LoginCode.LOGIN_SUCCESS))
                                 .userLoginResponse(
                                         UserLoginResponse.builder()
                                                          .userId(optionalUser.get().getUserId())
                                                          .userName(MaskingUtils.maskName(optionalUser.get().getUserName()))
                                                          .mobilePhoneNumber(MaskingUtils.maskPhoneNumber(optionalUser.get().getMobilePhoneNumber()))
                                                          .birthDate(optionalUser.get().getBirthDate())
                                                          .isChildren(CommonUtils.isUnderAge(optionalUser.get().getBirthDate(), 24))
                                                          .gender(Gender.M)
                                                          .accessToken(tokenResponse.token())
                                                          .refreshToken(tokenResponse.refreshToken())
                                                          .build()
                                 )
                                 .build();
    }

}
