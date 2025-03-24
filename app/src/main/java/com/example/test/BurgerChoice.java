// BurgerChoice.java
package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BurgerChoice extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burgerchoice1);

        // 메뉴 이름 설정
        TextView nameTextView = findViewById(R.id.bh1_nameTextView);
        Intent intent = getIntent();
        String menuName = intent.getStringExtra("menuName");
        nameTextView.setText(menuName);

        // 가격 설정
        TextView priceTextView = findViewById(R.id.txt_burgerSingle2);
        int price = intent.getIntExtra("price", 0);
        priceTextView.setText(price + " 원");

        // 취소 버튼에 대한 클릭 리스너 설정
        Button cancelButton = findViewById(R.id.btn_bc1_cancle);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 이전 화면으로 돌아가기
            }
        });

        // 세트선택 카드 -> burgerchoice2.xml
        View cardBurgerSet = findViewById(R.id.card_burgerSet);
        cardBurgerSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BurgerChoice.this, BurgerChoice2.class);
                intent.putExtra("menuName", menuName); // 메뉴 이름 전달
                //intent.putExtra("price", price);
                startActivity(intent);
            }
        });
    }
}

