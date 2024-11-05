package com.adminservice.notification.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
public class Notification extends BaseEntity {

    @Column(name="title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

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
