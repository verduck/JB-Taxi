package com.jj.bootcamp.jbtaxi.service;

import com.jj.bootcamp.jbtaxi.domain.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HeyTaxiService {
    @POST("user/signup")
    Call<User> signUp(@Body User user);

    @POST("user/signin")
    Call<User> signIn(@Body User user);
}
