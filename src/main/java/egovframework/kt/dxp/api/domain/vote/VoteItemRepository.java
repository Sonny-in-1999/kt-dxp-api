package egovframework.kt.dxp.api.domain.vote;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_VOTE_ITEM;
import egovframework.kt.dxp.api.entity.L_VOTE_USR;
import egovframework.kt.dxp.api.entity.key.L_VOTE_ITEM_KEY;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : VoteItemRepository
 * Description   : 이력 투표 Repository
 * Creation Date : 2024-10-21
 * Written by    : BITNA Researcher
 * History       : 1 - 2024-11-01, BITNA, 최초작성
 ******************************************************************************************/
@Repository
@DependsOn("applicationContextHolder")
public interface VoteItemRepository extends JpaDynamicRepository<L_VOTE_ITEM, L_VOTE_ITEM_KEY> {

    List<L_VOTE_ITEM> findByKeyVoteSequenceNumber(Integer voteSequenceNumber);
}
