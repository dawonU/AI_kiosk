// java>network>ApiService
package com.example.test.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("menus")
    fun getMenus(): Call<List<MenuResponse>>

    // 추가된 메소드: 메뉴를 가져오고 sub_menus를 파싱하는 메소드
    fun fetchMenus(onResult: (List<SubMenuResponse>?) -> Unit) {
        getMenus().enqueue(object : Callback<List<MenuResponse>> {
            override fun onResponse(call: Call<List<MenuResponse>>, response: Response<List<MenuResponse>>) {
                if (response.isSuccessful) {
                    val menus = response.body()
                    // 모든 메뉴의 sub_menus를 파싱하여 SubMenuResponse 리스트로 변환
                    val prices = menus?.flatMap { menuResponse ->
                        parseSubMenus(menuResponse.sub_menus) ?: emptyList() // null 처리
                    } ?: emptyList() // null 처리
                    onResult(prices) // SubMenuResponse 리스트 반환
                } else {
                    onResult(null) // 실패 시 null 반환
                }
            }

            override fun onFailure(call: Call<List<MenuResponse>>, t: Throwable) {
                onResult(null) // 실패 시 null 반환
            }
        })
    }

    // sub_menus 파싱 메소드
    private fun parseSubMenus(subMenusData: Any): List<SubMenuResponse>? {
        return when (subMenusData) {
            is List<*> -> subMenusData.mapNotNull { it as? Map<*, *> }.map {
                SubMenuResponse(
                    id = it["id"] as Int,
                    price = it["price"] as Double,
                    cat_name = it["cat_name"] as String,
                    menu_name = it["menu_name"] as String,
                    components = it["components"] as? Int,
                    choies_menu = it["choies_menu"] as? List<Int>,
                    variationName = it["variationName"] as String,
                    img_url = it["img_url_burgerSet"] as? String ?: it["img_url_burgerLSet"] as? String ?: it["img_burgerSingle"] as String
                )
            }
            else -> null
        }
    }

    // 가격을 가져오는 메소드
    private fun getPricesForLargeAndSet(menuResponse: MenuResponse): List<SubMenuResponse> {
        val subMenus = parseSubMenus(menuResponse.sub_menus) ?: emptyList() // null 처리
        return subMenus.filter { it.variationName == "라지세트" || it.variationName == "세트" }
    }
}