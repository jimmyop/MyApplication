package com.jimmy.statelayout.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimmy.statelayout.R;


public class LoginViewHolder extends BaseHolder {

    public ImageView ivImg;

    public LoginViewHolder(View view) {
        tvTip = (TextView) view.findViewById(R.id.tv_message);
        ivImg = (ImageView) view.findViewById(R.id.iv_img);
    }


}
