package com.example.text.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.text.R;
import com.example.text.utils.BitmapUtils;
import com.example.text.utils.CameraUtils;
import com.example.text.utils.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.tbruyelle.rxpermissions3.RxPermissions;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThirdFragment extends Fragment {


    private ImageView imageView;
    Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.thirdfragment, container, false);

        return view;

    }

    //权限请求
    private RxPermissions rxPermissions;

    //是否拥有权限
    private boolean hasPermissions = false;

    //底部弹窗
    private BottomSheetDialog bottomSheetDialog;
    //弹窗视图
    private View bottomView;
    //存储拍完照后的照片
    private File outputImagePath;
    //    启动相机标识
    public static final int TAKE_PHOTO = 1;
    //    启动相册标识
    public static final int SELECT_PHOTO = 2;

    //图片控件
    private ShapeableImageView ivHead;
    //Base64
    private String base64Pic;
    //拍照和相册获取图片的Bitmap
    private Bitmap orc_bitmap;

    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ivHead = getActivity().findViewById(R.id.iv_head);

        //检查版本
        checkVersion();
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
                TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
                TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
                TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);

                //拍照
                tvTakePictures.setOnClickListener(v -> {
                    takePhoto();
                    showMsg("拍照");
                    bottomSheetDialog.cancel();
                });
                //打开相册
                tvOpenAlbum.setOnClickListener(v -> {
                    openAlubm();
                    showMsg("打开相册");
                    bottomSheetDialog.cancel();
                });
                //取消
                tvCancel.setOnClickListener(v -> {
                    bottomSheetDialog.cancel();
                });
                //底部弹窗显示
                bottomSheetDialog.show();

            }
        });
        //取出缓存
        String imageUrl = SPUtils.getString("imageUrl", null, getActivity());
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).apply(requestOptions).into(ivHead);
        }
    }

    /**
     * 打开相册
     */
    private void openAlubm() {
        if (!hasPermissions) {
            showMsg("未获取到权限");
            checkVersion();
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);


    }


    /**
     * 检查版本
     */
    private void checkVersion() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是在Fragment中，则把this换成getActivity()
            rxPermissions = new RxPermissions((FragmentActivity) getActivity());
            //权限请求
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            //申请成功
                            showMsg("已获取权限");
                            hasPermissions = true;
                        } else {
                            //申请失败
                            showMsg("权限未开启");
                            hasPermissions = true;
                        }
                    });
        } else {
            showMsg("无需请求动态权限");
        }
    }

    /**
     * 更换头像
     *
     * @param view
     */
//    public void changeAvatar(View view) {
//        bottomSheetDialog = new BottomSheetDialog(getActivity());
//        bottomView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
//        bottomSheetDialog.setContentView(bottomView);
//        bottomSheetDialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
//        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
//        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
//        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);
//
//        //拍照
//        tvTakePictures.setOnClickListener(v -> {
//            takePhoto();
//            showMsg("拍照");
//            bottomSheetDialog.cancel();
//        });
//        //打开相册
//        tvOpenAlbum.setOnClickListener(v -> {
//            openAlubm();
//            showMsg("打开相册");
//            bottomSheetDialog.cancel();
//        });
//        //取消
//        tvCancel.setOnClickListener(v -> {
//            bottomSheetDialog.cancel();
//        });
//        //底部弹窗显示
//        bottomSheetDialog.show();
//
//    }


    /**
     * 拍照
     */
    private void takePhoto() {
        if (!hasPermissions) {
            showMsg("未获取到权限");
            checkVersion();
            return;
        }
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getActivity().getExternalCacheDir(),
                filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(getActivity(), outputImagePath);
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }


    /**
     * 返回到Activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照后返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //显示图片
                    displayImage(outputImagePath.getAbsolutePath());
                }
                break;
            //打开相册后返回
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4及以上系统使用这个方法处理图片
                        imagePath = CameraUtils.getImageOnKitKatPath(data, getActivity());
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, getActivity());
                    }
                    //显示图片
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 通过图片路径显示图片
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {

            //放入缓存
            SPUtils.putString("imageUrl", imagePath, getActivity());

            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(ivHead);

            //压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);

        } else {
            showMsg("图片获取失败");
        }
    }


    /**
     * Toast提示
     *
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
