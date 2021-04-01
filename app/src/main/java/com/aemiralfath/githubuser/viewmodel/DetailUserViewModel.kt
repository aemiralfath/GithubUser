package com.aemiralfath.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aemiralfath.githubuser.model.entity.DetailUserResponse
import com.aemiralfath.githubuser.model.entity.FollowResponse
import com.aemiralfath.githubuser.model.network.ServiceClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {
    private val token = "token b9303a8f853aded35a0947a47586bdd767129a6d"
    private var dataUser: MutableLiveData<DetailUserResponse> = MutableLiveData()
    private var dataUserFollower: MutableLiveData<List<FollowResponse>> = MutableLiveData()
    private var dataUserFollowing: MutableLiveData<List<FollowResponse>> = MutableLiveData()

    fun setUser(username: String) {
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
                }
            })
    }

    fun setUserFollowers(username: String) {
        ServiceClient().buildServiceClient()
            .getUserFollowerByUsername(username, token)
            .enqueue(object : Callback<List<FollowResponse>>{
                override fun onResponse(
                    call: Call<List<FollowResponse>>,
                    response: Response<List<FollowResponse>>
                ) {
                    Log.d("UserFollowers", response.body().toString())
                    dataUserFollower.postValue(response.body())
                }

                override fun onFailure(call: Call<List<FollowResponse>>, t: Throwable) {
                    Log.d("UserFollowers", t.toString())
                }

            })
    }

    fun setUserFollowing(username: String) {
        ServiceClient().buildServiceClient()
            .getUserFollowingByUsername(username, token)
            .enqueue(object : Callback<List<FollowResponse>>{
                override fun onResponse(
                    call: Call<List<FollowResponse>>,
                    response: Response<List<FollowResponse>>
                ) {
                    Log.d("UserFollowing", response.body().toString())
                    dataUserFollowing.postValue(response.body())
                }

                override fun onFailure(call: Call<List<FollowResponse>>, t: Throwable) {
                    Log.d("UserFollowing", "fail")
                }

            })
    }

    fun getDataUser() = dataUser

    fun getDataUserFollowers() = dataUserFollower

    fun getDataUserFollowing() = dataUserFollowing

}