package egovframework.kt.dxp.api.domain.file;

import egovframework.kt.dxp.api.entity.L_FILE;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * File Repository
 *
 * @author CHAEMINJI
 * @apiNote 2024-10-30 BITNA 쿼리메소드 추가
 * @since 2024-10-25<br />
 */

@Repository
@DependsOn("applicationContextHolder")
public interface FileRepository extends JpaRepository<L_FILE, Integer> {

    @Query(nativeQuery = true, value = """
            SELECT COALESCE(MAX(FILE_SQNO), 1)
              FROM L_FILE
            """)
    Integer getMaxFileSequenceNumber();

    List<L_FILE> findByBulletinBoardDivisionAndBulletinBoardSequenceNumber(String bulletinBoardDivision, Integer bulletinBoardSequenceNumber);
    List<L_FILE> findByBulletinBoardDivisionAndBulletinBoardSequenceNumberAndFileDivision(String bulletinBoardDivision, Integer bulletinBoardSequenceNumber, String fileDivision);

    List<L_FILE> findByBulletinBoardDivisionAndSaveFileName(String bulletinBoardDivision, String saveFileName);

    List<L_FILE> findByBulletinBoardDivisionAndBulletinBoardSequenceNumberIn(String bulletinBoardDivision, List<Integer> bulletinBoardSequenceNumber);
    List<L_FILE> findByBulletinBoardDivisionAndBulletinBoardSequenceNumberInAndFileDivision(String bulletinBoardDivision, List<Integer> bulletinBoardSequenceNumber, String fileDivision);
}
