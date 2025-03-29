package egovframework.kt.dxp.api.domain.notice;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.file.nomal.NormalFileDownloader;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.file.FileRepository;
import egovframework.kt.dxp.api.domain.notice.model.NoticeListImpl;
import egovframework.kt.dxp.api.domain.notice.record.*;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.L_NOTICE_USR;
import egovframework.kt.dxp.api.entity.M_NOTICE;
import egovframework.kt.dxp.api.entity.key.L_NOTICE_USR_KEY;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 공지사항 Service
 *
 * @author MINJI
 * @apiNote 목록조회 totalSize 반환
 * @since 2024-10-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class NoticeService {

    private static final Integer LIST_DISPLAY_PERIOD = 365;
    private static final String NOTICE_BBS_DIV = "01";

    private final NoticeRepository noticeRepository;
    private static final NoticeMapper noticeMapper = NoticeMapper.INSTANCE;
    private final MessageConfig messageConfig;

    private final NoticeUserRepository noticeUserRepository;
    private static final NoticeUserMapper noticeUserMapper = NoticeUserMapper.INSTANCE;

    private final FileRepository fileRepository;

    /**
     * 공지사항 목록 조회
     *
     * @param parameter 공지사항 조회 조건
     * @return NoticeSearchResponse 공지사항 조회 결과
     * @author MINJI
     * @since 2024-10-15<br />
     */
    @Transactional
    public GridResponse<NoticeSearchResponse> getSearchNoticeList(NoticeSearchRequest parameter) {

        // 페이지 번호가 음수이거나 사이즈가 음수인 경우 예외 처리
        if (parameter.pageNo() < 0 || parameter.pageSize() <= 0) {
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        Page<NoticeListImpl> page = this.getMyNotice(parameter.pageNo(), parameter.pageSize());

        // Page에서 List로 변환
        List<NoticeSearchResponse> list = noticeMapper.toSearchResponseList(page.getContent());
        return GridResponse.<NoticeSearchResponse>builder()
                           .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                           .totalSize(page.getTotalElements())
                           .totalPageSize(page.getTotalPages())
                           .size(page.getNumberOfElements())
                           .items(list)
                           .build();
    }

    private Page<NoticeListImpl> getMyNotice(int page, int size) {
         String userId = CommonUtils.getUserId();
        // 1년 정책 미정 -> 확정되면 변경 필요
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(LIST_DISPLAY_PERIOD);
        Pageable pageable = PageRequest.of(page, size);
        return noticeRepository.findNoticeListBeforeDate(userId, daysAgo, pageable);
        //return noticeRepository.findNoticeListBeforeDate(userId, daysAgo, pageable);
    }

    /**
     * 공지사항 상세 조회
     *
     * @param parameter 공지사항 상세 조회 조건
     * @return NoticeSearchResponse 공지사항 상세 조회 결과
     * @author MINJI
     * @since 2024-10-15<br />
     */
    @Transactional
    public ItemResponse<NoticeDetailSearchResponse> getDetailSearchNotice(NoticeDetailSearchRequest parameter) {
        M_NOTICE entity = noticeRepository.findById(parameter.noticeSequenceNumber())
                .orElseThrow(
                        () -> new ServiceException(ErrorCode.NO_DATA));
        L_NOTICE_USR_KEY key = new L_NOTICE_USR_KEY(CommonUtils.getUserId(), parameter.noticeSequenceNumber());
        L_NOTICE_USR lNoticeUsr = noticeUserRepository.findById(key).orElse(null);
        if (lNoticeUsr == null) {
            NoticeDetailCreateRequest noticeDetailCreateRequest = new NoticeDetailCreateRequest(
                    CommonUtils.getUserId(),
                    parameter.noticeSequenceNumber());
            L_NOTICE_USR createEntity = noticeUserMapper.toEntity(noticeDetailCreateRequest);
            noticeUserRepository.saveAndFlush(createEntity);
        }

        List<L_FILE> lFileList = fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumber(NOTICE_BBS_DIV, parameter.noticeSequenceNumber());

        if (lFileList.isEmpty()) {
            lFileList = null;
        }

        return ItemResponse.<NoticeDetailSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(noticeMapper.toDetailSearchResponse(entity, lFileList))
                .build();
    }

    @Transactional
    public void getDownloadFile(FileDownloadRequest parameter, HttpServletResponse httpServletResponse) {

        if (parameter.fileSequenceNumber() == null) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }
        L_FILE entity = fileRepository.findById(parameter.fileSequenceNumber())
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

        NormalFileDownloader fileDownloader = new NormalFileDownloader(httpServletResponse, entity.getActualFileName(),
                entity.getSaveFileName(), entity.getFilePath());
        fileDownloader.download();
    }

}
