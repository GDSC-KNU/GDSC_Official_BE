package com.gdsc_knu.official_homepage.dto.admin.memberStatus;

import com.gdsc_knu.official_homepage.dto.member.TeamInfoResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {
    private Long id;
    private Track track;
    private String name;
    private String studentNumber;
    private String email;
    private String phoneNumber;
    private List<TeamInfoResponse> teams;
    private Role role;

    public static MemberInfoResponse from(Member member) {
        return MemberInfoResponse.builder()
                .id(member.getId())
                .track(member.getTrack())
                .name(member.getName())
                .studentNumber(member.getStudentNumber())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .teams(member.getMemberTeams().stream()
                        .map(MemberTeam::getTeam)
                        .map(team -> TeamInfoResponse.builder()
                                .teamName(team.getTeamName())
                                .teamPageUrl(team.getTeamPageUrl())
                                .build())
                        .toList())
                .role(member.getRole())
                .build();
    }
}
