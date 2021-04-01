package com.aemiralfath.githubuser.model.network

import com.aemiralfath.githubuser.model.entity.DetailUserResponse
import com.aemiralfath.githubuser.model.entity.FollowResponse
import com.aemiralfath.githubuser.model.entity.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceRepository {

    @GET("/search/users")
    fun searchUser(
        @Query("q") username: String,
        @Header("Authorization") token: String
    ): Call<UsersResponse>

    @GET("/users/{username}")
    fun findUserDetailByUsername(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<DetailUserResponse>

    @GET("/users/{username}/followers")
    fun getUserFollowerByUsername(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<FollowResponse>>

    @GET("/users/{username}/following")
    fun getUserFollowingByUsername(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<FollowResponse>>
}