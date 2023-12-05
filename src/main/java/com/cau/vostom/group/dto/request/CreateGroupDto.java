package com.cau.vostom.group.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupDto{
    private String groupName;
    private String groupDescription;
    public static CreateGroupDto of(String groupName, String groupDescription) {
        return new CreateGroupDto(groupName, groupDescription);
    }
}
