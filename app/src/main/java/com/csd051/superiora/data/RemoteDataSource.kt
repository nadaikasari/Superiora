package com.csd051.superiora.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.csd051.superiora.network.ApiConfig
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.remote.ApiResponse
import com.csd051.superiora.utils.QueryUtilApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RemoteDataSource {

    fun getListData(tableLength: Int, courseId: Int): LiveData<ApiResponse<List<Task>>> {
//        EspressoIdlingResource.increment()
        val resultTask = MutableLiveData<ApiResponse<List<Task>>>()
        val client = ApiConfig.getApiService().getResponseList(QueryUtilApi.pathName(courseId))
        client.enqueue(object: Callback<List<Task>> {

            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {

                if (response.isSuccessful) {
//                    val listData = ArrayList<Task>()
//                    for (data in response.body()!!) {
//                        data.id_course = courseId
//                        data.id += tableLength + 1
//                        if (data.id_parent != -1) {
//                            data.id_parent += tableLength + 1
//                        }
//                        listData.add(
//                            data
//                        )
//                    }
                    resultTask.value = ApiResponse.success(response.body() as List<Task>)
                    Log.d("onResponsess: ", "resultTask.value.toString()")
//                    callback.onAllDataReceived(listData)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
//                EspressoIdlingResource.decrement()
            }
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return resultTask
    }

    interface LoadDataListCallback {
        fun onAllDataReceived(dataListResponse: List<Task>)
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