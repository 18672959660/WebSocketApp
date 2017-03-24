package com.caojing.websocketapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caojing.websocketapp.R;
import com.caojing.websocketapp.entity.LoginInfo;
import com.caojing.websocketapp.entity.MessageDBInfo;
import com.caojing.websocketapp.utils.TimerUtils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.vanniktech.emoji.EmojiTextView;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class MessageAdapter extends RecyclerArrayAdapter<MessageDBInfo> {
    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageListViewHolder(parent);
    }

    class MessageListViewHolder extends BaseViewHolder<MessageDBInfo> {
        private TextView text_time;
        private TextView text_name;
        private EmojiTextView text_message;
        private TextView text_name_who;
        private EmojiTextView text_message_who;
        private LinearLayout view_who;
        private LinearLayout view_me;

        public MessageListViewHolder(ViewGroup parent) {
            super(parent, R.layout.message_item);
            text_time = $(R.id.text_time);
            text_name = $(R.id.text_name);
            text_message = $(R.id.text_message);
            view_who = $(R.id.view_who);
            view_me = $(R.id.view_me);
            text_name_who = $(R.id.text_name_who);
            text_message_who = $(R.id.text_message_who);
        }

        @Override
        public void setData(MessageDBInfo data) {
            if (getAdapterPosition() >= 1) {
                String time = data.getTime().replace("T", " ");
                String starttime = getItem(getAdapterPosition() - 1).getTime().replace("T", " ");
                if (TimerUtils.getTimeExpend(starttime, time, "yyyy-MM-dd HH:mm:ss") < 2 * TimerUtils.ONE_MINUTE) {
                    text_time.setVisibility(View.GONE);
                } else {
                    text_time.setText(TimerUtils.getTimeStr(time, "yyyy-MM-dd HH:mm:ss"));
                    text_time.setVisibility(View.VISIBLE);
                }
            }
            if (LoginInfo.id.equals(getItem(getAdapterPosition()).getFromuid())) {
                view_who.setVisibility(View.GONE);
                view_me.setVisibility(View.VISIBLE);
                text_name.setText(data.getFromname());
                text_message.setText(data.getMsg());
            } else {
                view_who.setVisibility(View.VISIBLE);
                view_me.setVisibility(View.GONE);
                text_name_who.setText(data.getFromname());
                text_message_who.setText(data.getMsg());
            }
        }
    }
}
