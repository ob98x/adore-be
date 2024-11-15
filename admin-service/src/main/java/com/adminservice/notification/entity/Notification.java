package com.adminservice.notification.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
@Schema(name = "Notification", description = "공지사항")
public class Notification extends BaseEntity {

    @Schema(description = "제목", example = "공지사항입니다.")
    @Column(name="title", nullable = false)
    private String title;

    @Schema(description = "내용", example = "공지사항입니다.")
    @Column(name = "content", nullable = false)
    private String content;

    @Schema(description = "작성자 아이디", example = "1")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Schema(description = "상태", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private NotificationState state;

    @Builder
    public Notification (String title, String content, Member member, NotificationState state) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.state = state;
    }

    public static Notification of(String title, String content, Member member, NotificationState state) {
        return Notification.builder()
                .title(title)
                .content(content)
                .member(member)
                .state(state)
                .build();
    }
}
