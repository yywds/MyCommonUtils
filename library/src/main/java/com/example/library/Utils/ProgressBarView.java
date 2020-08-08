package com.example.library.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.library.R;

/**
 * 上传/下载进度对话框
 */
public class ProgressBarView extends RelativeLayout {
    private TextView textView;
    private RelativeLayout relativeLayout;

    public ProgressBarView(Context context) {
        super(context);
        initView();
    }

    public ProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
       View view  =  LayoutInflater.from(getContext()).inflate(R.layout.progessbar,this);
       textView = view.findViewById(R.id.title);
       relativeLayout = view.findViewById(R.id.rl_progress);
       relativeLayout.bringToFront();

    }

    /**
     * 设置对话框文字
     * @param text 要显示的文字
     */
    public void settitle(String text){
        textView.setText(text);
    }

    /**
     * 显示对话框
     */
    public void show(){
        relativeLayout.setVisibility(VISIBLE);
    }

    /**
     * 隐藏对话框
     */
    public void dismiss(){
        relativeLayout.setVisibility(GONE);
    }

}
