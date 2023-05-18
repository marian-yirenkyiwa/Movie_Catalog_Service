package io.movieservice.moviecatalogservice.resources;

import io.movieservice.moviecatalogservice.models.CatalogItem;
import io.movieservice.moviecatalogservice.models.Movie;
import io.movieservice.moviecatalogservice.models.Rating;
import io.movieservice.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//import static java.util.stream.Nodes.collect;



@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
        //get all rated movie IDs
        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating ->
                {
                    //For each movieId, call movie info service and get details
                    Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
                    //Put them all together
                    return new CatalogItem(movie.getName(), "Test", rating.getRating());
                })
                .collect(Collectors.toList());

    }

}
