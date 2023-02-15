//package com.example.text;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//
//import android.annotation.SuppressLint;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//
//public class See extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
//    private AddressBook fg1, fg2, fg3;
//    private FragmentManager fManager;
//
//
//    private RadioGroup rg_tab_bar;
//    private RadioButton rb_channel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.see);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //全屏显示
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //因为背景为浅色所以将状态栏字体设置为黑色
//            decorView.setSystemUiVisibility(option);//设置系统UI元素的可见性
//            getWindow().setStatusBarColor(Color.TRANSPARENT);//将状态栏设置成透明色
//        }
//
//
//        fManager = getFragmentManager();
//        rg_tab_bar = (RadioGroup) findViewById(R.id.fragmeng_radioGroup);
//        rg_tab_bar.setOnCheckedChangeListener(this);
//        //获取第一个单选按钮，并设置其为选中状态
//        rb_channel = (RadioButton) findViewById(R.id.fragment_radioBtn1);
//        rb_channel.setChecked(true);
//
//    }
//
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        FragmentTransaction fTransaction = fManager.beginTransaction();
//
//        hideAllFragment(fTransaction);
//        switch (checkedId) {
//            case R.id.fragment_radioBtn1:
////                fTransaction.add(R.id.fragment, fg1);
////
////                fTransaction.show(fg1);
//
//                if (fg1 == null) {
//                    fg1 = new AddressBook("第一个Fragment");
//                    fTransaction.add(R.id.fragment, fg1);
//                } else {
//                    fTransaction.show(fg1);
//                }
//                break;
//        }
//    }
//
//    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
//        if (fg1 != null) fragmentTransaction.hide(fg1);
//    }
//}


package com.example.text;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.text.fragment.FristFragment;
import com.example.text.fragment.SecondFragment;
import com.example.text.fragment.ThirdFragment;


public class Interface extends FragmentActivity implements View.OnClickListener {
    private FristFragment firstFragment = null;// 用于显示工作台界面
    private SecondFragment secondFragment = null;// 用于显示通讯录界面
    private ThirdFragment thirdFragment = null;// 用于显示我界面


    private View firstLayout = null;// 工作台显示布局
    private View secondLayout = null;// 通讯录显示布局
    private View thirdLayout = null;// 我显示布局

    /*声明组件变量*/
    private ImageView fristImg = null;
    private ImageView contactImg = null;
    private ImageView findImg = null;


    private TextView fristText = null;
    private TextView contactText = null;
    private TextView findText = null;


    private FragmentManager fragmentManager = null;// 用于对Fragment进行管理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//要求窗口没有title
        super.setContentView(R.layout.interface_);
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();//用于对Fragment进行管理
        // 设置默认的显示界面
        setTabSelection(0);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //因为背景为浅色所以将状态栏字体设置为黑色
            decorView.setSystemUiVisibility(option);//设置系统UI元素的可见性
            getWindow().setStatusBarColor(Color.TRANSPARENT);//将状态栏设置成透明色
        }

    }

    /**
     * 在这里面获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件
     */
    @SuppressLint("NewApi")
    public void initViews() {
//        FragmentManager: 为了管理Activity中的fragments,需要使用FragmentManager。可以通过调用getFragmentManager()
        fragmentManager = getFragmentManager();
        firstLayout = findViewById(R.id.frist_layout);
        secondLayout = findViewById(R.id.contacts_layout);
        thirdLayout = findViewById(R.id.self_layout);


        fristImg = (ImageView) findViewById(R.id.frist_img);
        contactImg = (ImageView) findViewById(R.id.contact_img);
        findImg = (ImageView) findViewById(R.id.self_img);


        fristText = (TextView) findViewById(R.id.frist_text);
        contactText = (TextView) findViewById(R.id.contact_text);
        findText = (TextView) findViewById(R.id.self_text);


        //处理点击事件
        firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);
        thirdLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frist_layout:
                setTabSelection(0);// 当点击了工作台时，选中第1个tab
                break;
            case R.id.contacts_layout:
                setTabSelection(1);// 当点击了通讯录时，选中第2个tab
                break;
            case R.id.self_layout:
                setTabSelection(2);// 当点击了我时，选中第3个tab
                break;

            default:
                break;
        }

    }

    /**
     * 根据传入的index参数来设置选中的tab页 每个tab页对应的下标。0表示工作台，1表示通讯录，2表示我
     */
    @SuppressLint("NewApi")
    private void setTabSelection(int index) {
        clearSelection();// 每次选中之前先清除掉上次的选中状态
//        FragmentTransaction对fragment进行添加,移除,替换,以及执行其他动作。
        FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启一个Fragment事务
        hideFragments(transaction);// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        switch (index) {
            case 0:
                // 当点击了我的tab时改变控件的图片和文字颜色
                findImg.setImageResource(R.drawable.baseline_panorama_fish_eye_24);//修改布局中的图片
                findText.setTextColor(Color.parseColor("#0090ff"));//修改字体颜色

                if (firstFragment == null) {

                    firstFragment = new FristFragment();
                    transaction.add(R.id.fragment, firstFragment);

                } else {
                    // 如果FirstFragment不为空，则直接将它显示出来
                    transaction.show(firstFragment);//显示的动作
                }
                break;
            // 以下和firstFragment类同
            case 1:
                contactImg.setImageResource(R.drawable.tab_address_pressed);
                contactText.setTextColor(Color.parseColor("#0090ff"));
                if (secondFragment == null) {

                    secondFragment = new SecondFragment();
                    transaction.add(R.id.fragment, secondFragment);
                } else {
                    transaction.show(secondFragment);
                }
                break;
            case 2:
                findImg.setImageResource(R.drawable.tab_settings_pressed);
                findText.setTextColor(Color.parseColor("#0090ff"));
                if (thirdFragment == null) {
                    thirdFragment = new ThirdFragment();
                    transaction.add(R.id.fragment, thirdFragment);
                } else {
                    transaction.show(thirdFragment);
                }
                break;

        }
        transaction.commit();

    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {
        findImg.setImageResource(R.drawable.baseline_panorama_fish_24);
        fristText.setTextColor(Color.parseColor("#82858b"));

        contactImg.setImageResource(R.drawable.tab_address_normal);
        contactText.setTextColor(Color.parseColor("#82858b"));

        findImg.setImageResource(R.drawable.tab_settings_normal);
        findText.setTextColor(Color.parseColor("#82858b"));

    }

    /**
     * 将所有的Fragment都设置为隐藏状态 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (firstFragment != null) {
            transaction.hide(firstFragment);
        }
        if (secondFragment != null) {
            transaction.hide(secondFragment);
        }
        if (thirdFragment != null) {
            transaction.hide(thirdFragment);
        }

    }


    /**
     * 返回菜单键监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            alertDialog.setTitle("系统提示:");
            // 设置对话框消息
            alertDialog.setMessage("亲你确定要退出该应用吗?");
            // 添加选择按钮并注册监听
            alertDialog.setButton("确定", listener);
            alertDialog.setButton2("取消", listener);
            // 显示对话框
            alertDialog.show();

        }
        return false;
    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // "确认"按钮退出程序
                case AlertDialog.BUTTON_POSITIVE:
                    finish();
                    break;
                // "取消"第二个按钮取消对话框
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };


}
