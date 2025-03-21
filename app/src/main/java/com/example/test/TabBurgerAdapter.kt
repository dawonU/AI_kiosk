package com.example.test

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.network.MenuResponse

class TabBurgerAdapter(private val burgermenuList: List<MenuResponse>) : RecyclerView.Adapter<TabBurgerAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.bc1_logo)

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val menu = itemView.getTag() as? MenuResponse // 클릭한 아이템의 메뉴 정보
                val intent = Intent(context, BurgerChoice::class.java).apply {
                    putExtra("menuName", menu?.name) // 메뉴 이름을 Intent에 추가
                }
                context.startActivity(intent)
            }
        }
    }

    // category_id가 11인 데이터만 필터링
    private val filteredMenuList: List<MenuResponse> = burgermenuList.filter { it.category_id == 11 }

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

        // ViewHolder에 메뉴 정보를 태그로 설정
        holder.itemView.setTag(menu)
    }

    override fun getItemCount(): Int {
        return filteredMenuList.size
    }
}

