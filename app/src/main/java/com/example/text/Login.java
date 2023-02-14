package com.example.text;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.text.db.DatabaseHelper;
import com.example.text.db.User;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    //    登录按钮
    @SuppressLint("StaticFieldLeak")
    static Button login;
    //注册文字
    TextView register;
//    EditText username, Password;

    //记住功能复选框
    private CheckBox rememberPass, auto_login;
    //新建数据库
    private DatabaseHelper mSQLite;


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        rememberPass = (CheckBox) findViewById(R.id.remember);
//        auto_login = findViewById(R.id.auto);

        EditText accountEdit = findViewById(R.id.username);
        EditText passwordEdit = findViewById(R.id.password);
//        这是一个静态的方法，接受一个Context参数，并且自动使用当前应用程序的包名作为前缀来命名SharePreferences文件。
//        得到了SharePreferences对象之后，就可以开始向SharePreferences文件中存储数据了
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {//将账号和密码都设置到文本框中
            String account = pref.getString("account", "");
            String password1 = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password1);
            rememberPass.setChecked(true);
        }
//
//        boolean isAuto = pref.getBoolean("auto_ischeck", false);
//        if (isAuto) {
//            auto_login.setChecked(true);
//        }



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //全屏显示
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //因为背景为浅色所以将状态栏字体设置为黑色
            decorView.setSystemUiVisibility(option);//设置系统UI元素的可见性
            getWindow().setStatusBarColor(Color.TRANSPARENT);//将状态栏设置成透明色
        }

        EditText ed_name = findViewById(R.id.username);
        EditText ed_password = findViewById(R.id.password);


        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed_name.getText().toString().trim();//Stirng中的trim()方法的作用就是去掉字符串前面和后面的空格.
                String passwrod = ed_password.getText().toString().trim();


                ArrayList<User> data = mSQLite.getAllDATA();//查询数据库数据
                boolean flag = false;
                //遍历队列的数据
                for (int i = 0; i < data.size(); i++) {
//                    新建一个User等于队列中的每一个数据
                    User userdata = data.get(i);
//                    再用输入的name和password与User中的对比
                    if (name.equals(userdata.getName()) && passwrod.equals(userdata.getPassword())) {
//                        相同flag返回true
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }


//                对于一个UI界面中,当判断用户是否输入用户名或密码时,我们常用TextUtils.isEmpty()方法来判断;
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(passwrod)) {
                    if (flag) {
                        String account = accountEdit.getText().toString();
                        String password2 = passwordEdit.getText().toString();
                        //记住我功能
                        editor = pref.edit();

                        if (rememberPass.isChecked()) {//复选框是否被选中
                            editor.putBoolean("remember_password", true);
                            editor.putString("account", account);
                            editor.putString("password", password2);

                        } else {
                            //数据清除
                            editor.clear();
                        }
//                        apply()数据提交
                        editor.apply();


                        Intent intent1 = new Intent(Login.this, Interface.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(Login.this, "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "用户名与密码不能为空", Toast.LENGTH_SHORT).show();
                }

            }



        });


        //注册按钮
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Regsiter.class);
                startActivity(intent);
                finish();
            }
        });


        mSQLite = new DatabaseHelper(Login.this);//获取数据库对象

    }


}