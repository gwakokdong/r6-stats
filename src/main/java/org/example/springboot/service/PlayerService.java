package org.example.springboot.service;

import lombok.RequiredArgsConstructor;
import org.example.springboot.domain.player.Player;
import org.example.springboot.domain.player.PlayerRepository;
import org.example.springboot.r6api.UbiApi;
import org.example.springboot.r6api.dto.GeneralPvpDto;
import org.example.springboot.r6api.dto.ProfileDto;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final UbiApi ubiApi;

    public Player findPlayerIfNotExistReturnNewEntity(String platform, String id) {
        Player player = playerRepository.findByPlatformAndAndUserId(platform, id);
        if(player == null) {
            // 게임서버에서 띄어쓰기를 + 로 구분. 저장할때는 원래 아이디지만, api에 요청할때는 +로 바꿔서 요청
            String removeSpaceId = id.replaceAll(" ", "+");
            ProfileDto profileDto = ubiApi.getProfile(platform, removeSpaceId);
            // 고유값이 profileId 로 찾는다 - 유저 아이디가 변경될 수도 있기 때문
            Player playerUsingProfileId = playerRepository.findByPlatformAndProfileId(platform, profileDto.getProfileId());
            if(playerUsingProfileId == null) {
                ubiApi.checkIsExistUserId(platform, profileDto.getProfileId());
                // 없을 시 자동으로 throw exception
                player = Player.builder()
                        .profileId(profileDto.getProfileId())
                        .platform(platform)
                        .userId(id)
                        .build();

                playerRepository.save(player);
            } else {
                playerUsingProfileId.updateUserId(id);
            }
        }

        return player;
    }
}
