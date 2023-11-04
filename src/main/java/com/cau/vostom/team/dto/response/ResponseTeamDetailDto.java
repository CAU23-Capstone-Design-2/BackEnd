package com.cau.vostom.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResponseTeamDetailDto {
    private String groupName;
    private String groupImage;
    private String groupInfo;
    private int groupMemberCount;
    private List<ResponseTeamMusicDto> teamMusicList;

    public static ResponseTeamDetailDto of(String groupName, String groupImage, String groupInfo, int groupMemberCount, List<ResponseTeamMusicDto> teamMusicList) {
        return new ResponseTeamDetailDto(groupName, groupImage, groupInfo, groupMemberCount, teamMusicList);
    }

}
