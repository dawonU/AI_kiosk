// BurgerChoice.java
package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        // Intent로부터 데이터 가져오기
        Intent intent = getIntent();
        String menuName = intent.getStringExtra("menuName");
        int price = intent.getIntExtra("price", 0);
        String img_url = intent.getStringExtra("img_url");
        String img_url_burgerSet = intent.getStringExtra("img_url_burgerSet");

        Log.d("BurgerChoice", "img_url_burgerSet: " + img_url_burgerSet);

        // UI 요소 초기화
        TextView nameTextView = findViewById(R.id.bh1_nameTextView); //상단메뉴명

        View cardBurgerSet = findViewById(R.id.card_burgerSet); //세트메뉴
        ImageView imgBurgerSet = findViewById(R.id.img_burgerSet);

        View cardBurgerSingle = findViewById(R.id.card_burgerSingle); //단품메뉴
        ImageView imgBurgerSingle = findViewById(R.id.img_burgerSingle);
        TextView priceTextView = findViewById(R.id.txt_burgerSingle2);

        Button cancelButton = findViewById(R.id.btn_bc1_cancle); //하단 취소버튼


        // 메뉴 이름 및 가격 설정
        nameTextView.setText(menuName);
        priceTextView.setText(price + " 원");

        // 이미지 로드
        Glide.with(this)
                .load(img_url_burgerSet)
                .into(imgBurgerSet);
        Glide.with(this)
                .load(img_url)
                .into(imgBurgerSingle);

        // 취소 버튼 클릭 리스너
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 이전 화면으로 돌아가기
            }
        });


        // 세트 선택 카드 클릭 리스너
        cardBurgerSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BurgerChoice.this, BurgerChoice2.class);
                intent.putExtra("menuName", menuName); // 메뉴 이름 전달
                intent.putExtra("price", price); // 가격 전달
                intent.putExtra("img_url_burgerSet", img_url_burgerSet);
                startActivity(intent); // BurgerChoice2 액티비티 시작
            }
        });
    }
}


