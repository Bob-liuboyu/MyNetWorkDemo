package app.demo.networkdemo.mynetworkdemo.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.demo.networkdemo.mynetworkdemo.BaseFragment;
import app.demo.networkdemo.mynetworkdemo.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Mr.Z on 16/8/8.
 */
public class HomeFragment extends BaseFragment {
    /**
     * actvity 空页面
     */
    @InjectView(R.id.btn_actvity_empty)
    Button btnActvityEmpty;
    /**
     * actvity 网络异常
     */
    @InjectView(R.id.btn_actvity_netError)
    Button btnActvityNetError;
    /**
     * fragment 空页面
     */
    @InjectView(R.id.btn_fragment_empty)
    Button btnFragmentEmpty;
    /**
     * fragment 网络异常
     */
    @InjectView(R.id.btn_fragment_netError)
    Button btnFragmentNetError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.inject(this, contentView);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.btn_actvity_empty, R.id.btn_actvity_netError, R.id.btn_fragment_empty, R.id.btn_fragment_netError})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_actvity_empty:
                baseActivity.showEmpty(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        baseActivity.dimissEmpty();
                    }
                });
                break;
            case R.id.btn_actvity_netError:
                baseActivity.showNetError(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        baseActivity.dimissNetError();
                    }
                });
                break;
            case R.id.btn_fragment_empty:
                baseActivity.switchEmptyFragment(R.id.contentView, new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        baseActivity.dissMissEmptyFragment();
                    }
                });
                break;
            case R.id.btn_fragment_netError:
                baseActivity.switchNetErrorFragment(R.id.contentView, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        baseActivity.disMissNetErrorFragment();
                    }
                });
                break;
        }
    }
}
