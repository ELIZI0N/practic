package com.example.practic.api

import com.example.practic.data.TopMangaResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("top/manga")
    fun getTopManga(): Call<TopMangaResponse>
}
