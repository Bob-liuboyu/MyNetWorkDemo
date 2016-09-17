# MyNetWorkDemo
app 错误页面，空数据页面，网络异常页面

当页面加载数据失败或者数据为空，我们应该怎么办？


体验良好的APP都会做相应的处理。

比如网络异常，会显示一个网络异常页面，提示用户去检查网络；

数据为空时，出现一个温馨的空页面，引导用户去创建数据等；

这些方法各大app都在使用，但是怎么发开？每一个页面都写一个空页、面错误页面和loading页面吗？那也太恶心了吧。

1、每个页面都include 一个公用的空页面/错误页面，当出现异常的时候动态的 visible/gone，想想都恶心，因为每个页面都会出现这种情况，而且每个页面还需要去处理相应的逻辑，工作量非常大，不利于代码的整洁

2、每当出现异常的时候，动态的去向当前的页面 addview，这样做会有很大的局限性，跟布局必须是相对布局，或者是帧布局，而且也要处理很多次相同的逻辑

经过一段时间的苦想，想出了下面的解决方案，打造简单灵活的支持Activity/Fragment 空页面及错误页面：

先简单的看一下效果：

![屏幕快照 2016-08-04 下午7.39.11.png](http://upload-images.jianshu.io/upload_images/1959357-fb8814136bfefc86.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![屏幕快照 2016-08-04 下午7.39.34.png](http://upload-images.jianshu.io/upload_images/1959357-c6d15cbcf8e28772.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![屏幕快照 2016-08-04 下午7.40.04.png](http://upload-images.jianshu.io/upload_images/1959357-8014c127d5c855f6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#####1、网络异常情况:
这里我做的比较简单，只是用广播动态的去监听当前的网络状况，当没有网络的时候，通过 setcontentview 切换当前 activity的页面，当有网络的情况，再切换回去即可。
```
/** 
 * 注册监听网络的广播 
 */
private void registerReceiver() {   
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);   
    this.registerReceiver(myNetReceiver, filter);
}
```
```
/** * 处理广播 */
private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                Log.e("dddd","网络不可以用");
                //显示网络异常页面，具体方法下面赘述
                showNetError(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }else {
                Log.e("dddd","网络可以用");
                //隐藏网络异常页面，具体方法下面赘述
                dimissNetError();
            }
        }
    };
```
#####2、页面空数据情况:
当我们请求网络数据，数据为空的时候，显示空面
```
/**
 * 显示空页面
 */
showEmpty(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        
                    }
                });
```
```
/**
 * 空页面消失
 */
dimissEmpty();
```

看了上面的方法，是不是感觉还蛮像那么回事的，下面看看是怎么实现的（如果有什么不好的地方，尽管提出来）
1、先定义一个接口
```
public interface BaseView {
    /**
     * 展示加载框
     */
    void showLoading();

    /**
     * 取消加载框展示
     */
    void dismissLoading();

    /**
     * 显示错误信息
     */
    void showErrorMsg(String msg);

    /**
     * 显示空页面
     */
    void showEmpty(SwipeRefreshLayout.OnRefreshListener listener);

    /**
     * 空页面消失，恢复之前的页面
     */
    void dimissEmpty();

    /**
     * 显示网络错误页面
     */
    void showNetError(View.OnClickListener listener);

    /**
     * 网络错误页面，恢复之前的页面
     */
    void dimissNetError();
}
```

我的做法是，这些通用的东西，通通放到baseActivity 中
```
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
```
ps:
1、空页面中加入了SwipeRefreshLayout，实现下拉刷新页面，并将接口暴露出来
2、网络异常页面，将点击方法暴露出去
