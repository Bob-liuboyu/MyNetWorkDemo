package app.demo.networkdemo.mynetworkdemo.errorfragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.demo.networkdemo.mynetworkdemo.BaseFragment;
import app.demo.networkdemo.mynetworkdemo.R;

/**
 * 空页面 Fragment
 * Created by Mr.Z on 16/8/4.
 */
public class EmptyFragment extends BaseFragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.empty, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (listener != null) {
                    listener.onRefresh();
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.removeAllViews();
                }
            }
        });
        return contentView;
    }

    /**
     * 重新加载
     *
     * @param listener
     */
    public void setOnRefresh(SwipeRefreshLayout.OnRefreshListener listener) {
        this.listener = listener;
    }
}
