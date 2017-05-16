package com.jimmy.commonlibrary.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jimmy.commonlibrary.R;

@SuppressLint("Recycle")
public class TitleHeadLayout extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private View viewParent;

    /**
     * 左边部件
     */
    private TextView tvLeft;
    /**
     * 中间部件
     */
    private TextView tvTitle;
    /**
     * 右边第一部件
     */
    private TextView tvRightOne;
    /**
     * 右边第二部件
     */
    private TextView tvRightTwo;
    /**
     * 中间箭头
     */
    private ImageView ivDown;
    /**
     * 右边数字提示
     */
    private TextView tvMsgNumber;
    private LinearLayout lltTitle;
    private View titleLine;

    private OnTitleClickListener mOnTitleClickListener;
    private OnTitleItemClickListener mOnTitleItemClickListener;
    private OnTitleLeftClickListener mOnTitleLeftClickListener;
    private OnTitleRightClickListener mOnTitleRightClickListener;

    public TitleHeadLayout(Context context) {
        this(context, null);
    }

    public TitleHeadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void init() {
        viewParent = LayoutInflater.from(mContext).inflate(R.layout.base_head_layout_detail, this, true);

        tvLeft = (TextView) viewParent.findViewById(R.id.tv_left);
        tvTitle = (TextView) viewParent.findViewById(R.id.tv_title);
        ivDown = (ImageView) viewParent.findViewById(R.id.iv_down);
        lltTitle = (LinearLayout) viewParent.findViewById(R.id.llt_title);
        tvRightTwo = (TextView) viewParent.findViewById(R.id.tv_right_two);
        tvRightOne = (TextView) viewParent.findViewById(R.id.tv_right_one);
        tvMsgNumber = (TextView) viewParent.findViewById(R.id.tv_msg_number);
        titleLine = viewParent.findViewById(R.id.title_line);

        tvLeft.setOnClickListener(this);
        lltTitle.setOnClickListener(this);
        tvRightTwo.setOnClickListener(this);
        tvRightOne.setOnClickListener(this);
    }

    public TextView getLeftButton() {
        return tvLeft;
    }

    /***
     * 设置左边返回图片
     * */
    public void setLeftImageResource(int resId) {
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    /**
     * 设置左边文字内容 String
     */
    public void setLeftText(String text) {
        if (tvLeft != null) {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(text);
        }
    }

    /**
     * 设置左边文字内容  ID
     */
    public void setLeftText(int id) {
        if (tvLeft != null) {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(id);
        }
    }

    public void setLeftTextColorByRes(int rid) {
        if (tvLeft != null) {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setTextColor(getResources().getColorStateList(rid));
        }
    }

    /**
     * 隐藏左边部件
     */
    public void hideLeftImg() {
        if (null != tvLeft) {
            tvLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置title字体的颜色
     */
    public void setTitleTextColorByRes(int res) {
        if (tvTitle != null) {
            tvTitle.setTextColor(mContext.getResources().getColorStateList(res));
        }
    }

    /**
     * 设置title的文字
     */
    public void setTitleText(int id) {
        if (tvTitle != null) {
            tvTitle.setText(id);
        }
    }

    /**
     * 设置title的文字
     */
    public void setTitleText(String text) {
        if (tvTitle != null) {
            tvTitle.setText(text);
        }
    }

    public void setMsgNumber(int num) {
        if (tvMsgNumber != null) {
            if (num > 0) {
                tvMsgNumber.setText(String.valueOf(num));
                tvMsgNumber.setVisibility(View.VISIBLE);
            } else {
                tvMsgNumber.setVisibility(View.GONE);
            }
        }
    }

    public void setMsgNumberTextViewVisible(int visible) {
        if (tvMsgNumber != null) {
            tvMsgNumber.setVisibility(visible);
        }
    }

    public void setMsgNumberTextViewVisible(boolean flag) {
        if (tvMsgNumber != null) {
            if (flag) {
                tvMsgNumber.setVisibility(View.VISIBLE);
            } else {
                tvMsgNumber.setVisibility(View.GONE);
            }
        }
    }

    /***
     * 设置底线颜色
     * */
    public void setTitleLineColor(int colorRid) {
        View view = findViewById(R.id.title_line);
        view.setBackgroundResource(colorRid);
    }

    /**
     * 隐藏标题栏底部横线
     */
    public void hideTitleLine() {
        if (null != titleLine) {
            titleLine.setVisibility(View.GONE);
        }
    }

    /**
     * 显示标题栏底部横线
     */
    public void showTitleLine() {
        if (null != titleLine) {
            titleLine.setVisibility(View.VISIBLE);
        }
    }

    /***
     * 获取右边按钮
     * */
    public TextView getRightFirstBtn() {
        return tvRightOne;
    }

    /***
     * 获取右边按钮
     * */
    public TextView getRightSecondBtn() {
        return tvRightTwo;
    }

    /***
     * 设置右边图片
     * */
    public void setRightFirstImageResource(int resId) {
        tvRightOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        tvRightOne.setVisibility(View.VISIBLE);
    }

    /***
     * 设置右边图片
     * */
    public void setRightSecondImageResource(int resId) {
        tvRightTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        tvRightTwo.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏右边箭头图片
     */
    public void hideRightFirstImg() {
        if (null != tvRightOne) {
            tvRightOne.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏右边箭头图片
     */
    public void hideRightSecondImg() {
        if (null != tvRightTwo) {
            tvRightTwo.setVisibility(View.GONE);
        }
    }


    /**
     * 设置右边文字内容 String
     */
    public void setRightFirstText(String text) {
        if (tvRightOne != null) {
            tvRightOne.setVisibility(View.VISIBLE);
            tvRightOne.setText(text);
        }
    }

    /**
     * 设置右边文字内容 String
     */
    public void setRightSecondText(String text) {
        if (tvRightTwo != null) {
            tvRightTwo.setVisibility(View.VISIBLE);
            tvRightTwo.setText(text);
        }
    }

    /**
     * 设置右边文字内容  ID
     */
    public void setRightFirstText(int id) {
        if (tvRightOne != null) {
            tvRightOne.setVisibility(View.VISIBLE);
            tvRightOne.setText(id);
        }
    }

    /**
     * 设置右边文字内容  ID
     */
    public void setRightSecondText(int id) {
        if (tvRightTwo != null) {
            tvRightTwo.setVisibility(View.VISIBLE);
            tvRightTwo.setText(id);
        }
    }

    /**
     * 设置右边文字大小
     */
    public void setRightFirstTextSize(int sp) {
        if (tvRightOne != null) {
            tvRightOne.setTextSize(sp);
        }
    }

    /**
     * 设置右边文字大小
     */
    public void setRightSecondTextSize(int sp) {
        if (tvRightTwo != null) {
            tvRightTwo.setTextSize(sp);
        }
    }

    public void setRightFirstTextColor(int rid) {
        if (tvRightOne != null) {
            tvRightOne.setVisibility(View.VISIBLE);
            tvRightOne.setTextColor(rid);
        }
    }

    public void setRightSecondTextColor(int rid) {
        if (tvRightTwo != null) {
            tvRightTwo.setVisibility(View.VISIBLE);
            tvRightTwo.setTextColor(rid);
        }
    }

    public void setRightFirstTextColor(ColorStateList csl) {
        if (tvRightOne != null) {
            tvRightOne.setVisibility(View.VISIBLE);
            tvRightOne.setTextColor(csl);
        }
    }

    public void setRightSecondTextColor(ColorStateList csl) {
        if (tvRightTwo != null) {
            tvRightTwo.setVisibility(View.VISIBLE);
            tvRightTwo.setTextColor(csl);
        }
    }

    public void hideRightFirst() {
        tvRightOne.setVisibility(View.GONE);
    }

    public void hideRightSecond() {
        tvRightTwo.setVisibility(View.GONE);
    }

    public void showRightFirst() {
        tvRightOne.setVisibility(View.GONE);
    }

    public void showRightSecond() {
        tvRightTwo.setVisibility(View.GONE);
    }

    public void showMiddleArrow() {
        ivDown.setVisibility(View.VISIBLE);
    }

    public void hideMiddleArrow() {
        ivDown.setVisibility(View.GONE);
    }

    /**
     * 设置Title head的背景
     **/
    public void setHeadBackgroundResource(int resid) {
        if (viewParent != null) {
            viewParent.setBackgroundResource(resid);
        }
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.tv_left) {

            if (mOnTitleItemClickListener != null) {
                mOnTitleItemClickListener.onLeftClickListener();
            } else if (mOnTitleLeftClickListener != null) {
                mOnTitleLeftClickListener.onLeftClickListener();
            } else if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        } else if (i == R.id.tv_right_two) {
            if (mOnTitleItemClickListener != null) {
                mOnTitleItemClickListener.onRightTwoClickListener();
            } else if (mOnTitleRightClickListener != null) {
                mOnTitleRightClickListener.onRightTwoClickListener();
            }
        } else if (i == R.id.tv_right_one) {
            if (mOnTitleItemClickListener != null) {
                mOnTitleItemClickListener.onRightOneClickListener();
            } else if (mOnTitleRightClickListener != null) {
                mOnTitleRightClickListener.onRightOneClickListener();
            }
        } else if (i == R.id.llt_title) {
            if (mOnTitleItemClickListener != null) {
                mOnTitleItemClickListener.onTitleClickListener();
            } else if (mOnTitleClickListener != null) {
                mOnTitleClickListener.onTitleClickListener();
            }
        } else {
        }
    }

    /**
     * 设置titlelayout的各部件的点击事件
     * 设置了这个监听器，setOnTitleLeftClickListener()、setOnTitleClickListener()、setOnTitleRightClickListener()会失效
     *
     * @param mOnTitleItemClickListener
     */
    public void setOnTitleItemClickListener(
            OnTitleItemClickListener mOnTitleItemClickListener) {
        this.mOnTitleItemClickListener = mOnTitleItemClickListener;
    }

    /**
     * 设置左边部件点击监听
     * 如果设置了setOnTitleItemClickListener()，这个监听器会失效
     *
     * @param mOnTitleLeftClickListener
     */
    public void setOnTitleLeftClickListener(
            OnTitleLeftClickListener mOnTitleLeftClickListener) {
        this.mOnTitleLeftClickListener = mOnTitleLeftClickListener;
    }

    /**
     * 设置title文字点击监听
     * 如果设置了setOnTitleItemClickListener()，这个监听器会失效
     *
     * @param mOnTitleClickListener
     */
    public void setOnTitleClickListener(
            OnTitleClickListener mOnTitleClickListener) {
        this.mOnTitleClickListener = mOnTitleClickListener;
    }

    /**
     * 设置右边两个部件点击监听
     * 如果设置了setOnTitleItemClickListener()，这个监听器会失效
     *
     * @param mOnTitleRightClickListener
     */
    public void setOnTitleRightClickListener(
            OnTitleRightClickListener mOnTitleRightClickListener) {
        this.mOnTitleRightClickListener = mOnTitleRightClickListener;
    }

    public interface OnTitleItemClickListener {

        void onLeftClickListener();

        void onRightOneClickListener();

        void onRightTwoClickListener();

        void onTitleClickListener();
    }

    public interface OnTitleLeftClickListener {

        void onLeftClickListener();
    }

    public interface OnTitleClickListener {

        void onTitleClickListener();
    }

    public interface OnTitleRightClickListener {

        void onRightOneClickListener();

        void onRightTwoClickListener();
    }
}
