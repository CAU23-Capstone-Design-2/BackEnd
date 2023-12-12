package com.cau.vostom.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseGroupDto {
    private Long groupId;
    private String groupName;
    private String groupImgUri;
    private String groupDescription;
    private int groupMemberCount;
    private Long leaderId; //그룹장 아이디
    private String leaderName; //그룹장 이름
    private String leaderImgUri; //그룹장 프로필 사진
    private boolean isLeader;
    private boolean isMember;

    public static ResponseGroupDto of(Long groupId, String groupName, String groupImgUri, String groupDescription, int groupMemberCount, 
                                    Long leaderId, String leaderName, String leaderImgUri, boolean isLeader, boolean isMember) {
        return new ResponseGroupDto(groupId, groupName, groupImgUri, groupDescription, groupMemberCount, leaderId, leaderName, leaderImgUri, isLeader, isMember);
    }

}
