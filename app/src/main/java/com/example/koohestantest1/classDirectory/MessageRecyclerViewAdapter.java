package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.TimeUtils;

import java.util.List;

import com.example.koohestantest1.ViewModels.SendMessageViewModel;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SendMessageViewModel> messageViewModels;

    private String TAG = MessageRecyclerViewAdapter.class.getSimpleName();

    BaseCodeClass baseCodeClass = new BaseCodeClass();

    private final int SENDER = 0;
    private final int GETTER = 1;

    public MessageRecyclerViewAdapter(Context mContext, List<SendMessageViewModel> messageViewModels) {
        this.mContext = mContext;
        this.messageViewModels = messageViewModels;
    }


    @Override
    public int getItemCount() {
        return messageViewModels.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_recieved, parent, false);
            return new GetterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_sent, parent, false);
            return new SenderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {

            SendMessageViewModel currentMessage = messageViewModels.get(position);
            //check if message is received or sent
            if (currentMessage.getUserSender().equals(MessageActivity.senderId)) {
                //message is received:
                GetterViewHolder getterViewHolder = (GetterViewHolder) holder;
                getterViewHolder.holder(currentMessage);

            } else {
                //message is sent:
                SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                senderViewHolder.holder(currentMessage);
            }
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder:  " + e.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageViewModels.get(position).getUserSender().equals(MessageActivity.senderId)) {
            return GETTER;
        } else {
            return SENDER;
        }
    }


    public class GetterViewHolder extends RecyclerView.ViewHolder {
        TextView textReceived, timeReceived;

        public GetterViewHolder(@NonNull View itemView) {
            super(itemView);
            textReceived = itemView.findViewById(R.id.messageTextIn);
            timeReceived = itemView.findViewById(R.id.messageTimeIn);
        }

        void holder(SendMessageViewModel messageData) {
            textReceived.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            timeReceived.setText(time);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView timeSent, textSent;
        ImageView ivTick;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSent = itemView.findViewById(R.id.tv_message_sent_time);
            textSent = itemView.findViewById(R.id.tv_message_sent);
            ivTick = itemView.findViewById(R.id.iv_message_sent_tick);
        }

        void holder(SendMessageViewModel messageData) {

            textSent.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            timeSent.setText(time);



            switch (messageData.getStatus()) {
                case "1":
                    ivTick.setImageResource(R.drawable.ic_tick);
                    break;
                case "2":
                    ivTick.setImageResource(R.drawable.ic_tick_done);
                    break;
                case "3":
                    ivTick.setImageResource(R.drawable.ic_tick_seen);
                    break;
            }
        }
    }

    public void updateMessage(List<SendMessageViewModel> _messageViewModels) {
        messageViewModels = _messageViewModels;
        notifyDataSetChanged();
    }
}
