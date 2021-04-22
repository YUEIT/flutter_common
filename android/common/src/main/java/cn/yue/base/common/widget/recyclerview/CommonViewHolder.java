package cn.yue.base.common.widget.recyclerview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Description : 通用ViewHolder
 * Created by yue on 2016/12/16
 */

public class CommonViewHolder<T> extends RecyclerView.ViewHolder{

    public static RecyclerView.ViewHolder getHolder(Context context, int id , ViewGroup root){
        View itemView = LayoutInflater.from(context).inflate(id, root, false);
        return new CommonViewHolder(itemView);
    }

    public CommonViewHolder(View itemView) {
        super(itemView);
    }

    public <V extends View> V getView(int id){

        if(itemView != null){
            View view = itemView.findViewById(id);
            return (V)view;
        }else{
            return null;
        }
    }

    public void setText(int id, String s){
        TextView t = ((TextView)getView(id));
        if (null != t){
            if (null == s){
                s = "";
            }
            t.setText(s.trim());
        }

    }

    public void setImageResource(int id, int resId){
        if(getView(id)!=null) {
            ((ImageView) getView(id)).setImageResource(resId);
        }
    }

    public void setBackgroundDrawable(int id, int resId){
        if(getView(id)!=null) {
            getView(id).setBackgroundResource(resId);
        }
    }

    public void setBackgroundColor(int id, int color){
        if(getView(id)!=null) {
            getView(id).setBackgroundColor(color);
        }
    }

    public void setOnClickListener(int id, View.OnClickListener l){
        if(getView(id)!=null) {
            getView(id).setOnClickListener(l);
        }
    }

    public void setOnLongClickListener(int id, View.OnLongClickListener l){
        if(getView(id)!=null) {
            getView(id).setOnLongClickListener(l);
        }
    }

    public void setOnTouchListener(int id, View.OnTouchListener l){
        if(getView(id)!=null) {
            getView(id).setOnTouchListener(l);
        }
    }


    public void setOnItemClickListener(final int position, final T t, final OnItemClickListener listener){
        if(itemView != null && listener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position, t);
                }
            });
        }
    }

    public void setOnItemLongClickListener(final int position, final T t, final OnItemLongClickListener listener){
        if(itemView != null && listener != null){
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(position, t);
                    return true;
                }
            });
        }
    }


    public interface OnItemClickListener<T>{
         void onItemClick(int position, T t);
    }

    public interface OnItemLongClickListener<T>{
        void onItemLongClick(int position, T t);
    }

}
