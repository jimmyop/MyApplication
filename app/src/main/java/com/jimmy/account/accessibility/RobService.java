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
//                Toast.makeText(getApplicationContext(),"TYPE_VIEW_SCROLLED",Toast.LENGTH_SHORT).show();
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                String className = event.getClassName().toString();
                if (className.equals("com.tencent.mm.ui.LauncherUI")) {

                    totalCount = 0;
                    receCount = 0;

                    Toast.makeText(getApplicationContext(), "LauncherUI", Toast.LENGTH_SHORT).show();
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {

                    goInfo();
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {

                    getInfo(event);
                } else if (className.equals("com.jimmy.account.activity.TestAccessibilityServiceActivity")) {

                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                    AccessibilityNodeInfo sourceNode = event.getSource();

                    getListViewNode(rootNode);

                }
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void goInfo() {

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        recycle(rootNode);
    }

    /**
     * 递归查找当前聊天窗口中的红包信息
     * <p>
     * 聊天窗口中的红包都存在"查看领取详情"一词,因此可根据该词查找红包
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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getInfo(AccessibilityEvent event) {

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        AccessibilityNodeInfo sourceNode = event.getSource();

        getListViewNode(sourceNode);
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取页面中的ListView
     */
    private AccessibilityNodeInfo getListViewNode(AccessibilityNodeInfo node) {

        if (node != null && node.getChildCount() > 0) {

            if (node.getClassName().toString().contains("ListView")) {

                dealListViewInfo(node);

            } else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (node.getChild(i) != null) {
                        getListViewNode(node.getChild(i));
                    }
                }
            }
        }
        return null;
    }

    /**
     * 处理ListView中的信息
     */
    private void dealListViewInfo(AccessibilityNodeInfo node) {

        if (node != null && node.getChildCount() > 0) {

            if (totalCount == 0 && receCount == 0) {
                AccessibilityNodeInfo header = node.getChild(0);
                dealListViewHeaderInfo(header);
                Toast.makeText(getApplicationContext(), totalCount + "--" + receCount, Toast.LENGTH_LONG).show();

            }

            dealListViewContentInfo(node);
        }
    }

    /**
     * 处理ListView Header中的信息
     */
    private void dealListViewHeaderInfo(AccessibilityNodeInfo header) {

        if (header != null && header.getChildCount() > 0) {
            for (int i = 0; i < header.getChildCount(); i++) {

                if (header.getChild(i).getText() != null) {
                    String str = header.getChild(i).getText().toString();
                    if (str.contains("/")) {
                        String[] arr = str.split("/");
                        String a = arr[0].substring(2, arr[0].length());
                        String b = arr[1].substring(0, arr[1].length() - 1);
                        try {
                            totalCount = Integer.valueOf(b);
                            receCount = Integer.valueOf(a);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理ListView 列表中的信息
     */
    private void dealListViewContentInfo(AccessibilityNodeInfo node) {

        if (node != null && node.getChildCount() > 1) {

            for (int i = 1; i < node.getChildCount(); i++) {

                dealListViewItemInfo(node.getChild(i));
            }
        }
    }

    /**
     * 处理ListView item中的信息
     */
    private void dealListViewItemInfo(AccessibilityNodeInfo item) {

        if (item != null && item.getChildCount() > 0) {

            AccessibilityNodeInfo nodeName = findViewById(item, "com.tencent.mm:id/bi7");
            AccessibilityNodeInfo nodePrice = findViewByText("元", item);

            String name = null;
            String price = null;
            if (nodeName != null && nodeName.getText() != null) {
                name = nodeName.getText().toString();
            }
            if (nodePrice != null && nodePrice.getText() != null) {
                price = nodePrice.getText().toString();
            }

            if (name != null && price != null) {
                Toast.makeText(getApplicationContext(), name + "-" + price, Toast.LENGTH_LONG).show();
            } else {

                if (name == null) {
                    Toast.makeText(getApplicationContext(), "name null", Toast.LENGTH_LONG).show();
                }
                if (price == null) {
                    Toast.makeText(getApplicationContext(), "price null", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public AccessibilityNodeInfo findViewById(AccessibilityNodeInfo item, String id) {

        if (item == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = item.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    public AccessibilityNodeInfo findViewByText(String text, AccessibilityNodeInfo item) {
        if (item == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = item.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }
}
