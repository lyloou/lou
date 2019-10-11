package com.lyloou.test.media.pic;// [Android 拍照，从图库选择照片，并裁剪，上传到服务器 - 简书](https://www.jianshu.com/p/bfd9fe0592cb)
// [拍照/从相册读取图片后进行裁剪的方法 - developer_Kale - 博客园](http://www.cnblogs.com/tianzhijiexian/p/3989296.html)
// [Android 7.0适配 -- FileProvider 拍照、选择相册、裁切图片, 小米机型适配 - 简书](https://www.jianshu.com/p/bec4497c2a63)

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.lyloou.test.util.Ufile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropImageActivity extends AppCompatActivity {
    private static final String TAG = CropImageActivity.class.getSimpleName();
    public static final String TMP_DIR = Environment.getExternalStorageDirectory() + "/lyloou/picture";
    public static final String EXTRA_ERROR = "error";
    private int width;
    private int height;
    private File file;
    private Activity mContext;
    private static final int CAMERA_REQUEST_CODE = 1;
    public static final int CAMERA_RESULT_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        if (savedInstanceState == null) {
            initIconFile(true);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                showDialog();
                return;
            }
            requestPermission();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, CropImageActivity.class);
        context.startActivity(intent);
    }

    private void initIconFile(boolean remove) {
        File tmpDir = new File(TMP_DIR);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        file = new File(tmpDir.getAbsolutePath(), "icon.png");
        if (remove && file.exists()) {
            file.delete();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            showDialog();
        } else {
            //申请相机权限和STORAGE权限
            ActivityCompat.requestPermissions(
                    mContext,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                showDialog();
            } else {
                String msg = "没有相关运行权限";
                backWithError(msg);
            }
        }
    }

    private void showDialog() {
        Intent intent = getIntent();
        width = intent.getIntExtra("width", 0);
        height = intent.getIntExtra("height", 0);
        if (width == 0) {
            width = 350;
        }
        if (height == 0) {
            height = 350;
        }


        new AlertDialog.Builder(this)
                .setTitle("更换头像")
                .setItems(new String[]{"拍照", "从相册选择",}, (dialog, which) -> {
                    switch (which) {
                        case 0://拍照
                            PictureHelper.takePicture(mContext, file);
                            break;
                        case 1://从相册选择
                            PictureHelper.getFromAlbum(mContext);
                            break;
                    }
                })
                .setOnCancelListener(dialog -> finish())
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PictureHelper.REQUEST_CODE_PAIZHAO && data == null) {
            finish();
            return;
        }

        Bitmap bitmap;
        Uri uri;
        switch (requestCode) {
            case PictureHelper.REQUEST_CODE_PAIZHAO:     // 从相机跳回来
                if (resultCode == Activity.RESULT_CANCELED) {
                    finish();
                    return;
                }

                if (file == null) {
                    initIconFile(false);
                }
                if (!file.exists()) {
                    backWithError("拍照异常");
                    return;
                }
                // 启动裁剪器
                PictureHelper.crop(mContext, Ufile.getUriForFile(mContext, file), file, width, height);
                break;
            case PictureHelper.REQUEST_CODE_ZHAOPIAN:     // 从图库跳回来
                // 此处的uri 是content类型的。 还有一种是file 型的。应该转换为后者
                uri = data.getData();
                if (uri == null) {
                    backWithError("图片无效");
                    return;
                }
                PictureHelper.crop(mContext, uri, file, width, height);
                break;
            case PictureHelper.REQUEST_CODE_CAIQIE:     // 从裁剪处跳回来
                try {
                    uri = Ufile.getUriForFile(this, file);
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    // 方法1
                    uri = saveBitmap(bitmap);
                    if (uri == null) {
                        backWithError("裁剪失败");
                        return;
                    }
                    backWithSuccess(uri);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;
        }
    }

    private void backWithSuccess(Uri uri) {
        Intent intent = new Intent();
        intent.setData(uri);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void backWithError(String errorMsg) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ERROR, errorMsg);
        intent.setData(null);
        setResult(RESULT_FIRST_USER, intent);
        finish();
    }

    /**
     * 把 Bitmap 保存在SD卡路径后，返回file 类型的 uri
     */
    private Uri saveBitmap(Bitmap bm) {
        if (bm == null) {
            return null;
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 80, fos);
            fos.flush();
            fos.close();
            return Ufile.getUriForFile(this, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}