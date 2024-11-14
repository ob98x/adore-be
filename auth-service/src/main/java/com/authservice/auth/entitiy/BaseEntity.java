package com.authservice.auth.entitiy;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id()
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    @Schema(description = "아이디", example = "1")
    private Long id;

    @Column(name = "created_at")
    @Schema(description = "생성일", example = "2021-08-01T00:00:00")
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Schema(description = "수정일", example = "2021-08-01T00:00:00")
    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

}