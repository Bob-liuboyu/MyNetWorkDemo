package app.demo.networkdemo.mynetworkdemo.dialog;


import app.demo.networkdemo.mynetworkdemo.BaseActivity;
import app.demo.networkdemo.mynetworkdemo.R;

/**
 * Created by Mr.Z on 16/8/8.
 */
public class WaitDialog extends MyBasicDialog {
    public WaitDialog(BaseActivity baseActivity) {
        super(baseActivity);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_wait);
    }
}
