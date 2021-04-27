package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.ActivityShowFullScreenImage;
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
    private final int IMAGE_SENDER = 2;
    private final int IMAGE_GETTER = 3;

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
        } else if (viewType == SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_sent, parent, false);
            return new SenderViewHolder(view);
        } else if (viewType == IMAGE_GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_message_recived, parent, false);
            return new ImageGetterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_message_sent, parent, false);
            return new ImageSenderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            SendMessageViewModel sendMessageViewModel = messageViewModels.get(position);
            int msgType = sendMessageViewModel.getMsgType();
            String userSender = sendMessageViewModel.getUserSender();
            if (msgType == BaseCodeClass.variableType.string_.getValue() || msgType == BaseCodeClass.variableType.int_.getValue()) {
                if (userSender.equals(MessageActivity.senderId)) {
                    GetterViewHolder getterViewHolder = (GetterViewHolder) holder;
                    getterViewHolder.holder(sendMessageViewModel);
                } else {
                    SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                    senderViewHolder.holder(sendMessageViewModel);
                }

            } else if (msgType == BaseCodeClass.variableType.Image_.getValue()){
                if (userSender.equals(MessageActivity.senderId)) {
                    ImageGetterViewHolder imageGetterViewHolder = (ImageGetterViewHolder) holder;
                    imageGetterViewHolder.holder(sendMessageViewModel);
                } else {
                    ImageSenderViewHolder imageSenderViewHolder = (ImageSenderViewHolder) holder;
                    imageSenderViewHolder.holder(sendMessageViewModel);
                }
            }


        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder:  " + e.getMessage());

        }

      /*  try {

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
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        SendMessageViewModel sendMessageViewModel = messageViewModels.get(position);
        int msgType = sendMessageViewModel.getMsgType();
        String userSender = sendMessageViewModel.getUserSender();

        if (msgType == BaseCodeClass.variableType.string_.getValue() || msgType == BaseCodeClass.variableType.int_.getValue()) {
            if (userSender.equals(MessageActivity.senderId)) {
                return GETTER;
            } else {
                return SENDER;
            }

        } else if (msgType == BaseCodeClass.variableType.Image_.getValue()){
            if (userSender.equals(MessageActivity.senderId)) {
                return IMAGE_GETTER;
            } else {
                return IMAGE_SENDER;
            }
        }else return 5;


      /*  if (messageViewModels.get(position).getUserSender().equals(MessageActivity.senderId)) {
            return GETTER;
        } else {
            return SENDER;
        }*/
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

    public class ImageGetterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMessageRecived;
        TextView txtImageMessageRecived, txtImageMessageTimeRecived;
        ConstraintLayout imgMsgRecParent;

        public ImageGetterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMessageRecived = itemView.findViewById(R.id.img_layout_imageMessageRecived);
            txtImageMessageRecived = itemView.findViewById(R.id.txt_layout_imageMessageRecived);
            txtImageMessageTimeRecived = itemView.findViewById(R.id.txtTime_layout_imageMessageRecived);
            imgMsgRecParent = itemView.findViewById(R.id.constraint_layout_imgMsgRecive_parent);

        }

        void holder(SendMessageViewModel messageData) {
            Glide.with(mContext).load(generateUrl(Integer.parseInt(messageData.getId()))).placeholder(R.drawable.placeholder).into(imgMessageRecived);
            txtImageMessageRecived.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtImageMessageTimeRecived.setText(time);

            imgMessageRecived.setOnClickListener(v -> showFullScreenImage(messageData.getId()));
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

    public class ImageSenderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMessageSend, imgMessageTick;
        TextView txtImageMessageSend, txtImageMessageTimeSend;
        ConstraintLayout imgMsgSendParent;

        public ImageSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMessageSend = itemView.findViewById(R.id.img_layout_imageMessageSent);
            imgMessageTick = itemView.findViewById(R.id.iv_imageMessage_sent_tick);
            txtImageMessageSend = itemView.findViewById(R.id.txt_layout_imageMessageSent);
            txtImageMessageTimeSend = itemView.findViewById(R.id.txtTime_layout_imageMessageSent);
            imgMsgSendParent = itemView.findViewById(R.id.constraint_layout_imgMsgSend_parent);
        }

        void holder(SendMessageViewModel messageData) {
            Glide.with(mContext).load(generateUrl(Integer.parseInt(messageData.getId()))).placeholder(R.drawable.placeholder).into(imgMessageSend);
            txtImageMessageSend.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtImageMessageTimeSend.setText(time);
            imgMsgSendParent.setOnClickListener(v -> showFullScreenImage(messageData.getId()));


            switch (messageData.getStatus()) {
                case "1":
                    imgMessageTick.setImageResource(R.drawable.ic_tick);
                    break;
                case "2":
                    imgMessageTick.setImageResource(R.drawable.ic_tick_done);
                    break;
                case "3":
                    imgMessageTick.setImageResource(R.drawable.ic_tick_seen);
                    break;
            }
        }

    }


    public void updateMessage(List<SendMessageViewModel> _messageViewModels) {
        messageViewModels = _messageViewModels;
        notifyDataSetChanged();
    }

    private String generateUrl(int chatId) {
        String url = baseCodeClass.pBASE_URL + "User/DownloadCahtFile?Chatid=" + chatId;
        return url;


    }

    private void showFullScreenImage(String chatId){
        Intent intent= new Intent(mContext,ActivityShowFullScreenImage.class);
        intent.putExtra("image_url",generateUrl(Integer.parseInt(chatId)));
        mContext.startActivity(intent);
    }

}
