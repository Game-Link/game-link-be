package com.gamelink.backend.domain.user.model.entity;

import com.gamelink.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProfileImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String imageId;

    private String mimeType;

    private String imageName;

    @Builder
    private ProfileImage(String imageId, String mimeType, String imageName) {
        this.imageId = imageId;
        this.mimeType = mimeType;
        this.imageName = imageName;
    }

    public void changeUser(User user) {
        if (this.user != null) {
            this.user.getProfileImages().remove(this);
        }

        this.user = user;
        this.user.getProfileImages().add(this);
    }
}
