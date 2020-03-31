package io.javabrains.moviecatalogservice.resources;

import com.netflix.discovery.DiscoveryClient;
import io.javabrains.moviecatalogservice.model.CatalogItem;
import io.javabrains.moviecatalogservice.model.Movie;
import io.javabrains.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;

    private final WebClient.Builder webClientBuilder;

    // for custom control, if needed
    private final DiscoveryClient discoveryClient;

    @Autowired
    public MovieCatalogResource(RestTemplate restTemplate,
                                WebClient.Builder webClientBuilder,
                                DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        // get all rated movie IDs
        UserRating ratings = webClientBuilder.build()
                .get()
                .uri("http://rating-data-service/ratings/users/" + userId)
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();

        return ratings.getUserRatings().stream()
                .map(rating -> {
                    // for each movie ID call movie info service and get details
                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
                    // put them together
                    return new CatalogItem(movie.getName(), "Desc", rating.getRating());
                })
                .collect(Collectors.toList());
    }
}
