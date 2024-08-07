package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.dto.admin.team.AdminMemberResponse;
import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    Optional<MemberTeam> findByMemberIdAndTeamId(Long memberId, Long teamId);

    @Query("SELECT new com.gdsc_knu.official_homepage.dto.admin.team.AdminMemberResponse(m.id, m.name, m.email, m.profileUrl) " +
            "FROM MemberTeam mt " +
            "JOIN mt.member m " +
            "WHERE mt.team.id = :teamId")
    List<AdminMemberResponse> findAllAdminMemberResponsesByTeamId(@Param("teamId") Long teamId);

}
