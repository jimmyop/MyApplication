package com.jimmy.account.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

/**
 * Created by chenjiaming1 on 2017/2/27.
 */

public class RobService extends AccessibilityService {

    private int totalCount = 0;
    private int receCount = 0;

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
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                String className = event.getClassName().toString();
                if (className.equals("com.tencent.mm.ui.LauncherUI")) {

                    Toast.makeText(getApplicationContext(), "LauncherUI", Toast.LENGTH_SHORT).show();
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {

                    goInfo();
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {

                    getInfo();
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
        recycle(rootNode);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getInfo() {

        totalCount = 0;
        receCount = 0;

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        recycle2(rootNode);
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
     * <p>
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

            if (node.getClassName().toString().contains("ListView")) {

                Toast.makeText(getApplicationContext(), node.getChildCount() + "--AA--" , Toast.LENGTH_SHORT).show();

                node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                AccessibilityNodeInfo.

                if (node.getChildCount() > 0) {

                    AccessibilityNodeInfo header = node.getChild(0);

                    if (header != null && header.getChildCount() > 0) {

                        for (int i = 0; i < header.getChildCount(); i++) {

                            String str = header.getChild(i).getText().toString();

                            if (str.contains("/")) {
                                String[] arr = str.split("/");
                                String a = arr[0].substring(2, arr[0].length());
                                String b = arr[1].substring(0, arr[1].length() - 1);
                                try {
                                    totalCount = Integer.valueOf(b);
                                    receCount = Integer.valueOf(a);
//                                    Toast.makeText(getApplicationContext(), "一共派了" + totalCount + "个红包，领取了" + receCount + "个", Toast.LENGTH_SHORT).show();
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

                if (node.getChildCount() >= receCount) {

//                    Toast.makeText(getApplicationContext(), node.getChildCount() + "--AA--" + receCount, Toast.LENGTH_SHORT).show();

                    for (int i = 1; i < node.getChildCount(); i++) {

                    }

//                    recycle3(node);
                } else {

                }
            } else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (node.getChild(i) != null) {
                        recycle2(node.getChild(i));
                    }
                }
            }
        }
        return node;
    }

    public AccessibilityNodeInfo recycle3(AccessibilityNodeInfo node) {

        Toast.makeText(getApplicationContext(), "get info recycle3", Toast.LENGTH_SHORT).show();
        if (node.getChildCount() != 0) {

            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    recycle3(node.getChild(i));
                }
            }
        } else {
            if (node.getText() != null && node.getText().toString().contains("元")) {

                Toast.makeText(getApplicationContext(), "get info 元", Toast.LENGTH_SHORT).show();
            }
        }
        return node;
    }

}
