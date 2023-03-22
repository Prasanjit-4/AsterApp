package com.mitron.asterapp

import retrofit2.Call
import retrofit2.http.GET

interface PostsApi {
    @GET("posts")
    fun getPosts(): Call<ArrayList<AsterResponseModel>>
}