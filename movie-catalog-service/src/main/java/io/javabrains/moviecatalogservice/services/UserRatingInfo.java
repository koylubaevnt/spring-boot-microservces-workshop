package io.javabrains.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserRatingInfo {

    private final WebClient.Builder webClientBuilder;

    // for custom control, if needed
    //private final DiscoveryClient discoveryClient;

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    public UserRating getUserRating(@PathVariable("userId") String userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://rating-data-service/ratings/users/" + userId)
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();
    }

    private UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Collections.singletonList(
                new Rating("0", 0)
        ));
        return userRating;
    }

}
