package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.key.L_WLFR_USR_KEY;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class L_WLFR_USR {

    /* 키 */
    @EmbeddedId
    private L_WLFR_USR_KEY key;

    @Builder()
    public L_WLFR_USR(Integer welfareSequenceNumber, String userId) {
        L_WLFR_USR_KEY key = L_WLFR_USR_KEY.builder()
                .welfareSequenceNumber(welfareSequenceNumber)
                .userId(userId)
                .build();
        this.key = key;
    }
}
