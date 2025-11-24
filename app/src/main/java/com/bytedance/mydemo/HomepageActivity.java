package com.bytedance.mydemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomepageActivity extends AppCompatActivity {

    private Button inform_btn,collect_btn,history_btn,settings_btn,aboutus_btn,feedback_btn;
    private TextView name_text,signature_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homepage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String username = getIntent().getStringExtra("username");
        String signature = getIntent().getStringExtra("signature");

        name_text = findViewById(R.id.username);
        signature_text = findViewById(R.id.signature);

        name_text.setText(username);
        signature_text.setText(signature);

        inform_btn = findViewById(R.id.information);
        inform_btn.setOnClickListener(v -> {
            Toast.makeText(this, "个人信息点击成功", Toast.LENGTH_SHORT).show();
        });

        collect_btn = findViewById(R.id.collect);
        collect_btn.setOnClickListener(v -> {
            Toast.makeText(this, "我的收藏点击成功", Toast.LENGTH_SHORT).show();
        });

        history_btn = findViewById(R.id.history);
        history_btn.setOnClickListener(v -> {
            Toast.makeText(this, "浏览历史点击成功", Toast.LENGTH_SHORT).show();
        });

        settings_btn = findViewById(R.id.settings);
        settings_btn.setOnClickListener(v -> {
            Toast.makeText(this, "设置点击成功", Toast.LENGTH_SHORT).show();
        });

        aboutus_btn = findViewById(R.id.about_us);
        aboutus_btn.setOnClickListener(v -> {
            Toast.makeText(this, "关于我们点击成功", Toast.LENGTH_SHORT).show();
        });

        feedback_btn = findViewById(R.id.feedback);
        feedback_btn.setOnClickListener(v -> {
            Toast.makeText(this, "意见反馈点击成功", Toast.LENGTH_SHORT).show();
        });
    }
}