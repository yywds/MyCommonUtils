package com.example.library.Utils.fileutils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 创建文件夹工具类
 */
public class FileUtil {
    /**
     * 方法名称：CreateFolderFile
     * 参数含义：path：文件夹路径，FileName：文件名
     * 方法作用：创建文件夹以及文件
     * 返回值含义：返回值为0代表文件夹创建失败，文件创建失败，
     * 1代表文件夹创建成功，文件创建失败，2代表文件夹和文件都创建成功
     * 3代表文件夹，文件已存在
     */
    public static int CreateFolderFile(String path, String FileName) {


        // 返回值为0代表文件夹创建失败，文件创建失败，1代表文件夹创建成功，文件创建失败，
        // 2代表文件夹和文件都创建成功 3代表文件夹，文件已存在
        int result = 0;
        // 创建文件夹
        File folder = new File(Environment.getExternalStorageDirectory(), path);
        // 判断文件夹是否存在
        if (!folder.exists() && !folder.isDirectory()) {
            System.out.println("文件夹路径不存在，创建路径:" + folder.getAbsolutePath());
            folder.mkdirs();
            result = 1;
        } else {
            System.out.println("文件夹路径存在:" + folder.getAbsolutePath());
        }
        Log.i("folder", "CreateFolderFile: " + folder.getAbsolutePath());
        // 创建文件
        File txtFile = new File(folder.getAbsolutePath() + "/" + FileName);
        if (!txtFile.exists()) {
            try {
                txtFile.createNewFile();
                System.out.println("文件夹路径不存在，创建文件:" + folder.getAbsolutePath() + "/" + FileName);
                result = 2;
            } catch (IOException e) {
                System.out.println("创建时出错");
                e.printStackTrace();
            }
        } else {
            result = 3;
            System.out.println("文件已存在:" + folder.getAbsolutePath() + "/" + FileName);
        }
        return result;
    }

    /**
     * 方法名称： createFolder
     * 方法作用：创建文件夹
     * @param path ：路径
     * @return
     * 返回值类型：int
     * 返回值含义：0代表 文件夹已存在 1代表文件夹创建成功
     */
    public static int createFolder(String path) {
        int result=0;
        // 创建文件夹
        File folder = new File(Environment.getExternalStorageDirectory(),path);
        // 判断文件夹是否存在
        if (!folder.exists() && !folder.isDirectory()) {
            System.out.println("文件夹创建成功，路径是:" + folder.getPath());
            folder.mkdirs();
            result = 1;
        } else {
            System.out.println("文件夹已存在:" + folder.getPath());
        }
        return result;
    }
}
