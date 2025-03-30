package com.example.kiosk.network

data class MenuResponse(
    val id: Int,
    val name: String,
    val menu_id: Int,
    val category: String,
    val category_id: Int,
    val daypart_id: Int,
    val price: Int,
    val img_url: String,

    val sub_Set_img_url: String,
    val sub_Set_price: Int,
    val sub_Set_cat_name: String,
    val sub_Set_menu_name: String,

    val sub_LSet_img_url: String,
    val sub_LSet_price: Int,
    val sub_LSet_cat_name: String,
    val sub_LSet_menu_name: String,

    val sub_menus: Any //String

)


data class SubMenuResponse(
    val id: Int,
    val price: Double,
    val cat_name: String,
    val menu_name: String,
    val components: Int?,
    val choies_menu: List<Int>?,
    val variationName: String,
    val img_url: String
)
