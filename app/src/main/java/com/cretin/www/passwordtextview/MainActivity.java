package com.cretin.www.passwordtextview;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.tools.inputpsw.dialog.PswInputDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //记录用户选择的密码位数
    private int pswCount = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<TextView> viewList = new ArrayList<>();
        viewList.add((TextView) findViewById(R.id.tv_3));
        viewList.add((TextView) findViewById(R.id.tv_4));
        viewList.add((TextView) findViewById(R.id.tv_5));
        viewList.add((TextView) findViewById(R.id.tv_6));
        viewList.add((TextView) findViewById(R.id.tv_7));
        viewList.add((TextView) findViewById(R.id.tv_8));
        viewList.add((TextView) findViewById(R.id.tv_9));

        for (int i = 0; i < viewList.size(); i++) {
            final int finalI = i;
            viewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pswCount = finalI + 3;
                    for (TextView textView : viewList) {
                        textView.setBackgroundResource(R.drawable.shape_round_gray);
                        textView.setTextColor(Color.parseColor("#999999"));
                    }
                    viewList.get(finalI).setBackgroundResource(R.drawable.shape_round_orange);
                    viewList.get(finalI).setTextColor(Color.parseColor("#E98A16"));
                }
            });
        }
    }

    //打开输入密码的对话框
    public void inoutPsw(View view) {
        //获取配置信息
        SwitchCompat switch_btn = findViewById(R.id.switch_btn);
        PswInputDialog pswInputDialog = new PswInputDialog(this);
        pswInputDialog.showPswDialog();
        if (!switch_btn.isChecked()) {
            //隐藏忘记密码的入口
            pswInputDialog.hideForgetPswClickListener();
        }
        //设置忘记密码的点击事件
        pswInputDialog.setOnForgetPswClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "您点击了忘记密码", Toast.LENGTH_SHORT).show();
            }
        });
        //设置密码长度
        pswInputDialog.setPswCount(pswCount);
        pswInputDialog.setListener(new PswInputDialog.OnPopWindowClickListener() {
            @Override
            public void onPopWindowClickListener(String psw, boolean complete) {
                if (complete)
                    Toast.makeText(MainActivity.this, "你输入的密码是：" + psw, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
