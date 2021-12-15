package com.csd051.superiora.network

import com.csd051.superiora.data.entity.Task
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("1uLIMQi9KzyBnRI3HBSWgxruxfnMDWpDEZMEBbJfEOjg/{path}")
    fun getResponseList(
        @Path("path") path: String
    ): Call<List<Task>>
}