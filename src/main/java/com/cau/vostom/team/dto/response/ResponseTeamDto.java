package com.cau.vostom.team.dto.response;

import com.cau.vostom.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseTeamDto {
    private Long teamId;
    private String title;
    private String groupImgUri;
    private String description;
    private int groupMemberCount;
    private Long userId; //그룹장 아이디
    private String userName; //그룹장 이름
    private String userImgUri; //그룹장 프로필 사진

    public static ResponseTeamDto of(Long teamId, String title, String groupImgUri, String description, int groupMemberCount, Long userId, String userName, String userImgUri) {
        return new ResponseTeamDto(teamId, title, groupImgUri, description, groupMemberCount, userId, userName, userImgUri);
    }

}
