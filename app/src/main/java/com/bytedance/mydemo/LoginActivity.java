package com.bytedance.mydemo;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.MotionEvent;
import android.view.View;
import android.text.InputType;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button login_btn;
    private EditText etPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.login);
        login_btn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
            startActivity(intent);
        });

        etPassword = findViewById(R.id.et_password);
        // 设置初始图标(隐藏状态)
        updatePasswordIcon(false);

        // 设置密码显示/隐藏的点击事件
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPassword.getRight() -
                            etPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width() -
                            etPassword.getPaddingEnd())) {

                        // 切换密码可见性
                        isPasswordVisible = !isPasswordVisible;

                        if (isPasswordVisible) {
                            // 显示密码
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            // 隐藏密码
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }

                        // 保持光标在末尾
                        etPassword.setSelection(etPassword.getText().length());
                        // 更新图标状态
                        updatePasswordIcon(isPasswordVisible);

                        return true;
                    }
                }
                return false;
            }
        });
    }

    // 更新图标
    private void updatePasswordIcon(boolean isVisible) {
        int sizeInDp = 30;
        float scale = getResources().getDisplayMetrics().density;
        int sizeInPx = (int) (sizeInDp * scale + 0.5f);

        // 根据状态选择不同的图标
        int iconRes = isVisible ? R.drawable.show_password : R.drawable.hide_password;
        Drawable eyeIcon = ContextCompat.getDrawable(this, iconRes);

        if (eyeIcon != null) {
            eyeIcon.setBounds(0, 0, sizeInPx, sizeInPx);
            etPassword.setCompoundDrawables(null, null, eyeIcon, null);
        }
    }
}
