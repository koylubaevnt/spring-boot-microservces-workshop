package io.javabrains.moviecatalogservice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRating {

    private String userId;

    private List<Rating> ratings;
}
