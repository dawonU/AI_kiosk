// BurgerChoice.java
package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class BurgerChoice extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burgerchoice1);

        // TabBurgerAdapter에서 전달한 데이터만 받아오기
        Intent intent = getIntent();
        String menuName = intent.getStringExtra("menuName");
        String sub_Set_cat_name = intent.getStringExtra("sub_Set_cat_name");
        String sub_LSet_cat_name = intent.getStringExtra("sub_LSet_cat_name");
        int id = intent.getIntExtra("id", 0);
        int price = intent.getIntExtra("price", 0);
        String img_url = intent.getStringExtra("img_url");
        String img_url_burgerSet = intent.getStringExtra("img_url_burgerSet");
        int subSetPrice = intent.getIntExtra("Setprice", 0);
        int subLSetPrice = intent.getIntExtra("LSetprice", 0);

        // UI 요소 초기화
        TextView nameTextView = findViewById(R.id.bh1_nameTextView);      // 메뉴명 표시
        TextView priceTextView = findViewById(R.id.txt_burgerSingle2);      // 단품 가격 표시
        ImageView imgBurgerSingle = findViewById(R.id.img_burgerSingle);    // 단품 이미지
        ImageView imgBurgerSet = findViewById(R.id.img_burgerSet);          // 세트 이미지
        Button cancelButton = findViewById(R.id.btn_bc1_cancle);            // 취소 버튼

        // 받아온 데이터로 UI 구성
        nameTextView.setText(menuName);
        priceTextView.setText(price + " 원");

        // 이미지 로드 (Glide 라이브러리 사용)
        Glide.with(this)
                .load(img_url)
                .into(imgBurgerSingle);
        Glide.with(this)
                .load(img_url_burgerSet)
                .into(imgBurgerSet);


        // 세트 메뉴 카드 클릭 리스너 (필요한 경우 다음 액티비티로 필요한 데이터만 전달)
        View cardBurgerSet = findViewById(R.id.card_burgerSet);
        cardBurgerSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(BurgerChoice.this, BurgerChoice2.class);
                nextIntent.putExtra("menuName", menuName);
                nextIntent.putExtra("sub_Set_cat_name", sub_Set_cat_name);
                nextIntent.putExtra("sub_LSet_cat_name", sub_LSet_cat_name);
                nextIntent.putExtra("price", price);
                nextIntent.putExtra("img_url", img_url);
                nextIntent.putExtra("img_url_burgerSet", img_url_burgerSet);
                nextIntent.putExtra("sub_Set_price", subSetPrice);
                nextIntent.putExtra("sub_LSet_price", subLSetPrice);
                startActivity(nextIntent);
            }
        });

        View card_burgerSingle = findViewById(R.id.card_burgerSingle);
        card_burgerSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(BurgerChoice.this, MenuMain.class);
                nextIntent.putExtra("menuName", menuName);
                nextIntent.putExtra("price", price);
                nextIntent.putExtra("img_url", img_url);
                startActivity(nextIntent);
            }
        });

        // 취소 버튼 클릭 시 현재 액티비티 종료
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}


