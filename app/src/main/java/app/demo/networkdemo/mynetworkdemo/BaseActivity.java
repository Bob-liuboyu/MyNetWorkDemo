package app.demo.networkdemo.mynetworkdemo;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import app.demo.networkdemo.mynetworkdemo.dialog.DialogManager;
import app.demo.networkdemo.mynetworkdemo.dialog.MyBasicDialog;
import app.demo.networkdemo.mynetworkdemo.dialog.WaitDialog;
import app.demo.networkdemo.mynetworkdemo.errorfragment.EmptyFragment;
import app.demo.networkdemo.mynetworkdemo.errorfragment.NetErrorFragment;

/**
 * 基础 activity
 * Created by Mr.Z on 16/7/12.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    /**
     * 缓冲框
     */
    private MyBasicDialog myBasicDialog;
    /**
     * 下拉刷新
     */
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private LayoutInflater inflater;
    /**
     * 当前页面的布局
     */
    protected View layoutMain = null;
    /**
     * 空页面布局
     */
    protected View layout_empty = null;
    /**
     * 网络异常布局
     */
    protected View layout_net_error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = this.getLayoutInflater();
        layoutMain = inflater.inflate(setLayoutId(), null);
        layout_empty = inflater.inflate(R.layout.empty_activity, null);
        layout_net_error = inflater.inflate(R.layout.net_error_activity, null);
        setContentView(layoutMain);
        initContentView(savedInstanceState);
        registerReceiver();
    }

    /**
     * 显示缓冲框
     */
    @Override
    public void showLoading() {
        WaitDialog waitDialog = new WaitDialog(this);
        showDialog(waitDialog);
    }

    /**
     * 设置当前的布局id
     *
     * @return
     */
    public abstract int setLayoutId();

    /**
     * 初始化UI
     *
     * @param savedInstanceState
     */
    protected abstract void initContentView(Bundle savedInstanceState);

    /**
     * 缓冲框消失
     */
    @Override
    public void dismissLoading() {
        hideDialog();
    }

    /**
     * 错误提示
     */
    @Override
    public void showErrorMsg(String msg) {
        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle("温馨提示").
                setMessage(msg).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                create();
        alertDialog.show();
    }

    /**
     * 显示空页面
     */
    @Override
    public void showEmpty(SwipeRefreshLayout.OnRefreshListener listener) {
        setContentView(layout_empty);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    /**
     * 空页面消失
     */
    @Override
    public void dimissEmpty() {
        myHandler.sendEmptyMessage(3);
    }

    /**
     * 网络异常
     */
    @Override
    public void showNetError(View.OnClickListener listener) {
        setContentView(layout_net_error);
        LinearLayout root = (LinearLayout) findViewById(R.id.netlayout);
        root.setOnClickListener(listener);
    }

    /**
     * 隐藏网络异常
     */
    @Override
    public void dimissNetError() {
        setContentView(layoutMain);
    }


    /**
     * 显示对话框
     *
     * @param myBasicDialog
     */
    private void showDialog(MyBasicDialog myBasicDialog) {
        this.myBasicDialog = myBasicDialog;
        myHandler.sendEmptyMessage(2);
    }

    /**
     * 隐藏对话框
     */
    private void hideDialog() {
        myHandler.sendEmptyMessage(1);
    }


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://隐藏对话框
                    DialogManager.getInstance().hideDialog();
                    break;
                case 2://显示对话框
                    DialogManager.getInstance().showDialog(myBasicDialog);
                    break;
                case 3://刷新页面
                    mSwipeRefreshLayout.removeAllViews();
                    setContentView(layoutMain);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    /**
     * 切换 fragment
     *
     * @param contentId
     * @param fragment
     */
    public void switchFragment(int contentId, BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(contentId, fragment).addToBackStack(null).commit();
    }

    /**
     * 切换空 fragment
     *
     * @param contentId
     */
    public void switchEmptyFragment(int contentId, SwipeRefreshLayout.OnRefreshListener listener) {
        EmptyFragment emptyFragment = new EmptyFragment();
        emptyFragment.setOnRefresh(listener);
        switchFragment(contentId, emptyFragment);
    }

    /**
     * 隐藏空 fragment
     */
    public void dissMissEmptyFragment() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * 切换网络异常 fragment
     *
     * @param contentId
     */
    public void switchNetErrorFragment(int contentId, View.OnClickListener listener) {
        NetErrorFragment netErrorFragment = new NetErrorFragment();
        netErrorFragment.setOnRefresh(listener);
        switchFragment(contentId, netErrorFragment);
    }

    /**
     * 隐藏网络异常 fragment
     */
    public void disMissNetErrorFragment() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * 点击键盘以外部分，键盘消失
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 当前键盘的显示情况
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 注册监听网络的广播
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(myNetReceiver, filter);
    }


    /**
     * 处理广播
     */
    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                Log.e("dddd","网络不可以用");
                showNetError(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }else {
                Log.e("dddd","网络可以用");
                dimissNetError();
            }
        }
    };
}
