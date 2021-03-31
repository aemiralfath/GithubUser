package com.aemiralfath.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aemiralfath.githubuser.model.entity.UsersResponse
import com.aemiralfath.githubuser.model.network.ServiceClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val token = "b9303a8f853aded35a0947a47586bdd767129a6d"
    private var dataUser: MutableLiveData<UsersResponse> = MutableLiveData()

    fun setUser() {
        ServiceClient().buildServiceClient()
            .searchUser("aemir", token)
            .enqueue(object : Callback<UsersResponse> {
                override fun onResponse(
                    call: Call<UsersResponse>,
                    response: Response<UsersResponse>
                ) {
                    Log.d("Search User", response.body().toString())
                    dataUser.postValue(response.body())
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    Log.d("Search User", "fail")
                }
            })
    }

    fun searchUser(query: String?) {
        query?.let {
            ServiceClient().buildServiceClient()
                .searchUser(it, token)
                .enqueue(object : Callback<UsersResponse> {
                    override fun onResponse(
                        call: Call<UsersResponse>,
                        response: Response<UsersResponse>
                    ) {
                        Log.d("Search User", response.body().toString())
                        dataUser.postValue(response.body())
                    }

                    override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                        Log.d("Search User", "fail")
                    }
                })
        }
    }

    fun getDataUser() = dataUser
}