package com.gamelink.backend.domain.user.service.impl;

import com.gamelink.backend.domain.user.exception.RefreshTokenExpiredException;
import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.domain.user.model.dto.request.RequestCreateUserProfileImage;
import com.gamelink.backend.domain.user.model.dto.request.RequestUpdateUserProfileImage;
import com.gamelink.backend.domain.user.model.dto.response.ResponseToken;
import com.gamelink.backend.domain.user.model.entity.ProfileImage;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.ProfileImageRepository;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.domain.user.service.UserService;
import com.gamelink.backend.global.auth.jwt.JwtAuthenticationToken;
import com.gamelink.backend.global.auth.jwt.JwtProvider;
import com.gamelink.backend.global.auth.jwt.repository.TokenRepository;
import com.gamelink.backend.infra.s3.model.dto.ImageRequest;
import com.gamelink.backend.infra.s3.model.dto.UploadedImage;
import com.gamelink.backend.infra.s3.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    private final ProfileImageRepository profileImageRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public ResponseToken reissue(String refreshToken) {
        if(!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new RefreshTokenExpiredException();
        }
        String accessToken = tokenRepository.findAccessToken(refreshToken);
        JwtAuthenticationToken newToken = jwtProvider.reissue(accessToken);
        return new ResponseToken(newToken);
    }

    @Override
    @Transactional
    public void createUserProfileImage(RequestCreateUserProfileImage request, UUID userSubId) {
        User user = userRepository.findOneBySubId(userSubId)
                .orElseThrow(UserNotFoundException::new);

        List<UploadedImage> images = imageUploadService.uploadedImages(
                ImageRequest.ofList(List.of(request.getProfileImage()))
        );

        List<ProfileImage> profileImages = new ArrayList<>();
        for (UploadedImage image : images) {
            ProfileImage.ProfileImageBuilder builder = ProfileImage.builder()
                    .imageName(image.getOriginalImageName())
                    .mimeType(image.getMimeType().toString())
                    .imageId(image.getImageId());

            profileImages.add(builder.build());
        }
        for (ProfileImage image : profileImages) {
            image.changeUser(user);
        }
        profileImageRepository.saveAll(profileImages);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserProfileImage(RequestUpdateUserProfileImage request, UUID userSubId) {
        User user = userRepository.findOneBySubId(userSubId)
                .orElseThrow(UserNotFoundException::new);
        user.getProfileImages().clear();

        List<UploadedImage> images = imageUploadService.uploadedImages(
                ImageRequest.ofList(List.of(request.getProfileImage()))
        );

        List<ProfileImage> profileImages = new ArrayList<>();
        for (UploadedImage image : images) {
            ProfileImage.ProfileImageBuilder builder = ProfileImage.builder()
                    .imageName(image.getOriginalImageName())
                    .mimeType(image.getMimeType().toString())
                    .imageId(image.getImageId());

            profileImages.add(builder.build());
        }
        for (ProfileImage image : profileImages) {
            image.changeUser(user);
        }
        profileImageRepository.saveAll(profileImages);
        userRepository.save(user);
    }
}
