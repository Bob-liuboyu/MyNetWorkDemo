package app.demo.networkdemo.mynetworkdemo.errorfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.demo.networkdemo.mynetworkdemo.BaseFragment;
import app.demo.networkdemo.mynetworkdemo.R;

/**
 * 网络异常 Fragment
 * Created by Mr.Z on 16/8/4.
 */
public class NetErrorFragment extends BaseFragment {
    private LinearLayout netErrLinear;
    private View.OnClickListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.net_error, null);
        netErrLinear = (LinearLayout) contentView.findViewById(R.id.netlayout);
        netErrLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(view);
                }
            }
        });
        return contentView;
    }

    /**
     * 重新加载
     * @param listener
     */
    public void setOnRefresh(View.OnClickListener listener){
       this.listener = listener;
    }
}
