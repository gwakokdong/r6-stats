package me.r6_search.r6api;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import me.r6_search.exception.r6api.R6BadAuthenticationException;
import me.r6_search.exception.r6api.R6NotFoundPlayerProfileException;
import me.r6_search.r6api.dto.*;
import me.r6_search.r6api.dto.*;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import static me.r6_search.r6api.RequestParam.RequestType;
import static me.r6_search.r6api.RequestParam.RequestType.*;

@RequiredArgsConstructor
@Component
public class UbiApi {
    private static final String RANK_URL_TEMPLATE = "https://public-ubiservices.ubi.com/v1/spaces/%s/sandboxes/%s/r6karma/players?board_id=pvp_ranked&profile_ids=%s&region_id=%s&season_id=%s";
    private static final String PROFILE_URL_TEMPLATE = "https://public-ubiservices.ubi.com/v2/profiles?platformType=%s&nameOnPlatform=%s";
    private static final String GENERAL_URL_TEMPLATE = "https://public-ubiservices.ubi.com/v1/spaces/%s/sandboxes/%s/playerstats2/statistics?populations=%s&statistics=%s";
    public static int currentSeason = 18;

    private final UbiAuthApi ubiAuthApi;
    private final UbiApiParser parser;

    public List<OperatorDto> getOperatorsStat(String platform, String profileId) {
        String operatorsUrl = makeGeneralUrl(OPERATOR, platform, profileId);
        String response = getDataUsingApi(operatorsUrl);
        return parser.parseResponseToOperatorList(response, profileId);
    }

    public GeneralPvpDto getGeneralPvp(String platform, String profileId) {
        String generalPvpUrl = makeGeneralUrl(GENERAL_PVP, platform, profileId);
        String response = getDataUsingApi(generalPvpUrl);
        return parser.parseResponseToGeneralPvpDto(response, profileId);
    }

    public CasualPvpDto getCasualPvp(String platform, String profileId) {
        String casualPvpUrl = makeGeneralUrl(CASUAL_PVP, platform, profileId);
        String response = getDataUsingApi(casualPvpUrl);
        return parser.parseResponseToCasualPvpDto(response, profileId);
    }

    public RankPvpDto getRankPvp(String platform, String profileId) {
        String rankPvpUrl = makeGeneralUrl(RANK_PVP, platform, profileId);

        String response = getDataUsingApi(rankPvpUrl);
        return parser.parseResponseToRankPvpDto(response, profileId);
    }

    public RankStatDto getRankStat(String platform, String profileId, String region, int season) {
        String rankUrl = String.format(RANK_URL_TEMPLATE,
                Platform.platformToSpaceId(platform),
                Platform.platformToPlatformId(platform),
                profileId,
                region,
                season
        );

        String response = getDataUsingApi(rankUrl);
        return parser.parseResponseToRankStatDto(response, profileId);
    }

    public ProfileDto getProfile(String platform, String id) {
        try {
            String profileUrl = String.format(PROFILE_URL_TEMPLATE, platform, id);
            String responseProfile = getDataUsingApi(profileUrl);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseProfile, JsonObject.class);
            JsonArray jsonArray = jsonObject.get("profiles").getAsJsonArray();
            return gson.fromJson(jsonArray.get(0), ProfileDto.class);
        } catch (IndexOutOfBoundsException e) {
            throw new R6NotFoundPlayerProfileException("Not found player id or platform");
        }
    }

    public boolean checkIsExistUserId(String platform, String profileId) {
        String generalPvpUrl = makeGeneralUrl(GENERAL_PVP, platform, profileId);
        String response = getDataUsingApi(generalPvpUrl);
        parser.parseResponseToGeneralPvpDto(response, profileId);
        return true;
    }

    private String makeGeneralUrl(RequestType requestType, String platform, String userId) {
        String requestParam = String.join(",", RequestParam.map.get(requestType));
        return String.format(GENERAL_URL_TEMPLATE,
                Platform.platformToSpaceId(platform),
                Platform.platformToPlatformId(platform),
                userId,
                requestParam
        );
    }

    private String getDataUsingApi(String requestUrl) {
        AuthToken token = ubiAuthApi.getAuthToken();
        try {
            URL url = new URL(requestUrl);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Ubi-SessionId", token.getSessionId());
            conn.setRequestProperty("Authorization", "Ubi_v1 t=" + token.getTicket());
            conn.setRequestProperty("Ubi-AppId", UbiAuthApi.UPP_APP_ID);

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_ACCEPTED) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                return br.readLine();
            } else if(responseCode == HttpsURLConnection.HTTP_UNAUTHORIZED || responseCode == HttpsURLConnection.HTTP_FORBIDDEN){
                throw new R6BadAuthenticationException("Check Ubi Auth token");
            } else {
                throw new R6BadAuthenticationException("Something wrong ubi api server .. ?");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
