package com.csd051.superiora.data

import android.util.Log
import com.csd051.superiora.network.ApiConfig
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.utils.QueryUtilApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RemoteDataSource {


    companion object {

        private const val TAG = "RemoteDataSource"

        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource().apply { instance = this }
            }
    }

    fun getListData(tableLength: Int, courseId: Int, callback: LoadDataListCallback) {
//        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().getResponseList(QueryUtilApi.pathName(courseId))
        client.enqueue(object: Callback<List<Task>> {

            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {

                if (response.isSuccessful) {
                    val listData = ArrayList<Task>()
                    for (data in response.body()!!) {
                        data.id_course = courseId
                        data.id += tableLength + 1
                        if (data.id_parent != -1) {
                            data.id_parent += tableLength + 1
                        }
                        listData.add(
                            data
                        )
                    }
                    callback.onAllDataReceived(listData)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
//                EspressoIdlingResource.decrement()
            }
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    interface LoadDataListCallback {
        fun onAllDataReceived(movieListResponse: List<Task>)
    }

}