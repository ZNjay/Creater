package com.example.text;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.text.db.DatabaseHelper;
import com.example.text.db.User;

import java.util.ArrayList;

public class Regsiter extends AppCompatActivity {

    private DatabaseHelper mSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regsiter);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //因为背景为浅色所以将状态栏字体设置为黑色
            decorView.setSystemUiVisibility(option);//设置系统UI元素的可见性
            getWindow().setStatusBarColor(Color.TRANSPARENT);//将状态栏设置成透明色
        }


        //找到各个控件
        Button btn_ready = findViewById(R.id.reday);
        Button btn_back = findViewById(R.id.back);
        EditText ed_name = findViewById(R.id.userName);
        EditText ed_password = findViewById(R.id.userpassword);

        //注册监听事件
        btn_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的用户名和密码
                String name = ed_name.getText().toString().trim();
                String password = ed_password.getText().toString().trim();

                //获取数据库数据，判断用户名是否已存在
                ArrayList<User> data = mSQLite.getAllDATA();
                boolean flag = false;
                for (int i = 0; i < data.size(); i++) {
                    User userdata = data.get(i);
                    if (name.equals(userdata.getName())) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }
                //判断用户名和密码是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    if (!flag) {
                        mSQLite.insert(name, password);
                        Intent intent1 = new Intent(Regsiter.this, Login.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(Regsiter.this, "注册成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Regsiter.this, "用户名已被注册", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Regsiter.this, "用户名与密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //监听返回按钮
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Regsiter.this, Login.class);
                startActivity(intent2);
                finish();
            }
        });

        mSQLite = new DatabaseHelper(Regsiter.this);
    }
}