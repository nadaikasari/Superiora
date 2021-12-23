package com.csd051.superiora.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.remote.ApiResponse
import com.csd051.superiora.network.ApiConfig
import com.csd051.superiora.utils.QueryUtilApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    fun getListData(courseId: Int): LiveData<ApiResponse<List<Task>>> {
        val resultTask = MutableLiveData<ApiResponse<List<Task>>>()
        val client = ApiConfig.getApiService().getResponseList(QueryUtilApi.pathName(courseId))
        client.enqueue(object : Callback<List<Task>> {

            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    resultTask.value = ApiResponse.success(response.body() as List<Task>)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return resultTask
    }

    companion object {
        private const val TAG = "RemoteDataSource"

        @Volatile
        private var instance: RemoteDataSource? = null
        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource().apply { instance = this }
            }
    }

}