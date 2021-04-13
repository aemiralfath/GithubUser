package com.aemiralfath.githubuser.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aemiralfath.githubuser.BuildConfig
import com.aemiralfath.githubuser.model.network.ServiceClient
import com.aemiralfath.githubuser.model.network.response.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val token = BuildConfig.API_TOKEN
    private var dataUser: MutableLiveData<UsersResponse> = MutableLiveData()

    fun setUser(context: Context) {
        ServiceClient().buildServiceClient()
            .searchUser("aemir", token)
            .enqueue(object : Callback<UsersResponse> {
                override fun onResponse(
                    call: Call<UsersResponse>,
                    response: Response<UsersResponse>
                ) {
                    Log.d("SearchUser", response.body().toString())
                    dataUser.postValue(response.body())
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    Log.d("SearchUser", "fail")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun searchUser(context: Context, query: String?) {
        query?.let {
            ServiceClient().buildServiceClient()
                .searchUser(it, token)
                .enqueue(object : Callback<UsersResponse> {
                    override fun onResponse(
                        call: Call<UsersResponse>,
                        response: Response<UsersResponse>
                    ) {
                        Log.d("SearchUser", response.body().toString())
                        dataUser.postValue(response.body())
                    }

                    override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                        Log.d("SearchUser", "fail")
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun getDataUser() = dataUser
}