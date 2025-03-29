package egovframework.kt.dxp.api.domain.user;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.user.UserEnum.ValidationType;
import egovframework.kt.dxp.api.entity.M_USR;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class UserService {

    private final UserRepository userRepository;
    private final MessageConfig messageConfig;
    private final UserValidatorUtil userValidatorUtil;
    public static final String DECRYPT_KEY_VALUE = "CCDID";
    public static final String API_HEADER_KEY = "X-API-Key";


    @Transactional
    public ItemResponse<Boolean> validateUser(HttpServletRequest httpServletRequest, UserValidateEncodedRequest request) {
        ValidationType type = null;
        String value;

        String apiKey = httpServletRequest.getHeader(API_HEADER_KEY);
        if (apiKey == null || apiKey.isEmpty()) {
            return ItemResponse.<Boolean>builder()
                               .status(messageConfig.getCode(ErrorCode.INVALID_PARAMETER))
                               .message(messageConfig.getMessage(ErrorCode.INVALID_PARAMETER))
                               .item(false)
                               .build();
        }
        //// NOTE: Test를 위해 생성함 나중에 삭제
        //log.info("SHA256: {}", userValidatorUtil.encrypt(DECRYPT_KEY_VALUE));

        if(!userValidatorUtil.validate(DECRYPT_KEY_VALUE, apiKey)){
            return ItemResponse.<Boolean>builder()
                               .status(messageConfig.getCode(ErrorCode.NOT_AUTHENTICATION))
                               .message(messageConfig.getMessage(ErrorCode.NOT_AUTHENTICATION))
                               .item(false)
                               .build();
        }

        try {
            UserValidateRequest userValidateRequest = UserValidateRequest.fromEncodedString(request.encodedData());
            type = ValidationType.fromString(userValidateRequest.validationType());
            value = userValidateRequest.value();
        } catch (IllegalArgumentException e) {
            return ItemResponse.<Boolean>builder()
                               .status(messageConfig.getCode(ErrorCode.INVALID_PARAMETER))
                               .message(messageConfig.getMessage(ErrorCode.INVALID_PARAMETER))
                               .item(false)
                               .build();
        }

        // NOTE: User가 여러명일 수 있음
        // NOTE: 회원 탈퇴 유무도 체크 확인해야함
        switch (type) {
            case PHONE_NUM -> {
                String mobilePhoneNumber = value.replaceAll("-", "");
                Optional<M_USR> optionalUser = userRepository.findByMobilePhoneNumber(mobilePhoneNumber);
                if (optionalUser.isEmpty()) {
                    return ItemResponse.<Boolean>builder()
                                       .status(messageConfig.getCode(ErrorCode.NO_DATA))
                                       .message(messageConfig.getMessage(ErrorCode.NO_DATA))
                                       .item(false)
                                       .build();
                }
            }
            case USER_CI -> {
                Optional<M_USR> optionalUser = userRepository.findByCertificationId(value);
                if (optionalUser.isEmpty()) {
                    return ItemResponse.<Boolean>builder()
                                       .status(messageConfig.getCode(ErrorCode.NO_DATA))
                                       .message(messageConfig.getMessage(ErrorCode.NO_DATA))
                                       .item(false)
                                       .build();
                }
            }
            default -> throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        return ItemResponse.<Boolean>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(true)
                .build();
    }
}
