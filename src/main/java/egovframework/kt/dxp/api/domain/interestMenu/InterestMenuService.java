package egovframework.kt.dxp.api.domain.interestMenu;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuCreateRequest;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuDeleteRequest;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuResponse;
import egovframework.kt.dxp.api.domain.interestMenu.record.MenuRequest;
import egovframework.kt.dxp.api.domain.interestMenu.record.MenuSearchResponse;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.entity.M_MENU;
import egovframework.kt.dxp.api.entity.M_USR_MENU;
import egovframework.kt.dxp.api.entity.M_USR;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.C_USR_TRMS_AGREE_KEY;
import egovframework.kt.dxp.api.entity.key.M_USER_MENU_KEY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.context.annotation.DependsOn;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class InterestMenuService {

    private final UserRepository userRepository;
    private final InterestMenuRepository interestmenuRepository;
    private final MenuRepository menuRepository;
    private static final InterestMenuMapper interestmenuMapper = InterestMenuMapper.INSTANCE;
    private final MessageConfig messageConfig;

    private final static String MENU_DIV_APP = "APP";

    /**
     * 메뉴 정보 조회
     *
     * @return MenuSearchResponse 메뉴 조회 결과
     * @author MinJi Chae
     * @since 2024-10-22<br />
     */
    @Transactional
    public ItemsResponse<MenuSearchResponse> getSearchMenuList() {

        List<M_MENU> list = menuRepository.findByUseYnAndMenuDivisionOrderBySortSequenceNumberAsc(UseYn.Y,
                MENU_DIV_APP);
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }
        return ItemsResponse.<MenuSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(interestmenuMapper.toMenuSearchResponseList(list))
                .build();
    }

    /**
     * 관심메뉴 정보 조회
     *
     * @return InterestMenuResponse 관심메뉴 조회 결과
     * @author MinJi Chae
     * @since 2024-10-22<br />
     */
    @Transactional
    public ItemsResponse<InterestMenuResponse> getSearchInterestMenuList() {

        String userId = CommonUtils.getUserId();
        List<M_USR_MENU> list = interestmenuRepository.findByKeyUserIdAndMenuUseYnIsOrderBySortSequenceNumberAsc(userId,
                UseYn.Y);
        // if (CollectionUtils.isEmpty(list)) {
        // throw new ServiceException(ErrorCode.NO_DATA);
        // }

        return ItemsResponse.<InterestMenuResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(interestmenuMapper.toSearchResponseList(list))
                .build();
    }

    /**
     * 관심메뉴 추가
     *
     * @return 관심메뉴 추가 성공 건수
     * @author MinJi Chae
     * @since 2024-10-22<br />
     */

    @Transactional
    public ItemResponse<Integer> createInterestMenu(
            InterestMenuCreateRequest parameter) {

        String userId = CommonUtils.getUserId();
        M_USR userEntity = userRepository.findById(userId).orElseThrow(
                () ->  new ServiceException(ErrorCode.NO_DATA));

        List<M_USR_MENU> mUsrMenus = new ArrayList<>();
        for(MenuRequest data : parameter.menuList()) {
            M_MENU mMenu = menuRepository.findById(data.menuId()).orElseThrow(
                    () -> new ServiceException(ErrorCode.NO_DATA));

            M_USER_MENU_KEY mUserMenuKey = M_USER_MENU_KEY.builder()
                    .menuId(data.menuId())
                    .userId(userId)
                    .build();

            M_USR_MENU entity = M_USR_MENU.builder()
                    .key(mUserMenuKey)
                    .sortSequenceNumber(data.sortSequenceNumber())
                    .menu(mMenu)
                    .user(userEntity)
                    .build();

            mUsrMenus.add(entity);
        }
        interestmenuRepository.saveAll(mUsrMenus);

        return ItemResponse.<Integer>builder()
                .status(messageConfig.getCode(NormalCode.CREATE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.CREATE_SUCCESS))
                .item(1)
                .build();
    }

    /**
     * 관심메뉴 삭제
     *
     * @return 관심메뉴 삭제 건수
     * @author MinJi Chae
     * @since 2024-10-23<br />
     */
    @Transactional
    public ItemResponse<Integer> deleteInterestMenu(
            InterestMenuDeleteRequest parameter) {

        String userId = CommonUtils.getUserId();

        parameter.menuList().forEach(data -> {
            M_USER_MENU_KEY id = new M_USER_MENU_KEY(userId, data.menuId());
            M_USR_MENU entity = interestmenuRepository.findById(id).orElseThrow(
                    () -> new ServiceException(ErrorCode.NO_DATA));
            interestmenuRepository.delete(entity);
            List<M_USR_MENU> list = new ArrayList<>();
            if (!entity.getSortSequenceNumber().equals(data.sortSequenceNumber())) {
                list = interestmenuRepository.findByKeyUserIdAndMenuUseYnIsOrderBySortSequenceNumberAsc(userId,
                        UseYn.Y);
            }
            newSequenceNumber(list);
        });

        return ItemResponse.<Integer>builder()
                .status(messageConfig.getCode(NormalCode.DELETE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.DELETE_SUCCESS))
                .item(1)
                .build();
    }

    public void newSequenceNumber(List<M_USR_MENU> list) {
        List<M_USR_MENU> newList = new ArrayList<>();

        // sequence 조정
        list.forEach(menu -> {
            long index = list.indexOf(menu);
            menu.setSortSequenceNumber((int) (index + 1));
            newList.add(menu);
        });
        interestmenuRepository.saveAllAndFlush(newList);
    }

}
