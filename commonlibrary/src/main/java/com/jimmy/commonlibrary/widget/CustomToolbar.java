package com.jimmy.commonlibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jimmy.commonlibrary.R;

import butterknife.ButterKnife;


/**
 *
 */
public class CustomToolbar extends Toolbar{

    private TextView mTitleText;

    private Paint mPaint;
    private boolean isDrawBottomLine = true;

    public CustomToolbar(Context context) {
        super(context);
        initPaint();
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.divider));
    }


    public boolean isDrawBottomLine() {
        return isDrawBottomLine;
    }

    public void setDrawBottomLine(boolean drawBottomLine) {
        isDrawBottomLine = drawBottomLine;
        invalidate();
    }

    @Override
    public void setTitle(@StringRes int resId) {
        CharSequence title = getResources().getText(resId);
        setTitle(title);
    }

    @Override
    public void setTitle(CharSequence title) {
        ensureTitleTextView();
        super.setTitle("");
        mTitleText.setText(title);
    }

    private void ensureTitleTextView(){
        if (mTitleText == null){
            mTitleText = ButterKnife.findById(this, R.id.toolbar_title);
        }
    }

    public void setTitleTextColor(int color){
        ensureTitleTextView();
        mTitleText.setTextColor(color);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isDrawBottomLine)
            canvas.drawLine(0, getMeasuredHeight() - mPaint.getStrokeWidth(), getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }
}
