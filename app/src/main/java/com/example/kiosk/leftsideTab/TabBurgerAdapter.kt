package com.example.kiosk.leftsideTab

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kiosk.R
import com.example.kiosk.network.MenuResponse
import com.example.kiosk.BurgerChoice

class TabBurgerAdapter(private val burgermenuList: List<MenuResponse>) : RecyclerView.Adapter<TabBurgerAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        //val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.menuImgView)

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val menu = itemView.getTag() as? MenuResponse ?: return@setOnClickListener// 클릭한 아이템의 메뉴 정보
                //전달
                val intent = Intent(context, BurgerChoice::class.java).apply {
                    putExtra("menuName", menu.name)
                    putExtra("id", menu.id)
                    putExtra("price", menu.price)
                    putExtra("img_url", menu.img_url)

                    putExtra("Setprice", menu.sub_Set_price)
                    putExtra("LSetprice", menu.sub_LSet_price)
                    putExtra("Set_cat_name", menu.sub_Set_cat_name)
                    putExtra("LSet_cat_name", menu.sub_LSet_cat_name)

                    putExtra("img_url_burgerSet", menu.sub_Set_img_url)
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
        //holder.priceTextView.text = "${menu.price}원"

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

