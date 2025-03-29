// MenuAdapter.kt
package com.example.kiosk

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kiosk.network.MenuResponse


class MenuAdapter(private val menuList: List<MenuResponse>, private val context: MenuMain) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.bc1_logo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]
        holder.nameTextView.text = menu.name
        holder.priceTextView.text = "₩: ${menu.price}"

        Glide.with(holder.imageView.context)
            .load(menu.img_url)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BurgerChoice2::class.java)
            intent.putExtra("menuName", menu.name) // 메뉴 이름을 Intent에 추가
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
}
