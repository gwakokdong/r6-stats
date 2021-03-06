package me.r6_search.web.dto.r6api;

import lombok.Data;

@Data
public class RankStatRegionResponseDto {
    String region;
    RankStatResponseDto rankStat;

    public RankStatRegionResponseDto(String region, RankStatResponseDto rankStat) {
        this.region = region;
        this.rankStat = rankStat;
    }
}
