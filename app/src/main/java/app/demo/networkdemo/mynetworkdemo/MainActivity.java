package app.demo.networkdemo.mynetworkdemo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import app.demo.networkdemo.mynetworkdemo.fragment.HomeFragment;


public class MainActivity extends BaseActivity {
    private long clickTime = 0; //记录第一次点击的时间

    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        switchFragment(R.id.contentView, new HomeFragment());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }
}
