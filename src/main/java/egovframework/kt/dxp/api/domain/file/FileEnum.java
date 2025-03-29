package egovframework.kt.dxp.api.domain.file;

import lombok.Getter;

public class FileEnum {

    @Getter
    public enum FileDivision {
        C,    // 일반 파일, Common
        MI   // 메인 이미지, MAIN IMAGE
    }
}
