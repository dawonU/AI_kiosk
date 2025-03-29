//TabDessertAdapter.kt

package com.example.kiosk.leftsideTab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kiosk.R
import com.example.kiosk.network.MenuResponse

class TabDessertAdapter(private val dessertmenuList: List<MenuResponse>) : RecyclerView.Adapter<TabDessertAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.menuImgView)
    }

    // category_id가 15인 데이터만 필터링
    private val filteredMenuList: List<MenuResponse> = dessertmenuList.filter { it.category_id == 15 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = filteredMenuList[position]
        holder.nameTextView.text = menu.name
        holder.priceTextView.text = "${menu.price}원"

        Glide.with(holder.imageView.context)
            .load(menu.img_url)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return filteredMenuList.size
    }
}
