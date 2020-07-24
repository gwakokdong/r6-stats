package me.r6_search.config;

import lombok.RequiredArgsConstructor;
import me.r6_search.service.PlayerService;
import me.r6_search.service.UserProfileService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String FRONT_END_IP = "http://localhost:3000";
    private final PlayerService playerService;
    private final UserProfileService userProfileService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PlayerHandlerResolver(playerService));
        resolvers.add(new UserProfileHandlerResolver(userProfileService));
    }
}