package egovframework.kt.dxp.api.domain.vote;

import org.springframework.context.annotation.DependsOn;
import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_VOTE_USR;
import egovframework.kt.dxp.api.entity.key.L_VOTE_USR_KEY;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : VoteRepository
 * Description   : 이력 투표 Repository
 * Creation Date : 2024-10-21
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-21, MinJi Chae, 최초작성
 ******************************************************************************************/
@Repository
@DependsOn("applicationContextHolder")
public interface VoteUserRepository extends JpaDynamicRepository<L_VOTE_USR, L_VOTE_USR_KEY> {

    Integer countByKeyUserId(String userId);

    List<L_VOTE_USR> findByKeyVoteSequenceNumber(Integer voteSequenceNumber);

    List<L_VOTE_USR> findByKeyUserId(String userId);

    List<L_VOTE_USR> findByKeyUserIdAndKeyVoteSequenceNumber(String userId, Integer voteSequenceNumber);

    @Query(nativeQuery = true, value = """
          SELECT COUNT(*) 
          FROM (
          SELECT COUNT(*)  FROM L_VOTE_USR
          WHERE VOTE_SQNO = :voteSequenceNumber
          GROUP BY USR_ID) t1;
            """)
    Integer getVoteCount(@Param("voteSequenceNumber") Integer voteSequenceNumber);
}
