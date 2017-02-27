package com.jimmy.account.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.jimmy.commonlibrary.utils.LogUtils;

import org.apache.commons.logging.Log;

import java.util.List;

/**
 * Created by chenjiaming1 on 2017/2/27.
 */

public class RobService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                //界面点击
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                //界面文字改动
                break;

            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
//                handleNotification(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                String className = event.getClassName().toString();
                if (className.equals("com.tencent.mm.ui.LauncherUI")) {
//                    getPacket();
                    Toast.makeText(getApplicationContext(),"LauncherUI",Toast.LENGTH_SHORT).show();

                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
//                    openPacket();
//                    getPacket();
                    goInfo();

                    Toast.makeText(getApplicationContext(),"LuckyMoneyReceiveUI",Toast.LENGTH_SHORT).show();

                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {


                    Toast.makeText(getApplicationContext(),"LuckyMoneyDetailUI",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 模拟点击,拆开红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("@id/bi4");
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void goInfo() {

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        AccessibilityNodeInfo node = recycle(rootNode);

//        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//
//        if (nodeInfo != null) {
//
//            //为了演示,直接查看了红包控件的id
//            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("@id/bgm");
//            nodeInfo.recycle();
//            for (AccessibilityNodeInfo item : list) {
//                Toast.makeText(getApplicationContext(),"Info-"+ item.getText(),Toast.LENGTH_SHORT).show();
//
//            }
//        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getInfo() {

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        AccessibilityNodeInfo node = recycle(rootNode);

        if (rootNode != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByViewId("@id/bi4");
            rootNode.recycle();
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }


    }

    /**
     * 模拟点击,打开抢红包界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        AccessibilityNodeInfo node = recycle(rootNode);

        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        AccessibilityNodeInfo parent = node.getParent();
        while (parent != null) {
            if (parent.isClickable()) {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            parent = parent.getParent();
        }
    }

    /**
     * 递归查找当前聊天窗口中的红包信息
     *
     * 聊天窗口中的红包都存在"领取红包"一词,因此可根据该词查找红包
     *
     * @param node
     */
    public AccessibilityNodeInfo recycle(AccessibilityNodeInfo node) {
        if (node.getChildCount() == 0) {
            if (node.getText() != null) {
                if ("查看领取详情".equals(node.getText().toString())) {

                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    recycle(node.getChild(i));
                }
            }
        }
        return node;
    }

    public AccessibilityNodeInfo recycle2(AccessibilityNodeInfo node) {

        if (node.getChildCount() == 0) {
            if (node.getText() != null) {
                if ("查看领取详情".equals(node.getText().toString())) {

                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    recycle(node.getChild(i));
                }
            }
        }
        return node;
    }


}
