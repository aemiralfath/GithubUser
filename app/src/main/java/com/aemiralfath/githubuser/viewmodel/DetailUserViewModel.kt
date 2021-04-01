package com.aemiralfath.githubuser.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aemiralfath.githubuser.BuildConfig
import com.aemiralfath.githubuser.model.entity.DetailUserResponse
import com.aemiralfath.githubuser.model.entity.FollowResponse
import com.aemiralfath.githubuser.model.network.ServiceClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {
    private val token = BuildConfig.API_TOKEN
    private var dataUser: MutableLiveData<DetailUserResponse> = MutableLiveData()
    private var dataUserFollower: MutableLiveData<List<FollowResponse>> = MutableLiveData()
    private var dataUserFollowing: MutableLiveData<List<FollowResponse>> = MutableLiveData()

    fun setUser(context: Context, username: String) {
        ServiceClient().buildServiceClient()
            .findUserDetailByUsername(username, token)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    Log.d("User", response.body().toString())
                    dataUser.postValue(response.body())
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.d("User", "fail")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun setUserFollowers(context: Context, username: String) {
        ServiceClient().buildServiceClient()
            .getUserFollowerByUsername(username, token)
            .enqueue(object : Callback<List<FollowResponse>> {
                override fun onResponse(
                    call: Call<List<FollowResponse>>,
                    response: Response<List<FollowResponse>>
                ) {
                    Log.d("UserFollowers", response.body().toString())
                    dataUserFollower.postValue(response.body())
                }

                override fun onFailure(call: Call<List<FollowResponse>>, t: Throwable) {
                    Log.d("UserFollowers", t.toString())
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun setUserFollowing(context: Context, username: String) {
        ServiceClient().buildServiceClient()
            .getUserFollowingByUsername(username, token)
            .enqueue(object : Callback<List<FollowResponse>> {
                override fun onResponse(
                    call: Call<List<FollowResponse>>,
                    response: Response<List<FollowResponse>>
                ) {
                    Log.d("UserFollowing", response.body().toString())
                    dataUserFollowing.postValue(response.body())
                }

                override fun onFailure(call: Call<List<FollowResponse>>, t: Throwable) {
                    Log.d("UserFollowing", "fail")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun getDataUser() = dataUser

    fun getDataUserFollowers() = dataUserFollower

    fun getDataUserFollowing() = dataUserFollowing

}