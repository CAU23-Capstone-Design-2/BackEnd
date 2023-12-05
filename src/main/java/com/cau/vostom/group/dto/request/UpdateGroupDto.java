package com.cau.vostom.group.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGroupDto {
    private String groupName;
    private String groupDescription;
    private MultipartFile groupImage;

    public static UpdateGroupDto of(String groupName, String groupDescription, MultipartFile groupImage) {
        return new UpdateGroupDto(groupName, groupDescription, groupImage);
    }
}
