package egovframework.kt.dxp.api.domain.dong;

import java.util.List;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.request.DynamicRequest;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.dong.record.DongSearchResponse;
import egovframework.kt.dxp.api.entity.M_DONG;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@DependsOn("applicationContextHolder")
@RequiredArgsConstructor
public class DongService {

    private final DongRepository dongRepository;
    private static final DongMapper dongMapper = DongMapper.INSTANCE;
    private final MessageConfig messageConfig;

    @Transactional
    public ItemsResponse<DongSearchResponse> getSearchDongList(DynamicRequest parameter) {

        Page<M_DONG> page = dongRepository.findDynamicWithPageable(parameter);
        if (CollectionUtils.isEmpty(page.getContent())) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }
        List<DongSearchResponse> list = dongMapper.toSearchResponseList(page.getContent());

        return ItemsResponse.<DongSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(list)
                .build();
    }

}
