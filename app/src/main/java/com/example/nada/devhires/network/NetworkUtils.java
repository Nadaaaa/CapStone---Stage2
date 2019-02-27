package com.example.nada.devhires.network;

import com.example.nada.devhires.models.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkUtils {

    @GET("users/{username}/repos")
    Call<List<Repository>> getRepos(
            @Path(value = "username", encoded = true) String username
            /*@Query("type") String type,
            @Query("sort") String sort,
            @Query("direction") String direction*/
    );
}
    /*type	string	Can be one of all, owner, member. Default: owner
      sort	string	Can be one of created, updated, pushed, full_name. Default: full_name
      direction	string	Can be one of asc or desc. Default: when using full_name: asc, otherwise desc*/