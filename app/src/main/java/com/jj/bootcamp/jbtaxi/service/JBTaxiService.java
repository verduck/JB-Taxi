package com.jj.bootcamp.jbtaxi.service;

import com.jj.bootcamp.jbtaxi.domain.Taxi;
import com.jj.bootcamp.jbtaxi.domain.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface JBTaxiService {
    @POST("user/signup")
    Call<User> signUp(@Body User user);

    @POST("user/signin")
    Call<User> signIn(@Body User user);

    @PATCH("user/update")
    Call<User> updateUser(@Body User user);

    @POST("user/get")
    Call<User> getUser(@Body User user);

    @PATCH("user/register-certification")
    Call<User> registerCertification(@Body User user);

    @POST("taxi/register")
    Call<Taxi> registerTaxi(@Body Taxi taxi);

    @POST("taxi/get")
    Call<Taxi> getTaxi(@Body User user);
}
