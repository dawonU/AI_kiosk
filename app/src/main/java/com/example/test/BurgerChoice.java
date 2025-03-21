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
        setContentView(R.layout.burgerchoice1); // burgerchoice1.xml 레이아웃을 설정

        // 메뉴 이름 설정
        TextView nameTextView = findViewById(R.id.bh1_nameTextView);
        Intent intent = getIntent();
        String menuName = intent.getStringExtra("menuName"); // 메뉴 이름을 Intent에서 가져옴
        nameTextView.setText(menuName); // TextView에 설정

        // 취소 버튼에 대한 클릭 리스너 설정
        Button cancelButton = findViewById(R.id.btn_bc1_back);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 이전 화면으로 돌아가기
            }
        });

        // burgerchoice2.xml로 전환
        View cardBurgerSet = findViewById(R.id.card_burgerSet);
        cardBurgerSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BurgerChoice.this, BurgerChoice2.class);
                intent.putExtra("menuName", menuName); // 메뉴 이름 전달
                startActivity(intent);
            }
        });
    }
}

