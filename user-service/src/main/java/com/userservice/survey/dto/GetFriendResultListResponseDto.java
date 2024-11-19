package com.userservice.survey.dto;

import com.userservice.survey.entity.Friend;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetFriendResultListResponseDto {

    private List<FriendListInfo> friendList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class FriendListInfo {
        private Long friendId;
        private String name;
        private String gender;
        private Integer age;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static FriendListInfo fromFriend(Friend friend) {
            FriendListInfo info = new FriendListInfo();
            info.setFriendId(friend.getId());
            info.setName(friend.getName());
            info.setCreatedAt(friend.getCreatedAt());
            info.setUpdatedAt(friend.getUpdatedAt());
            info.setGender(friend.getGender());
            info.setAge(friend.getAge());
            return info;
        }
    }

    public static GetFriendResultListResponseDto createResponse(List<FriendListInfo> friendList, int totalPages, boolean hasNext) {
        GetFriendResultListResponseDto info = new GetFriendResultListResponseDto();
        info.setFriendList(friendList);
        info.setTotalPages(totalPages);
        info.setHasNext(hasNext);
        return info;
    }
}


