//MenuResponse.kt

package com.example.test.network

data class MenuResponse(
    val id: Int,
    val name: String,
    val menu_id: Int,
    val category: String,
    val category_id: Int,
    val daypart_id: Int,
    val price: Int,
    val img_url: String,
    val sub_menus: Any ,
)

data class SubMenuResponse(
    val id: Int,
    val img: String,
    val price: Double,
    val cat_name: String,
    val menu_name: String,
    val components: Int?,
    val choies_menu: List<Int>?,
    val variationName: String
)
