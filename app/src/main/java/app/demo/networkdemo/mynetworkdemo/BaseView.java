package app.demo.networkdemo.mynetworkdemo;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * Created by Mr.Z on 16/7/12.
 */
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
