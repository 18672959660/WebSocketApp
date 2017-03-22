package com.caojing.websocketapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.caojing.websocketapp.R;
import com.caojing.websocketapp.entity.FriendDBInfo;
import com.caojing.websocketapp.entity.FriendListInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

/**
 *  朋友列表适配器
 * Created by Administrator on 2017/3/2 0002.
 */

public class FriendListAdapter extends RecyclerArrayAdapter<FriendDBInfo> {


    public FriendListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendListViewHolder(parent, R.layout.friend_item);
    }

    class FriendListViewHolder extends BaseViewHolder<FriendDBInfo>{

        private TextView item_id;
        private TextView item_name;
        private TextView item_tip;
        private EmojiTextView item_msg;

        public FriendListViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            item_id=$(R.id.item_id);
            item_name=$(R.id.item_name);
            item_tip=$(R.id.item_tip);
            item_msg=$(R.id.item_msg);
        }

        @Override
        public void setData(FriendDBInfo data) {
            item_id.setText(data.getUserId());
            item_name.setText(data.getUserName());
            item_msg.setText(data.getMsg());
            if ("1".equals(data.getNewMsg())){
                //如果提示大于0，就提示用户有几条未读消息，后期可以根据tip的大小来设置有几条未读消息
                item_tip.setVisibility(itemView.VISIBLE);
            }else{
                //没有未读消息
                item_tip.setVisibility(itemView.GONE);
            }
        }
    }
}
