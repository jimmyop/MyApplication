package com.jimmy.commonlibrary.widget.meituan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.jimmy.commonlibrary.R;


public class MeiTuanRefreshSecondStepView extends View {

    private Bitmap endBitmap;
    AnimationDrawable secondAnim;

    public MeiTuanRefreshSecondStepView(Context context, AttributeSet attrs,
                                        int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MeiTuanRefreshSecondStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeiTuanRefreshSecondStepView(Context context) {
        super(context);
        init();
    }

    private void init() {
        endBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pull_end_image_frame_05));
        setBackgroundResource(R.drawable.pull_to_refresh_second_anim);

    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        secondAnim = (AnimationDrawable) getBackground();
    }

    public void startAnimation() {
        //启动
        if (secondAnim != null) {
            //停止
            secondAnim.stop();
            secondAnim.start();
        }

    }

    public void stopAnimation() {
        //启动
        if (secondAnim != null) {
            //停止
            secondAnim.stop();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureWidth(widthMeasureSpec) * endBitmap.getHeight() / endBitmap.getWidth());
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = endBitmap.getWidth();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }
}