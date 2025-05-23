package com.example.kiosk.leftsideTab

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kiosk.MenuMain
import com.example.kiosk.R
import com.example.kiosk.network.MenuResponse

class TabCoffeeAdapter(
    private val coffeemenuList: List<MenuResponse>,
    private val ageGroup: String) : RecyclerView.Adapter<TabCoffeeAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.menuImgView)
    }

    // category_id가 17인 데이터만 필터링
    private val filteredMenuList: List<MenuResponse> = coffeemenuList.filter { it.category_id == 17 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = filteredMenuList[position]
        holder.nameTextView.text = menu.name
        holder.priceTextView.text = "${menu.price}원"

        // ageGroup 이 "senior"인 경우 텍스트 크기를 16sp, 아니면 기본 13sp로 설정
        if (ageGroup == "senior") {
            holder.nameTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 20f)
            holder.priceTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 24f)
        } else {
            holder.nameTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13f)
            holder.priceTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13f)
        }

        Glide.with(holder.imageView.context)
            .load(menu.img_url)
            .into(holder.imageView)

        // 아이템 클릭 시 MenuMain에 커피 메뉴를 추가하도록 인텐트 전달
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MenuMain::class.java).apply {
                putExtra("menuName", menu.name)
                putExtra("price", menu.price)
                // 커피 메뉴는 단품이므로 추가 가격 정보는 0으로 설정
                putExtra("subSetPrice", 0)
                putExtra("subLSetPrice", 0)
                // 기존 MenuMain 인스턴스에 새로운 인텐트 전달 (예: singleTop 모드)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredMenuList.size
    }
}
