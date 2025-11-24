package com.bytedance.mydemo;
import com.bytedance.mydemo.MyDatabaseHelper;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.MotionEvent;
import android.view.View;
import android.text.InputType;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginActivity extends AppCompatActivity {
    private Button login_btn;
    private Button weixin_btn;
    private Button apple_btn;
    private EditText etEmail;
    private EditText etPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // 获取 SharedPreferences.Editor
        SharedPreferences.Editor editor = getSharedPreferences("app_config",
                Context.MODE_PRIVATE).edit();
        // 存⼊数据
        editor.putString("Sam", "Hello World!");
        // 提交数据
        editor.apply();

        // 用户数据库
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_NAME, "Sam");
        values.put(MyDatabaseHelper.COLUMN_EMAIL, "123456@qq.com");
        values.put(MyDatabaseHelper.COLUMN_PASSWORD, "88888888");
        database.insert(MyDatabaseHelper.TABLE_NOTES, null, values);

        // 按钮事件
        login_btn = findViewById(R.id.login);
        login_btn.setOnClickListener(v -> {
            etEmail = findViewById(R.id.et_email);
            etPassword = findViewById(R.id.et_password);
            String inputEmail = etEmail.getText().toString();
            String inputPassword = etPassword.getText().toString();

            // 用于存储查询结果的变量
            String username = null;
            String signature = null;

            // 定义查询参数
            String[] projection = {
                    MyDatabaseHelper.COLUMN_NAME
            };

            String selection =
                    MyDatabaseHelper.COLUMN_EMAIL + " = ? AND " +
                            MyDatabaseHelper.COLUMN_PASSWORD + " = ?";

            String[] selectionArgs = { // 筛选参数：替换 ? 的值
                    inputEmail,
                    inputPassword
            };

            // 执行查询
            Cursor cursor = database.query(
                    MyDatabaseHelper.TABLE_NOTES, // 表名
                    projection,                   // 要返回的列
                    selection,                    // WHERE 子句 (email = ? AND password = ?)
                    selectionArgs,                // WHERE 子句的值
                    null,                         // GROUP BY
                    null,                         // HAVING
                    null                          // ORDER BY
            );

            // 处理查询结果
            if (cursor.moveToFirst()) {
                // 找到了匹配的用户

                // 获取列索引
                int nameIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NAME);

                // 获取数据
                if (nameIndex != -1) {
                    username = cursor.getString(nameIndex);
                    SharedPreferences prefs = getSharedPreferences("app_config", Context.MODE_PRIVATE);
                    signature = prefs.getString(username, "这个用户没有签名");
                }


                // 登录成功：跳转到下一个 Activity 并传递数据
                Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                if (username != null) {
                    intent.putExtra("username", username); // 传递用户名
                }
                if (signature != null) {
                    intent.putExtra("signature", signature); // 传递签名
                }

                // 弹出提示
                Toast.makeText(LoginActivity.this, "登录成功，欢迎 " + username, Toast.LENGTH_SHORT).show();

                startActivity(intent);
                finish(); // 结束 LoginActivity，用户按返回键时不会回到登录页

            } else {
                // 登录失败：未找到匹配的账户
                Toast.makeText(LoginActivity.this, "邮箱或密码错误，请重试", Toast.LENGTH_SHORT).show();
            }

            // 6. 关闭 Cursor 和 Database
            cursor.close();
            database.close();
        });

        weixin_btn = findViewById(R.id.weixin_login);
        weixin_btn.setOnClickListener(v -> {
            Toast.makeText(this, "微信登录成功", Toast.LENGTH_SHORT).show();
        });

        apple_btn = findViewById(R.id.apple_login);
        apple_btn.setOnClickListener(v -> {
            Toast.makeText(this, "Apple登录成功", Toast.LENGTH_SHORT).show();
        });

        etPassword = findViewById(R.id.et_password);
        // 设置初始图标(隐藏状态)
        updatePasswordIcon(false);

        // 设置密码显示/隐藏的点击事件
        etPassword.setOnTouchListener((v, event) -> {
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
                    v.performClick();
                    return true;
                }
            }
            return false;
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

