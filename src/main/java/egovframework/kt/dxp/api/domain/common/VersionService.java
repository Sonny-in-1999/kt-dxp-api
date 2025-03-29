package egovframework.kt.dxp.api.domain.common;

import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.common.record.VersionRequest;
import egovframework.kt.dxp.api.domain.common.record.VersionResponse;
import egovframework.kt.dxp.api.domain.myPage.VersionRepository;
import egovframework.kt.dxp.api.entity.L_VER;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class VersionService {

    //private static final VersionMapper versionMapper = VersionMapper.INSTANCE;
    private final VersionRepository versionRepository;
    private final MessageConfig messageConfig;


    @Transactional(readOnly = true)
    public ItemResponse<VersionResponse> getLatestVersionInfo(VersionRequest request) {
        L_VER latestVersion = versionRepository
                .findFirstByOperatingSystemTypeOrderByKeyCreateDateDescKeyVersionIdDesc(request.operatingSystemType())
                .orElse(null);

        //NOTE: 조회 했을때 데이터가 없는 경우 신규로 Updqte
        if (latestVersion == null) {
            VersionResponse response = VersionResponse.builder()
                    .latestVersionId(null)
                    .versionId(request.versionId())
                    .isUpdateAvailableYn(UseYn.Y)
                    .build();
            return ItemResponse.<VersionResponse>builder()
                    .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                    .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                    .item(response)
                    .build();
        }

        L_VER currentVersion = versionRepository.findFirstByKeyVersionIdAndOperatingSystemTypeOrderByKeyCreateDateDesc(request.versionId(), request.operatingSystemType())
                .orElse(null);

        //NOTE: 조회 했을때 데이터가 없는 경우 신규로 Updqte
        if (currentVersion == null) {
            VersionResponse response = VersionResponse.builder()
                    .latestVersionId(null)
                    .versionId(request.versionId())
                    .isUpdateAvailableYn(UseYn.Y)
                    .build();
            return ItemResponse.<VersionResponse>builder()
                    .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                    .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                    .item(response)
                    .build();
        }

        VersionResponse response = VersionResponse.builder()
                                                  .latestVersionId(latestVersion.getKey().getVersionId())
                                                  .versionId(currentVersion.getKey().getVersionId())
                                                  .isUpdateAvailableYn(currentVersion.getUpdateVersionYn())
                                                  .build();
        return ItemResponse.<VersionResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(response)
                .build();
    }
}
