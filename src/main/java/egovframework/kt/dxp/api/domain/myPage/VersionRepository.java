package egovframework.kt.dxp.api.domain.myPage;

import org.springframework.context.annotation.DependsOn;
import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_VER;
import egovframework.kt.dxp.api.entity.key.L_VER_KEY;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : MypageRepository
 * Description   : 이력 버전 Repository
 * Creation Date : 2024-10-21
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-21, MinJi Chae, 최초작성
 ******************************************************************************************/
@Repository
@DependsOn("applicationContextHolder")
public interface VersionRepository extends JpaDynamicRepository<L_VER, L_VER_KEY> {

    L_VER findFirstByOrderByKeyCreateDateDesc();

    Optional<L_VER> findFirstByOperatingSystemTypeOrderByKeyCreateDateDescKeyVersionIdDesc(String osType);
    Optional<L_VER> findFirstByKeyVersionIdAndOperatingSystemTypeOrderByKeyCreateDateDesc(String versionId, String osType);
}

