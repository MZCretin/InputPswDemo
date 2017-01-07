package com.cretin.www.passwordtextview.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.cretin.www.passwordtextview.R;
import com.cretin.www.passwordtextview.adapter.CommonAdapter;
import com.cretin.www.passwordtextview.model.KeybordModel;
import com.cretin.www.passwordtextview.view.PswView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by cretin on 16/3/4.
 */
public class SelectPopupWindow extends PopupWindow implements View.OnClickListener {
    private View mMenuView;
    private OnPopWindowClickListener listener;
    //当前密码
    private String mCurrPsw = "";
    //默认输入的密码长度是6位
    private int mPswCount = 6;
    //自定义View之密码框
    private PswView pswView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
        }
    };

    public SelectPopupWindow(Activity context, OnPopWindowClickListener listener) {
        initView(context, listener);
    }

    private void initView(Activity context, OnPopWindowClickListener listener) {
        this.listener = listener;
        initViewInputPsw(context);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dialog_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = ( int ) event.getY();
                if ( event.getAction() == MotionEvent.ACTION_UP ) {
                    if ( y < height ) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void initViewInputPsw(Activity context) {
        LayoutInflater inflater = ( LayoutInflater ) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_popwindow_dialog_input_psw, null);
        GridView gridView = ( GridView ) mMenuView.findViewById(R.id.gridView);
        pswView = ( PswView ) mMenuView.findViewById(R.id.pswView);
        String[] keys = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "delete"};
        String[] key_engs = new String[]{"", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ", "", "", "delete"};
        //构造数据
        List<KeybordModel> list = new ArrayList<>();
        for ( int i = 0; i < key_engs.length; i++ ) {
            KeybordModel m = new KeybordModel();
            m.setKey(keys[i]);
            m.setKeyEng(key_engs[i]);
            list.add(m);
        }
        //实例化适配器
        GirdViewAdapter adapter = new GirdViewAdapter(context, list, R.layout.item_gridview_keyboard);
        gridView.setAdapter(adapter);
        initEvent(gridView, list, pswView);
    }

    //给键盘设置事件监听
    private void initEvent(GridView gridView, final List<KeybordModel> list, final PswView pswView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //对空按钮不作处理
                if ( i != 9 ) {
                    if ( i == 11 ) {
                        //删除
                        if ( mCurrPsw.length() > 0 )
                            mCurrPsw = mCurrPsw.substring(0, mCurrPsw.length() - 1);
                        pswView.setDatas(mCurrPsw);
                        listener.onPopWindowClickListener(mCurrPsw, false);
                    } else {
                        //非删除按钮
                        mCurrPsw += list.get(i).getKey();
                        //绘制当前密码
                        pswView.setDatas(mCurrPsw);
                        //当长度等于最大长度 表示密码输入完毕 dimiss 接口回调
                        if ( mCurrPsw.length() == mPswCount ) {
                            listener.onPopWindowClickListener(mCurrPsw, true);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(0);
                                }
                            },200);
                        } else {
                            listener.onPopWindowClickListener(mCurrPsw, false);
                        }
                    }
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        listener.onPopWindowClickListener(mCurrPsw, false);
        dismiss();
    }


    public interface OnPopWindowClickListener {
        void onPopWindowClickListener(String psw, boolean complete);
    }

    class GirdViewAdapter extends CommonAdapter<KeybordModel> {

        public GirdViewAdapter(Context context, List<KeybordModel> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolders holder, KeybordModel item, int position) {
            if ( item.getKey().equals("delete") ) {
                holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                holder.getView(R.id.rela_item).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_bg_selector_gray));
                holder.getView(R.id.ll_keys).setVisibility(View.INVISIBLE);
            } else if ( TextUtils.isEmpty(item.getKey()) ) {
                holder.getView(R.id.iv_delete).setVisibility(View.GONE);
                holder.getView(R.id.rela_item).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_bg_selector_gray));
                holder.getView(R.id.ll_keys).setVisibility(View.INVISIBLE);
            } else {
                holder.getView(R.id.rela_item).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_bg_selector));
                holder.getView(R.id.iv_delete).setVisibility(View.GONE);
                holder.getView(R.id.ll_keys).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_key, item.getKey());
                holder.setText(R.id.tv_key_eng, item.getKeyEng());
            }
        }
    }

    public int getmPswCount() {
        return mPswCount;
    }

    public void setmPswCount(int mPswCount) {
        this.mPswCount = mPswCount;
        if(pswView!=null)
            pswView.setmPsw_count(mPswCount);
    }
}
