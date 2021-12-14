package com.csd051.superiora.data.api

import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.entity.TaskResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("1uLIMQi9KzyBnRI3HBSWgxruxfnMDWpDEZMEBbJfEOjg/{path}")
    fun getResponseList(
        @Path("path") path: String
    ): Call<List<Task>>
}