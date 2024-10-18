package com.gamelink.backend.domain.user.service.impl;

import com.gamelink.backend.domain.user.exception.RefreshTokenExpiredException;
import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.domain.user.model.dto.request.RequestCreateUserProfileImage;
import com.gamelink.backend.domain.user.model.dto.request.RequestUpdateUserProfileImage;
import com.gamelink.backend.domain.user.model.dto.response.ResponseSummarizeMatchDataDto;
import com.gamelink.backend.domain.user.model.dto.response.ResponseToken;
import com.gamelink.backend.domain.user.model.dto.response.ResponseUserProfileDto;
import com.gamelink.backend.domain.user.model.entity.ProfileImage;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.ProfileImageRepository;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.domain.user.service.UserService;
import com.gamelink.backend.global.auth.jwt.JwtAuthenticationToken;
import com.gamelink.backend.global.auth.jwt.JwtProvider;
import com.gamelink.backend.global.auth.jwt.repository.TokenRepository;
import com.gamelink.backend.infra.riot.exception.RiotUserNotFoundException;
import com.gamelink.backend.infra.riot.model.dto.response.ResponseSummonerSoloRankDto;
import com.gamelink.backend.infra.riot.model.dto.response.ResponseSummonerTeamRankDto;
import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.riot.repository.RiotUserRepository;
import com.gamelink.backend.infra.riot.service.RiotOpenApiService;
import com.gamelink.backend.infra.s3.model.dto.ImageRequest;
import com.gamelink.backend.infra.s3.model.dto.UploadedImage;
import com.gamelink.backend.infra.s3.service.AWSObjectStorageService;
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
    private final AWSObjectStorageService s3service;

    private final RiotUserRepository riotUserRepository;
    private final RiotOpenApiService openApiService;

    @Override
    public ResponseUserProfileDto getUserProfile(UUID userSubId) {
        RiotUser riotUser = riotUserRepository.findOneByUserSubId(userSubId)
                .orElseThrow(RiotUserNotFoundException::new);

        String summonerIconUrl = openApiService.getProfileUrl(riotUser.getProfileIconId());
        return new ResponseUserProfileDto(
                riotUser,
                summonerIconUrl,
                s3service,
                riotUser.getQueues().stream().filter(RankQueue::isSoloRank).filter(RankQueue::isActive)
                        .findFirst().map(rq -> {
                            String rankImageUrl = openApiService.getRankImageUrl(rq);
                            return new ResponseSummonerSoloRankDto(rq, rankImageUrl);
                        }).orElse(null),
                riotUser.getQueues().stream().filter(RankQueue::isTeamRank).filter(RankQueue::isActive)
                        .findFirst().map(rq -> {
                            String rankImageUrl = openApiService.getRankImageUrl(rq);
                            return new ResponseSummonerTeamRankDto(rq, rankImageUrl);
                        }).orElse(null),
                new ResponseSummarizeMatchDataDto()
        );
    }

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
