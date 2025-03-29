package egovframework.kt.dxp.api.domain.main.record;

import egovframework.kt.dxp.api.domain.citizen.record.CredentialData;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuResponse;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

public record MainSearchResponse(
        //@ApiModelProperty(name = "DID")
        //String did,
        List<CredentialData> credentialData,
        @ApiModelProperty(name = "안 읽은 메세지 존재 여부", example = "N[없음], Y[있음]")
        UseYn pushMessageYn,
        @ApiModelProperty(name = "관심메뉴")
        List<InterestMenuResponse> interestMenuList,
        @ApiModelProperty(name = "베너")
        List<BannerSearchResponse> bannerList
) {

}
