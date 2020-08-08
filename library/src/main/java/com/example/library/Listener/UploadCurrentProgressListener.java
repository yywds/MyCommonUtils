package com.example.library.Listener;

/**
 * 上传进度接口
 */
public interface UploadCurrentProgressListener {
    /**
     * 上传进度
     */
    void onCurrentProgress(long totalLength, int currentLength);
}
