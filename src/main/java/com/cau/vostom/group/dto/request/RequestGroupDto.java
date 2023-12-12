package com.cau.vostom.group.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestGroupDto{
    private String groupName;
    private String groupDescription;
    public static RequestGroupDto of(String groupName, String groupDescription) {
        return new RequestGroupDto(groupName, groupDescription);
    }
}
