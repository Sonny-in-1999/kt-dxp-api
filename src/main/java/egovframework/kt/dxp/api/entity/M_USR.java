package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.base.BaseEntity;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity()
public class M_USR extends BaseEntity implements Persistable<String> {

    public static final String UNKNOWN_NAME = "(알 수 없음)";

    /* 사용자 아이디              */
    @Id
    @Size(max = 36)
    @Column(name = "USR_ID", nullable = false, length = 36)
    private String userId;

    /* 사용자 명                  */
    @Size(max = 50)
    @NotNull
    @Column(name = "USR_NM", nullable = false, length = 50)
    private String userName;

    /* 비밀번호                   */
    @Size(max = 512)
    @NotNull
    @Column(name = "PSWD", nullable = false, length = 512)
    private String password;

    /* 휴대폰 번호                */
    @Size(max = 30)
    @Column(name = "HP_NO", length = 30)
    private String mobilePhoneNumber;

    /* 성별 유형                  */
    @Size(max = 3)
    @Column(name = "GNDR_TYPE", length = 3)
    private String genderType;

    /* 생년월일                   */
    @Size(max = 8)
    @NotNull
    @Column(name = "BRDT", nullable = false, length = 8)
    private String birthDate;

    /* 동 코드                    */
    @Size(max = 5)
    @NotNull
    @Column(name = "DONG_CD", nullable = false, length = 5)
    private String dongCode;

    /* 자녀 수                    */
    @NotNull
    @Column(name = "CHILDREN_CNT", nullable = false)
    private Integer childrenCount;

    /* 인증 아이디                */
    @Size(max = 512)
    @NotNull
    @Column(name = "CERT_ID", nullable = false, length = 512)
    private String certificationId;

    /* 운영체계 유형              */
    @Size(max = 3)
    @NotNull
    @Column(name = "OS_TYPE", nullable = false, length = 3)
    private String operatingSystemType;

    /* 푸시 키                    */
    @Size(max = 512)
    @Column(name = "PUSH_KEY", length = 512)
    private String pushKey;

    /* 최근 로그인 일시           */
    @Setter
    @Column(name = "RCT_LGN_DT")
    private LocalDateTime recentLoginDate;

    /* 어세스 토큰                */
    @Setter
    @Size(max = 512)
    @Column(name = "AC_TKN", length = 512)
    private String accessToken;

    /* 리프레시 토큰              */
    @Setter
    @Size(max = 512)
    @Column(name = "RF_TKN", length = 512)
    private String refreshToken;

    ///* 거래코드              */
    //@Setter
    //@Size(max = 50)
    //@Column(name = "TRXCODE", length = 50)
    //private String trxCode;

    @Setter
    @OneToOne(mappedBy = "mUsr", fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "USR_ID", nullable = false, updatable = false)
    private C_ALARM cAlarm;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "SCSS_YN")
    private UseYn secessionYn;

    public void addAlram(C_ALARM cAlram) {
        this.cAlarm = cAlram;
        this.setCAlarm(cAlram);
    }

    @Setter
    @OneToMany(mappedBy = "mUsr", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<C_USR_TRMS_AGREE> cUsrTermsAgrees = new ArrayList<>();

    public void addTermsAgree(List<C_USR_TRMS_AGREE> cUsrTermsAgrees) {
        this.setCUsrTermsAgrees(cUsrTermsAgrees);
    }

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DONG_CD", referencedColumnName = "DONG_CD", insertable = false, updatable = false)
    private M_DONG dong;

    @Setter
    @OneToOne(mappedBy = "mUsr", fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "USR_ID", nullable = false, updatable = false)
    private M_USR_PRNT mUsrPrnt;

    public void addUsrPrnt(M_USR_PRNT mUsrPrnt) {
        this.mUsrPrnt = mUsrPrnt;
        this.setMUsrPrnt(mUsrPrnt);
    }

    @Builder(toBuilder = true)
    public M_USR(String userId, String userName, String password, String mobilePhoneNumber,
            String genderType, String birthDate, String dongCode, Integer childrenCount,
            String certificationId, String operatingSystemType, String pushKey,
            LocalDateTime recentLoginDate, String accessToken, String refreshToken, C_ALARM cAlarm,
            List<C_USR_TRMS_AGREE> cUsrTermsAgrees) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.genderType = genderType;
        this.birthDate = birthDate;
        this.dongCode = dongCode;
        this.childrenCount = childrenCount;
        this.certificationId = certificationId;
        this.operatingSystemType = operatingSystemType;
        this.pushKey = pushKey;
        this.recentLoginDate = recentLoginDate;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.cAlarm = cAlarm;
        this.cUsrTermsAgrees = cUsrTermsAgrees;
        this.secessionYn = UseYn.N;
    }

    /* 계정 정보 수정 */
    public void updateUserInformation(String dongCode, Integer childrenCount, M_DONG dong){
        this.dongCode = dongCode;
        this.childrenCount = childrenCount;
        this.dong = dong;
    }

    /* 패스워드 정보 수정 */
    public void updatePasswordInformation(String password){
        this.password = password;
    }

    /* 모바일 정보 수정 */
    public void updateLoginOSInformation(String mobilePhoneNumber, String operatingSystemType, String pushKey){
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.operatingSystemType = operatingSystemType;
        this.pushKey = pushKey;
    }

    /* 토큰 정보 수정 */
    public void updateUserToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    /* 계정 정보 삭제 */
    public void deleteUserInformation() {
        this.userName = UNKNOWN_NAME;
        this.password = "";
        this.mobilePhoneNumber = "";
        this.certificationId = "";
        this.pushKey = "";
        this.accessToken = "";
        this.refreshToken = "";
        this.secessionYn = UseYn.Y;
    }

    /* 로그아웃 */
    public void updateLogoutInformation() {
        this.password = "";
        this.operatingSystemType = "";
        this.pushKey = "";
        this.accessToken = "";
        this.refreshToken = "";
    }

    @Override
    public String getId() {
        return this.getUserId();
    }

    @Override
    public boolean isNew() {
        return this.accessToken == null;
    }
}
