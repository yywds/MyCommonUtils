package com.example.library.Listener;

/**
 * 上传进度计算接口
 */
public interface UploadProgressListener {
    /**
     * 上传进度计算
     */
    void onProgress(int progress);
}
