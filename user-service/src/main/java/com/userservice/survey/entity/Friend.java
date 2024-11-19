package com.userservice.survey.entity;

import com.userservice.global.BaseEntity;
import com.userservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "fr_name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private int age;

    @Column(name = "prefer_notes")
    @Convert(converter = StringListConverter.class)
    private List<String> preferNotes;

    @Column(name = "fr_character")
    private String character;

    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private FriendState state;

    @Builder
    public Friend(Member member, String name, String gender, int age, List<String> preferNotes, String character, int price, FriendState state) {
        this.member = member;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.preferNotes = preferNotes;
        this.character = character;
        this.price = price;
        this.state = state;
    }

    public static Friend of(Member member, String name, String gender, int age, List<String> preferNotes, String character, int price, FriendState state) {
        return Friend.builder()
                .member(member)
                .name(name)
                .gender(gender)
                .age(age)
                .preferNotes(preferNotes)
                .character(character)
                .price(price)
                .state(state)
                .build();
    }
}
