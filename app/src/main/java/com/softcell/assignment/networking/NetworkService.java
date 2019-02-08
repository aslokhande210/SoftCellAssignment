package com.softcell.assignment.networking;


import com.softcell.assignment.model.LoginRequest;
import com.softcell.assignment.model.ResponseToken;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;


public interface NetworkService {

    @POST("api/login")
    Observable<ResponseToken> login(@Body LoginRequest loginRequest);

}
