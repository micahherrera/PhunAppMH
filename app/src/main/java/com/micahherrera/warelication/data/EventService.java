package com.micahherrera.warelication.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by micahherrera on 11/19/16.
 */

public interface EventService {

    @GET("/phunware/dev-interview-homework/master/feed.json")
    Call<List<Event>> getEvents();
}

