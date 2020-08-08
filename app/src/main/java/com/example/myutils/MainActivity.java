package com.example.myutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library.Listener.DownloadProgressListener;
import com.example.library.Listener.UploadProgressListener;
import com.example.library.Utils.ProgressBarView;
import com.example.library.Utils.fileutils.FileHttpUtil;
import com.lcw.library.imagepicker.ImagePicker;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button startUpload, chooseFile, startDownload;
    TextView filePath, downloadProgress;
    int REQUESTCODE_FROM_ACTIVITY = 1000;
    private ProgressBarView prodressBar;
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private String path;
    private String fileurl;
    private String thumburl;
    private Context context;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    initView();
                    deal();
                    // Storage permission are allowed.
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                })
                .start();


    }

    public void initView() {
        chooseFile = (Button) findViewById(R.id.chooseFile);
        startUpload = (Button) findViewById(R.id.startUpload);
        filePath = (TextView) findViewById(R.id.filePath);
        startDownload = (Button) findViewById(R.id.startDownload);
        downloadProgress = (TextView) findViewById(R.id.downloadProgress);
        prodressBar = (ProgressBarView) findViewById(R.id.loading);
        prodressBar.bringToFront();//置于最顶层
    }

    public void deal() {

        /**
         * 选择上传视频
         */
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 选择视频
                 */
                ImagePicker.getInstance()
                        .setTitle("选择视频")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(false)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
//                        .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(MainActivity.this, REQUEST_SELECT_IMAGES_CODE);
            }
        });


        /**
         * 上传文件
         */
        startUpload.setOnClickListener(new View.OnClickListener() {
            String PostUrl = "http://139.159.154.117:8083/upload/uploadStudent";

            @Override
            public void onClick(View v) {
                prodressBar.show();
                FileHttpUtil.UploadFile(path, PostUrl, new UploadProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                prodressBar.settitle("正在上传  " + progress + "%");
                            }
                        });

                    }
                }).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                prodressBar.dismiss();
                                Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                prodressBar.dismiss();
                                Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });


        /**
         * 下载文件
         */
        startDownload.setOnClickListener(new View.OnClickListener() {
            String GettUrl = "http://139.159.154.117:8083/upload/video/teacher/eaa48b7731694877aa55d68f74fece4b.mp4";
            @Override
            public void onClick(View view) {
                FileHttpUtil.downloadFile("MyUtils",GettUrl, new DownloadProgressListener() {
                    @Override
                    public void onDownloadSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                                prodressBar.dismiss();//隐藏对话框
                            }
                        });
                    }

                    @Override
                    public void onDownloadFailure() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgressUpdate(int progress) {
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                prodressBar.show();
                                prodressBar.settitle("正在下载  " + progress + "%");
                            }
                        });
                    }
                });
            }
        });

    }

    /**
     * 选择视频回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            List<String> imagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            path = imagePaths.get(0);
            System.out.println("路径为：" + path);
            filePath.setText(path);
        }
    }
}
