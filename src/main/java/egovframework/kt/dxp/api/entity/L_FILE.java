package egovframework.kt.dxp.api.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class L_FILE implements Serializable {

    /* 파일 순번 */
    @Id
    @Column(name = "FILE_SQNO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileSequenceNumber;

    /* 실제 파일 명 */
    @Column(name = "ACTL_FILE_NM")
    private String actualFileName;

    /* 저장 파일 명 */
    @Column(name = "SAVE_FILE_NM")
    private String saveFileName;

    /* 파일 경로 */
    @Column(name = "FILE_PATH")
    private String filePath;

    /* 썸네일 파일 경로 */
    @Column(name = "THUMB_FILE_PATH")
    private String thumbnailFilePath;

    /* 파일 확장자 */
    @Column(name = "FILE_EXTS")
    private String fileExtension;

    /* 파일 크기 */
    @Column(name = "FILE_SIZE")
    private Long fileSize;

    /* 게시판 구분 */
    @Column(name = "BBS_DIV")
    private String bulletinBoardDivision;

    /* 게시판 순번 */
    @Column(name = "BBS_SQNO")
    private Integer bulletinBoardSequenceNumber;

    /* 파일 구분 */
    @Column(name = "FILE_DIV")
    private String fileDivision;

    /* 하위 게시판 순번 */
    @Column(name = "SUB_BBS_SQNO")
    private Integer subBulletinBoardSequenceNumber;

    /* 생성 일시 */
    @Column(name = "CRT_DT")
    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BBS_SQNO", referencedColumnName = "NOTICE_SQNO", insertable = false, updatable = false)
    private M_NOTICE notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BBS_SQNO", referencedColumnName = "WLFR_SQNO", insertable = false, updatable = false)
    private L_WLFR welfare;

    @Builder
    public L_FILE(String actualFileName, String saveFileName, String filePath, String thumbnailFilePath, String fileExtension, Long fileSize, String bulletinBoardDivision, Integer bulletinBoardSequenceNumber, String fileDivision, Integer subBulletinBoardSequenceNumber) {
        this.actualFileName = actualFileName;
        this.saveFileName = saveFileName;
        this.filePath = filePath;
        this.thumbnailFilePath = thumbnailFilePath;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.bulletinBoardDivision = bulletinBoardDivision;
        this.bulletinBoardSequenceNumber = bulletinBoardSequenceNumber;
        this.fileDivision = fileDivision;
        this.subBulletinBoardSequenceNumber = subBulletinBoardSequenceNumber;
    }
}