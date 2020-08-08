package com.example.library.Listener;

/**
 * 下载接口
 */
public interface DownloadProgressListener {
    /**
     * 下载成功
     */
    void onDownloadSuccess();

    /**
     * 下载失败
     */
    void onDownloadFailure();

    /**
     * 下载进度
     * @param progress 当前下载进度
     */
    void onProgressUpdate(int progress);
}
