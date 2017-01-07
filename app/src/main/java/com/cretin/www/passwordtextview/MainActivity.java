package com.cretin.www.passwordtextview;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.cretin.www.passwordtextview.popwindow.SelectPopupWindow;

public class MainActivity extends AppCompatActivity implements SelectPopupWindow.OnPopWindowClickListener{
    private SelectPopupWindow menuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //打开输入密码的对话框
    public void inoutPsw(View view){
        menuWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if(complete)
            Toast.makeText(this, "您输入的密码是"+psw, Toast.LENGTH_SHORT).show();
    }
}
