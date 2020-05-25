package com.cretin.tools.inputpsw;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addDatas(List<T> mList) {
        mDatas.addAll(mList);
        notifyDataSetChanged();
    }

    public void addData(T item) {
        if(item != null){
            mDatas.add(item);
            notifyDataSetChanged();
        }
    }

    public void removeData(T item) {
        if(item != null){
            mDatas.remove(item);
            notifyDataSetChanged();
        }
    }

    public void refreshDatas(List<T> mList) {
        mDatas.clear();
        mDatas.addAll(mList);
        notifyDataSetChanged();
    }
    public void clearDatas() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolders viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolders holder, T item, int position);

    private ViewHolders getViewHolder(int position, View convertView,
                                      ViewGroup parent) {
        return ViewHolders.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

    public static class ViewHolders {
        private final SparseArray<View> mViews;
        private int mPosition;
        private View mConvertView;

        private ViewHolders(Context context, ViewGroup parent, int layoutId,
                            int position) {
            this.mPosition = position;
            this.mViews = new SparseArray<View>();
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false);
            // setTag
            mConvertView.setTag(this);
        }

        /**
         * 拿到一个ViewHolder对象
         *
         * @param context
         * @param convertView
         * @param parent
         * @param layoutId
         * @param position
         * @return
         */
        public static ViewHolders get(Context context, View convertView,
                                      ViewGroup parent, int layoutId, int position) {
            if (convertView == null) {
                return new ViewHolders(context, parent, layoutId, position);
            }
            return (ViewHolders) convertView.getTag();
        }

        public View getConvertView() {
            return mConvertView;
        }

        /**
         * 通过控件的Id获取对于的控件，如果没有则加入views
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 为TextView设置字符串
         *
         * @param viewId
         * @param text
         * @return
         */
        public ViewHolders setText(int viewId, String text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @return
         */
        public ViewHolders setImageUrl(int viewId, String url) {
            ImageView view = getView(viewId);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @return
         */
        public ViewHolders setImageRes(int viewId, int res) {
            ImageView view = getView(viewId);
            return this;
        }

//	/**
//	 * 为ImageView设置图片
//	 *
//	 * @param viewId
//	 * @param drawableId
//	 * @return
//	 */
//	public ViewHolders setImageByUrl(int viewId, String url)
//	{
//		ImageLoader.getInstance(3, Type.LIFO).loadImage(url,
//				(ImageView) getView(viewId));
//		return this;
//	}

        public int getPosition() {
            return mPosition;
        }

    }
}
