package egovframework.kt.dxp.api.domain.accountManagement;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountInformationSearchResponse;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountManagementModifyRequest;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountManagementModifyResponse;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountManagementSearchResponse;
import egovframework.kt.dxp.api.domain.dong.DongRepository;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.entity.M_DONG;
import egovframework.kt.dxp.api.entity.M_USR;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : AccountManagementService
 * Description   :  계정정보 관리 Service
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 *                 2 - 2024-10-28, BITNA, 계정정보 조회 추가
 ******************************************************************************************/
@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class AccountManagementService {

    private final UserRepository userRepository;
    private final DongRepository dongRepository;
    private static final AccountManagementMapper accountmanagementMapper = AccountManagementMapper.INSTANCE;
    private final MessageConfig messageConfig;


    /**
     * 계정 정보 조회
     *
     * @return AccountmanagementResponse 계정 조회 결과
     * @author MinJi Chae
     * @since 2024-10-18<br />
     */
    @Transactional
    public ItemResponse<AccountManagementSearchResponse> getSearchAccountmanagemen() {

        String userId = CommonUtils.getUserId();
        M_USR entity = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorCode.NO_DATA));

        return ItemResponse.<AccountManagementSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(accountmanagementMapper.toSearchResponse(entity))
                .build();
    }

    /**
     * 계정 정보 수정
     *
     * @return NoticeSearchResponse 공지사항 조회 결과
     * @author MinJi Chae
     * @since 2024-10-18<br />
     */
    @Transactional
    public ItemResponse<AccountManagementModifyResponse> modifyAccountmanagement(
            AccountManagementModifyRequest parameter) {

        String userId = CommonUtils.getUserId();

        M_DONG mDong = dongRepository.findById(parameter.dongCode())
                .orElseThrow(
                        () -> new ServiceException(ErrorCode.NO_DATA));

        M_USR entity = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorCode.NO_DATA));

        entity.updateUserInformation(parameter.dongCode(), parameter.childrenCount(), mDong);
        userRepository.save(entity);
        
        return ItemResponse.<AccountManagementModifyResponse>builder()
                .status(messageConfig.getCode(NormalCode.MODIFY_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.MODIFY_SUCCESS))
                .item(accountmanagementMapper.toModifyResponse(entity))
                .build();
    }

    /**
     * 계정 정보 조회
     *
     * @return AccountInformationResponse 계정 조회 결과
     * @author BITNA
     * @since 2024-10-28<br />
     */
    @Transactional
    public ItemResponse<AccountInformationSearchResponse> getSearchAccountInformation() {

        String userId = CommonUtils.getUserId();
        M_USR entity = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorCode.NO_DATA));

        return ItemResponse.<AccountInformationSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(accountmanagementMapper.toSearchAccountInformationResponse(entity))
                .build();
    }

}
