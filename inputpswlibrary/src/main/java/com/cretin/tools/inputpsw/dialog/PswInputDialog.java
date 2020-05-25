package com.cretin.tools.inputpsw.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cretin.tools.inputpsw.CommonAdapter;
import com.cretin.tools.inputpsw.R;
import com.cretin.tools.inputpsw.model.KeybordModel;
import com.cretin.tools.inputpsw.view.PswView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主要是确认对话框 默认有布局 只需要设置文字和事件监听
 */
public class PswInputDialog extends Dialog {
    private View contentView;
    private Context mContext;

    private OnPopWindowClickListener listener;

    //默认输入的密码长度是6位
    private int mPswCount = 6;
    private String mCurrPsw = "";

    //自定义View之密码框
    private PswView pswView;
    private TextView tvForgetPsw;
    private ImageView ivClose;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
        }
    };

    public PswInputDialog(Context context) {
        super(context, R.style.Theme_AppCompat_Light_Dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        contentView = View.inflate(mContext, R.layout.layout_popwindow_dialog_input_psw, null);
        setContentView(contentView);

        initViewInputPsw();
    }

    /**
     * 设置密码长度默认6位
     *
     * @param pswCount
     */
    public void setPswCount(int pswCount) {
        if (pswView != null && pswCount >= 3 && pswCount <= 9) {
            this.mPswCount = pswCount;
            pswView.setPsw_count(pswCount);
        }
    }

    /**
     * 显示
     */
    public void showPswDialog() {
        show();
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        getWindow().setGravity(Gravity.BOTTOM);
    }


    private void initViewInputPsw() {
        GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
        pswView = (PswView) contentView.findViewById(R.id.pswView);
        tvForgetPsw = contentView.findViewById(R.id.tv_forget_psw);
        ivClose = contentView.findViewById(R.id.iv_close);
        pswView.setPsw_count(mPswCount);
        String[] keys = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "delete"};
        String[] key_engs = new String[]{"", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ", "", "", "delete"};
        //构造数据
        List<KeybordModel> list = new ArrayList<>();
        for (int i = 0; i < key_engs.length; i++) {
            KeybordModel m = new KeybordModel();
            m.setKey(keys[i]);
            m.setKeyEng(key_engs[i]);
            list.add(m);
        }
        //实例化适配器
        GirdViewAdapter adapter = new GirdViewAdapter(mContext, list, R.layout.item_gridview_keyboard);
        gridView.setAdapter(adapter);
        initEvent(gridView, list, pswView);
    }

    public void hideForgetPswClickListener() {
        if (tvForgetPsw != null) {
            tvForgetPsw.setVisibility(View.GONE);
        }
    }

    public void setOnForgetPswClickListener(View.OnClickListener listener) {
        if (tvForgetPsw != null && listener != null) {
            tvForgetPsw.setOnClickListener(listener);
        }
    }

    //给键盘设置事件监听
    private void initEvent(GridView gridView, final List<KeybordModel> list, final PswView pswView) {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //对空按钮不作处理
                if (i != 9) {
                    if (i == 11) {
                        //删除
                        if (mCurrPsw.length() > 0)
                            mCurrPsw = mCurrPsw.substring(0, mCurrPsw.length() - 1);
                        pswView.setDatas(mCurrPsw);
                        listener.onPopWindowClickListener(mCurrPsw, false);
                    } else {
                        //非删除按钮
                        mCurrPsw += list.get(i).getKey();
                        //绘制当前密码
                        pswView.setDatas(mCurrPsw);
                        //当长度等于最大长度 表示密码输入完毕 dimiss 接口回调
                        if (mCurrPsw.length() == mPswCount) {
                            if (listener != null)
                                listener.onPopWindowClickListener(mCurrPsw, true);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(0);
                                }
                            }, 200);
                        } else {
                            if (listener != null)
                                listener.onPopWindowClickListener(mCurrPsw, false);
                        }
                    }
                }

            }
        });
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
            if (item.getKey().equals("delete")) {
                holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                holder.getView(R.id.rela_item).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_bg_selector_gray));
                holder.getView(R.id.ll_keys).setVisibility(View.INVISIBLE);
            } else if (TextUtils.isEmpty(item.getKey())) {
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

    public void setListener(OnPopWindowClickListener listener) {
        this.listener = listener;
    }
}
