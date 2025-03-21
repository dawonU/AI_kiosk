// java>network>ApiService
package com.example.test.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("menus")
    fun getMenus(): Call<List<MenuResponse>>
}