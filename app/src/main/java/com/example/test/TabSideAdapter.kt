//TabSideAdapter.kt

package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.network.MenuResponse

class TabSideAdapter(private val sidemenuList: List<MenuResponse>) : RecyclerView.Adapter<TabSideAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.bc1_logo)
    }

    // category_id가 13인 데이터만 필터링
    private val filteredMenuList: List<MenuResponse> = sidemenuList.filter { it.category_id == 13 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = filteredMenuList[position]
        holder.nameTextView.text = menu.name
        holder.priceTextView.text = "₩: ${menu.price}"

        Glide.with(holder.imageView.context)
            .load(menu.img_url)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return filteredMenuList.size
    }
}
