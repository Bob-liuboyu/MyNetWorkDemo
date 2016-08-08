package app.demo.networkdemo.mynetworkdemo.dialog;

import android.app.Dialog;

import app.demo.networkdemo.mynetworkdemo.BaseActivity;
import app.demo.networkdemo.mynetworkdemo.R;


/**
 * Created by Mr.Z on 16/8/8.
 */
public class MyBasicDialog extends Dialog {

    protected BaseActivity baseActivity;

    public MyBasicDialog(BaseActivity baseActivity) {
        super(baseActivity, R.style.Dialog_Legend);
        this.baseActivity = baseActivity;
    }

    public MyBasicDialog(BaseActivity baseActivity, int theme) {
        super(baseActivity, theme);
        this.baseActivity = baseActivity;
    }
}
