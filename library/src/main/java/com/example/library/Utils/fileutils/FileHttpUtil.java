package com.example.library.Utils.fileutils;

import android.os.Environment;

import com.example.library.Listener.DownloadProgressListener;
import com.example.library.Listener.UploadCurrentProgressListener;
import com.example.library.Listener.UploadProgressListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 文件上传/下载工具类
 */
public class FileHttpUtil {


    public static void downloadFile(String filename,final String url, final DownloadProgressListener downloadInterface) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadInterface.onDownloadFailure();
            }

            @Override
            public void onResponse(Call call, Response response) {
                //存储下载文件的目录
                String savePath = filename;
                //创建文件夹
                FileUtil.createFolder(savePath);
                //输入流
                InputStream inputStream = null;
                //输出流
                FileOutputStream fileOutputStream = null;
                //每次读取的字节大小（缓冲流）
                byte readBytes[] = new byte[2048];
                //读取的长度(写入的长度)
                int length = 0;
                try {
                    //将服务器获取的文件传给输入流
                    inputStream = response.body().byteStream();
                    //获取文件总长度
                    long sumLength = response.body().contentLength();
                    //当前已下载长度
                    long nowLength=0;
                    //创建File对象 用于输出流创建文件
                    File file = new File(Environment.getExternalStorageDirectory(), savePath + "/" + getNameFromUrl(url));
                    //创建输出流
                    fileOutputStream = new FileOutputStream(file);
                    //循环将读到输入流的内容写入文件输出流
                    while ((length=inputStream.read(readBytes))!=-1) {
                       fileOutputStream.write(readBytes,0,length);
                        //累计已下载长度
                       nowLength+=length;
                        //下载进度更新的回调接口
                     downloadInterface.onProgressUpdate((int)(nowLength*1.0f/sumLength*100));
                    }
                    //把缓冲区的数据强行输出
                    fileOutputStream.flush();
                    //下载成功回调接口
                    downloadInterface.onDownloadSuccess();
                } catch (FileNotFoundException e) {
                    //下载失败回调接口
                    downloadInterface.onDownloadFailure();
                    e.printStackTrace();
                } catch (IOException e) {
                    //下载失败回调接口
                    downloadInterface.onDownloadFailure();
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    /**
     * okhttp3上传文件
     * @param path  图片路径
     * @param url  图片上传请求接口
     * @return
     */
    public static Call UploadFile(String path, String url,final UploadProgressListener uploadInterface) {

        // 获取要上传的文件
        File mFile = new File(path);
        OkHttpClient client = new OkHttpClient.Builder().build();
        // 设置文件以及文件上传类型封装
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), mFile);
        // 文件上传的请求体封装
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", mFile.getName(), requestBody)
                .build();
        //上传流监听
        // 这是使用了加强的MultipartBody,通过代理该类的输出流操作，
        // 获取流的总大小和每次写入的大小
        ExMultipartBody exMultipartBody = new ExMultipartBody(multipartBody, new UploadCurrentProgressListener() {
            @Override
            public void onCurrentProgress(long totalLength, int currentLength) {
                uploadInterface.onProgress((int)(currentLength*1.0f/totalLength*100));//计算进度
            }
        });

        Request request = new Request.Builder()
                .url(url)
                .post(exMultipartBody)
                .build();

        Call call = client.newCall(request);

        return call;
    }

    /*
     * 方法名称：getNameFromUrl
     * 方法作用：从下载链接解析出文件名
     * 参数含义：下载链接
     * created by  livingwater  2018/8/16 下午8:51
     */
    private static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}
