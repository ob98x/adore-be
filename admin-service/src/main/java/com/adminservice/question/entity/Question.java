package com.adminservice.question.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question")
public class Question extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member applicant;

    @OneToOne
    @JoinColumn(name="processor_id")
    private Member processor;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_state", nullable = false)
    private QuestionState state;

    @Column(name = "answer_content")
    private String answerContent;

}