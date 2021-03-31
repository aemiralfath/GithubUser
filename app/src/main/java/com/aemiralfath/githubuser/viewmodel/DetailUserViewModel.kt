package com.aemiralfath.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aemiralfath.githubuser.model.entity.DetailUserResponse
import com.aemiralfath.githubuser.model.network.ServiceClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {
    private val token = "token b9303a8f853aded35a0947a47586bdd767129a6d"
    private var dataUser: MutableLiveData<DetailUserResponse> = MutableLiveData()

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

    fun getDataUser() = dataUser
}