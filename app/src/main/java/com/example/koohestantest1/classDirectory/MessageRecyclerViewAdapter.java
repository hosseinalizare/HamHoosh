package com.example.koohestantest1.classDirectory;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.koohestantest1.ActivityShowFullScreenImage;
import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.MyStoreOrderDetail;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.Utils.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.SendMessageVM;
import com.mikhaellopez.circularimageview.CircularImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public List<SendMessageViewModel> messageViewModels;
    private String TAG = MessageRecyclerViewAdapter.class.getSimpleName();
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    private Bitmap bitmap;
    private String caption;
    private SendMessageVM sendMessageVM;
    private String docName;
    private String musicName;
    private File myDir;
    private String root;
    private File myFile;
    private final int SENDER = 0;
    private final int GETTER = 1;
    private final int IMAGE_SENDER = 2;
    private final int IMAGE_GETTER = 3;
    private final int DOC_SENDER = 4;
    private final int DOC_GETTER = 5;
    private final int MSG_SENDER_REPLY = 6;
    private final int MSG_GETTER_REPLY = 7;
    private final int IMG_MSG_SENDER_REPLY = 8;
    private final int IMG_MSG_GETTER_REPLY = 9;
    private final int DOC_SENDER_REPLY = 10;
    private final int DOC_GETTER_REPLY = 11;
    private final int ORDER_SENDER = 12;
    private final int ORDER_GETTER = 13;
    private final int DATE_MESSAGE = 14;
    private final int MUSIC_SENDER = 15;
    private final int MUSIC_GETTER = 16;
    private final int MUSIC_SENDER_REPLY = 17;
    private final int MUSIC_GETTER_REPLY = 18;
    private final int LAKE_SUPPORT_SENDER = 805;
    private final int LAKE_SUPPORT_GETTER = 806;
    private final int WAIT_FOR_UPLOAD = 100;
    private final int WAIT_FOR_UPLOAD_DOC = 200;
    private final int WAIT_FOR_UPLOAD_MUSIC = 300;

    OnClickMessage onClickMessage;
    public List<Integer> messageIdList;
    private List<Integer> downloadList;
    private boolean isSelectedMode = false;
    private List<SendOrderClass> loadedObject;
    public int imgWaitPosition;
    public int docWaitPosition;
    public int musicWaitPosition;
    public List<Integer> deletePositionList;

    public MessageRecyclerViewAdapter(Context mContext, List<SendMessageViewModel> messageViewModels, OnClickMessage onClickMessage) {
        this.mContext = mContext;
        this.messageViewModels = messageViewModels;
        this.onClickMessage = onClickMessage;
        messageIdList = new ArrayList<>();
        downloadList = new ArrayList<>();
        loadedObject = new ArrayList<>();
        deletePositionList = new ArrayList<>();
      /*  root = getCacheDirectory(mContext).getPath()+"/chat";
        myDir = new File(root);
        myDir.mkdir();
*/

        /*root = Environment.getExternalStorageDirectory()+File.separator+"applicationAsbid";
        myDir = new File(root);
        myDir.mkdirs();*/

        /*root = Environment.getExternalStorageDirectory()+"/myAsbid";
        myDir = new File(root);
        myDir.mkdirs();*/

        // root  +="asbid";

        //myDir = new File(root,"asbidd");
        root = Environment.getExternalStorageDirectory().getPath()/*+ "/Dehkade/Chat1"*/;
        myDir = new File(root);
        myDir.mkdir();


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
        } else if (viewType == IMAGE_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_message_sent, parent, false);
            return new ImageSenderViewHolder(view);
        } else if (viewType == DOC_GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doc_message_recived, parent, false);
            return new DocGetterViewHolder(view);
        } else if (viewType == DOC_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doc_message_send, parent, false);
            return new DocSenderViewHolder(view);
        } else if (viewType == ORDER_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_message_sent, parent, false);
            return new OrderSenderViewHolder(view);
        } else if (viewType == ORDER_GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_message_recived, parent, false);
            return new OrderGetterViewHolder(view);
        } else if (viewType == WAIT_FOR_UPLOAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wait_for_send_image_message_sent, parent, false);
            return new WaitForUploadImageHolder(view);
        } else if (viewType == MSG_SENDER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_reply, parent, false);
            return new ReplyMsgSenderViwHolder(view);
        } else if (viewType == MSG_GETTER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_reply_get, parent, false);
            return new ReplyMsgGetterViwHolder(view);
        } else if (viewType == IMG_MSG_SENDER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_message_reply_send, parent, false);
            return new ReplyImgMsgSenderViwHolder(view);
        } else if (viewType == IMG_MSG_GETTER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_message_reply_get, parent, false);
            return new ReplyImgMsgGetterViwHolder(view);

        } else if (viewType == DOC_SENDER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doc_message_reply_send, parent, false);
            return new ReplyDocMsgSenderViwHolder(view);

        } else if (viewType == DOC_GETTER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doc_message_reply_get, parent, false);
            return new ReplyDocMsgGetterViwHolder(view);
        } else if (viewType == WAIT_FOR_UPLOAD_DOC) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wait_for_send_doc_message_sent, parent, false);
            return new WaitForUploadDocHolder(view);
        } else if (viewType == WAIT_FOR_UPLOAD_MUSIC) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wait_for_send_music_message_sent, parent, false);
            return new WaitForUploadMusicHolder(view);
        }else if (viewType == MUSIC_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_message_send, parent, false);
            return new MusicSenderViewHolder(view);
        }else if (viewType == MUSIC_GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doc_message_recived, parent, false);
            return new MusicGetterViewHolder(view);
        }else if (viewType == MUSIC_SENDER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_message_reply_send, parent, false);
            return new ReplyMusicMsgSenderViwHolder(view);

        } else if (viewType == MUSIC_GETTER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_message_reply_get, parent, false);
            return new ReplyMusicMsgGetterViwHolder(view);
        }else if (viewType ==DATE_MESSAGE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_date_message, parent, false);
            return new DateHolder(view);
        }

        else if (viewType == LAKE_SUPPORT_GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lack_of_message_recieved, parent, false);
            return new LakeSupportGetterHolder(view);
        } else  {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lack_of_message_send, parent, false);
            return new LakeSupportSenderHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            SendMessageViewModel sendMessageViewModel = messageViewModels.get(position);

/*
            generateDate(sendMessageViewModel.getDateSend(), (DateHolder) holder);
*/

            int msgType = sendMessageViewModel.getMsgType();
            String userSender = sendMessageViewModel.getUserSender();
            if (msgType == BaseCodeClass.variableType.string_.getValue() || msgType == BaseCodeClass.variableType.int_.getValue()) {


                if (userSender.equals(MessageActivity.senderId)) {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                        ReplyMsgGetterViwHolder replyMsgGetterViwHolder = (ReplyMsgGetterViwHolder) holder;
                        replyMsgGetterViwHolder.holder(sendMessageViewModel,position);

                    } else {
                        GetterViewHolder getterViewHolder = (GetterViewHolder) holder;
                        getterViewHolder.holder(sendMessageViewModel,position);
                    }

                } else {
                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                        ReplyMsgSenderViwHolder replyMsgSenderViwHolder = (ReplyMsgSenderViwHolder) holder;
                        replyMsgSenderViwHolder.holder(sendMessageViewModel,position);
                    } else {
                        SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                        senderViewHolder.holder(sendMessageViewModel,position);
                    }

                }


            } else if (msgType == BaseCodeClass.variableType.Image_.getValue()) {
                if (userSender.equals(MessageActivity.senderId)) {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                        ReplyImgMsgGetterViwHolder replyImgMsgGetterViwHolder = (ReplyImgMsgGetterViwHolder) holder;
                        replyImgMsgGetterViwHolder.holder(sendMessageViewModel,position);
                    } else {
                        ImageGetterViewHolder imageGetterViewHolder = (ImageGetterViewHolder) holder;
                        imageGetterViewHolder.holder(sendMessageViewModel,position);
                    }

                } else {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyImgMsgSenderViwHolder replyImgMsgSenderViwHolder = (ReplyImgMsgSenderViwHolder) holder;
                        replyImgMsgSenderViwHolder.holder(sendMessageViewModel,position);
                    } else {

                        ImageSenderViewHolder imageSenderViewHolder = (ImageSenderViewHolder) holder;
                        imageSenderViewHolder.holder(sendMessageViewModel,position);
                    }

                }
            } else if (msgType == BaseCodeClass.variableType.File_.getValue()) {
                if (userSender.equals(MessageActivity.senderId)) {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyDocMsgGetterViwHolder replyDocMsgGetterViwHolder = (ReplyDocMsgGetterViwHolder) holder;
                        replyDocMsgGetterViwHolder.holder(sendMessageViewModel,position);
                    } else {

                        DocGetterViewHolder docGetterViewHolder = (DocGetterViewHolder) holder;
                        docGetterViewHolder.holder(messageViewModels.get(position),position);

                    }
                } else {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyDocMsgSenderViwHolder replyDocMsgSenderViwHolder = (ReplyDocMsgSenderViwHolder) holder;
                        replyDocMsgSenderViwHolder.holder(sendMessageViewModel,position);
                    } else {
                        DocSenderViewHolder docSenderViewHolder = (DocSenderViewHolder) holder;
                        docSenderViewHolder.holder(messageViewModels.get(position), position);
                    }
                }
            } else if (msgType == BaseCodeClass.variableType.Order_.getValue()) {
                if (userSender.equals(MessageActivity.senderId)) {

                    OrderGetterViewHolder orderGetterViewHolder = (OrderGetterViewHolder) holder;
                    orderGetterViewHolder.holder(messageViewModels.get(position).getAttachObjectID());

                } else {
                    OrderSenderViewHolder orderSenderViewHolder = (OrderSenderViewHolder) holder;
                    orderSenderViewHolder.holder(messageViewModels.get(position).getAttachObjectID());
                }
            }else if (msgType == BaseCodeClass.variableType.Music_.getValue()) {

                if (userSender.equals(MessageActivity.senderId)) {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyMusicMsgGetterViwHolder replyMusicMsgGetterViwHolder = (ReplyMusicMsgGetterViwHolder) holder;
                        replyMusicMsgGetterViwHolder.holder(sendMessageViewModel,position);
                    } else {

                        MusicGetterViewHolder musicGetterViewHolder = (MusicGetterViewHolder) holder;
                        musicGetterViewHolder.holder(messageViewModels.get(position),position);

                    }
                } else {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyMusicMsgSenderViwHolder replyMusicMsgSenderViwHolder = (ReplyMusicMsgSenderViwHolder) holder;
                        replyMusicMsgSenderViwHolder.holder(sendMessageViewModel,position);
                    } else {
                        MusicSenderViewHolder musicSenderViewHolder = (MusicSenderViewHolder) holder;
                        musicSenderViewHolder.holder(messageViewModels.get(position), position);
                    }
                }

            }else if (msgType == BaseCodeClass.variableType.time.getValue()) {

                DateHolder dateHolder = (DateHolder)holder;
                dateHolder.txtTime.setText(messageViewModels.get(position).getMessage1());

            }else if (msgType == 222) {
                WaitForUploadImageHolder waitForUploadImageHolder = (WaitForUploadImageHolder) holder;
                waitForUploadImageHolder.holder(bitmap, caption, position);

            } else if (msgType == 333) {
                WaitForUploadDocHolder waitForUploadDocHolder = (WaitForUploadDocHolder) holder;
                waitForUploadDocHolder.holder(docName, position);

            }else if (msgType == 444) {
                    WaitForUploadMusicHolder waitForUploadMusicHolder = (WaitForUploadMusicHolder) holder;
                waitForUploadMusicHolder.holder(musicName, position);

            }
            else {
                if (userSender.equals(MessageActivity.senderId)) {
                    LakeSupportGetterHolder lakeSupportHolder = (LakeSupportGetterHolder) holder;
                    lakeSupportHolder.holder(messageViewModels.get(position));


                } else {
                    LakeSupportSenderHolder lakeSupportHolder = (LakeSupportSenderHolder) holder;
                    lakeSupportHolder.holder(messageViewModels.get(position));
                }

            }


        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder:  " + e.getMessage());

        }

    }


    @Override
    public int getItemViewType(int position) {
        SendMessageViewModel sendMessageViewModel = messageViewModels.get(position);
        int msgType = sendMessageViewModel.getMsgType();
        String userSender = sendMessageViewModel.getUserSender();

      /*  Date date = TimeUtils.convertStrToDate(sendMessageViewModel.getDateSend());
        int newDay = date.getDay();
        int oldDay = 0;
        int newMonth = date.getMonth();
        int oldMonth = 0;*/


        if (msgType == BaseCodeClass.variableType.string_.getValue() || msgType == BaseCodeClass.variableType.int_.getValue()) {
            if (userSender.equals(MessageActivity.senderId)) {

                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return MSG_GETTER_REPLY;
                } else {
                    return GETTER;
                }
            } else {
                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return MSG_SENDER_REPLY;
                } else {
                    return SENDER;
                }

            }

        } else if (msgType == BaseCodeClass.variableType.Image_.getValue()) {
            if (userSender.equals(MessageActivity.senderId)) {

                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return IMG_MSG_GETTER_REPLY;
                } else {
                    return IMAGE_GETTER;
                }
            } else {
                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return IMG_MSG_SENDER_REPLY;
                } else {
                    return IMAGE_SENDER;
                }

            }
        } else if (msgType == BaseCodeClass.variableType.File_.getValue()) {
            if (userSender.equals(MessageActivity.senderId)) {

                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return DOC_GETTER_REPLY;
                } else {
                    return DOC_GETTER;
                }
            } else {

                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return DOC_SENDER_REPLY;
                } else {
                    return DOC_SENDER;
                }

            }
        } else if (msgType == BaseCodeClass.variableType.Order_.getValue()) {
            if (userSender.equals(MessageActivity.senderId)) {
                return ORDER_GETTER;

            } else {
                return ORDER_SENDER;
            }

        } else if (msgType == BaseCodeClass.variableType.Music_.getValue()) {

            if (userSender.equals(MessageActivity.senderId)) {

                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return MUSIC_GETTER_REPLY;
                } else {
                    return MUSIC_GETTER;
                }
            } else {

                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return MUSIC_SENDER_REPLY;
                } else {
                    return MUSIC_SENDER;
                }

            }

        }else if (msgType == BaseCodeClass.variableType.time.getValue()) {

           return DATE_MESSAGE;

        }else if (msgType == 222) {
            return WAIT_FOR_UPLOAD;
        } else if (msgType == 333) {
            return WAIT_FOR_UPLOAD_DOC;

        }else if (msgType == 444) {
            return WAIT_FOR_UPLOAD_MUSIC;

        }

        /*else if (newDay != oldDay && newMonth != oldMonth) {
            return DATE_MESSAGE;
        }*/ else {
            if (userSender.equals(MessageActivity.senderId)) {
                return LAKE_SUPPORT_GETTER;


            } else {
                return LAKE_SUPPORT_SENDER;
            }
        }
    }


    public class GetterViewHolder extends RecyclerView.ViewHolder {
        TextView textReceived, timeReceived;
        ConstraintLayout parent;

        public GetterViewHolder(@NonNull View itemView) {
            super(itemView);
            textReceived = itemView.findViewById(R.id.messageTextIn);
            timeReceived = itemView.findViewById(R.id.messageTimeIn);
            parent = itemView.findViewById(R.id.layout_messageRecived_parent);
        }

        void holder(SendMessageViewModel messageData,int position) {
            textReceived.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            timeReceived.setText(time);

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

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

        void holder(SendMessageViewModel messageData,int position) {
            Glide.with(mContext).load(generateUrl(Integer.parseInt(messageData.getId()))).placeholder(R.drawable.image_placeholder).into(imgMessageRecived);
            txtImageMessageRecived.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtImageMessageTimeRecived.setText(time);
            imgMessageRecived.setOnClickListener(v -> showFullScreenImage(messageData.getId()));

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                imgMsgRecParent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                imgMsgRecParent.setBackgroundColor(Color.TRANSPARENT);
            }

            imgMsgRecParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    imgMsgRecParent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(imgMsgRecParent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            imgMsgRecParent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        imgMsgRecParent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        imgMsgRecParent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(imgMsgRecParent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });


        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView timeSent, textSent;
        ImageView ivTick;
        ConstraintLayout parent;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSent = itemView.findViewById(R.id.tv_message_sent_time);
            textSent = itemView.findViewById(R.id.tv_message_sent);
            ivTick = itemView.findViewById(R.id.iv_message_sent_tick);
            parent = itemView.findViewById(R.id.layout_messageSent_parent);
        }

        void holder(SendMessageViewModel messageData,int position) {

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


            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    }
                }
            });

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

        void holder(SendMessageViewModel messageData,int position) {
            Glide.with(mContext).load(generateUrl(Integer.parseInt(messageData.getId()))).placeholder(R.drawable.image_placeholder).into(imgMessageSend);
            txtImageMessageSend.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtImageMessageTimeSend.setText(time);
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
            imgMessageSend.setOnClickListener(v -> showFullScreenImage(messageData.getId()));

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                imgMsgSendParent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                imgMsgSendParent.setBackgroundColor(Color.TRANSPARENT);

            }


            imgMsgSendParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    imgMsgSendParent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(imgMsgSendParent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            imgMsgSendParent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        imgMsgSendParent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        imgMsgSendParent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(imgMsgSendParent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    }
                }
            });


        }

    }

    public class DocSenderViewHolder extends RecyclerView.ViewHolder {
        TextView txtDocName, txtTime;
        ImageView imgMessageTick;
        CardView cardRoot;
        ConstraintLayout parent;

        public DocSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_layout_Doc_message_send);
            imgMessageTick = itemView.findViewById(R.id.iv_layout_Doc_message_recived_tick);
            txtTime = itemView.findViewById(R.id.txtTime_layout_Doc_message_send);
            cardRoot = itemView.findViewById(R.id.cardRoot_LayoutDocMessageSend);
            parent = itemView.findViewById(R.id.constraint_layout_docSend_parent);

        }

        void holder(SendMessageViewModel messageData, int position) {
            txtDocName.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);
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

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });


        }
    }

    public class DocGetterViewHolder extends RecyclerView.ViewHolder {
        TextView txtDocName, txtTime;
        CircularImageView imgDownload;
        ProgressBar prg;
        CardView cardRoot;
        ConstraintLayout parent;

        public DocGetterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_layout_Doc_message_recived);
            txtTime = itemView.findViewById(R.id.txtTime_layout_Doc_message_recived);
            imgDownload = itemView.findViewById(R.id.img_layout_Doc_message_recived);
            prg = itemView.findViewById(R.id.prg_layout_Doc_message_recived);
            cardRoot = itemView.findViewById(R.id.cardRoot_LayoutDocMessageSend);
            parent = itemView.findViewById(R.id.constraint_layout_docRecived_parent);


        }

        void holder(SendMessageViewModel messageData,int position) {
            txtDocName.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);
       /*     String fname = root + "/" +messageData.getMessage1();
            File file = new File(fname);
            if (file.canRead()){
                imgDownload.setImageResource(R.drawable.ic_doc3);
                imgDownload.setContentDescription("after");
            }
            if (file.exists()){
                imgDownload.setImageResource(R.drawable.ic_doc3);
                imgDownload.setContentDescription("after");
            }*/

            if (isDocDownload(Integer.parseInt(messageData.getId()))) {
                imgDownload.setImageResource(R.drawable.ic_doc3);
                prg.setVisibility(View.GONE);

            } else {
                imgDownload.setImageResource(R.drawable.ic_download2);
                prg.setVisibility(View.GONE);
            }

            imgDownload.setOnClickListener(v -> {
                if (imgDownload.getContentDescription().equals("after")) {
                   /* String fname = root + "/" +messageData.getMessage1();
                    File file = new File(fname);

                    openFile(file);*/


                } else {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadDoc(prg, imgDownload, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())), messageData);
                }

            });


            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);
            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });


        }
    }

    public class OrderSenderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtCustomerName, txtOrderAddress, txtProductCount, txtSumPrice, txtDetails;
        ImageView imgCustomer, imgProduct;

        public OrderSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.OrderID);
            txtOrderDate = itemView.findViewById(R.id.orderDate);
            txtCustomerName = itemView.findViewById(R.id.costumertName);
            txtOrderAddress = itemView.findViewById(R.id.OrderAddress);
            txtProductCount = itemView.findViewById(R.id.txtMaystoreOrderListProductCount);
            txtSumPrice = itemView.findViewById(R.id.sumPrice);
            imgCustomer = itemView.findViewById(R.id.customerImage);
            imgProduct = itemView.findViewById(R.id.sumProductImage);
            txtDetails = itemView.findViewById(R.id.detail);
        }

        void holder(String orderId) {
            if (!isDataLoaded(orderId)) {
                MessageApi messageApi = RetrofitInstance.getRetrofit().create(MessageApi.class);
                Call<SendOrderClass> call = messageApi.getOrderData2(orderId);
                call.enqueue(new Callback<SendOrderClass>() {
                    @Override
                    public void onResponse(Call<SendOrderClass> call, Response<SendOrderClass> response) {
                        SendOrderClass sendOrderClasses = response.body();
                        initOrder2(sendOrderClasses, OrderSenderViewHolder.this);


                    }

                    @Override
                    public void onFailure(Call<SendOrderClass> call, Throwable t) {

                    }
                });

            } else {
                initOrder2(loadOrder(orderId), OrderSenderViewHolder.this);
            }


        }

    }

    public class OrderGetterViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtCustomerName, txtOrderAddress, txtProductCount, txtSumPrice, txtDetails;
        ImageView imgCustomer, imgProduct;

        public OrderGetterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.OrderID);
            txtOrderDate = itemView.findViewById(R.id.orderDate);
            txtCustomerName = itemView.findViewById(R.id.costumertName);
            txtOrderAddress = itemView.findViewById(R.id.OrderAddress);
            txtProductCount = itemView.findViewById(R.id.txtMaystoreOrderListProductCount);
            txtSumPrice = itemView.findViewById(R.id.sumPrice);
            imgCustomer = itemView.findViewById(R.id.customerImage);
            imgProduct = itemView.findViewById(R.id.sumProductImage);
            txtDetails = itemView.findViewById(R.id.detail);
        }

        void holder(String orderId) {
            if (!isDataLoaded(orderId)) {
                MessageApi messageApi = RetrofitInstance.getRetrofit().create(MessageApi.class);
                Call<SendOrderClass> call = messageApi.getOrderData2(orderId);
                call.enqueue(new Callback<SendOrderClass>() {
                    @Override
                    public void onResponse(Call<SendOrderClass> call, Response<SendOrderClass> response) {
                        SendOrderClass sendOrderClasses = response.body();
                        initOrder(sendOrderClasses, OrderGetterViewHolder.this);


                    }

                    @Override
                    public void onFailure(Call<SendOrderClass> call, Throwable t) {

                    }
                });

            } else {
                initOrder(loadOrder(orderId), OrderGetterViewHolder.this);
            }


        }
    }

    public class MusicSenderViewHolder extends RecyclerView.ViewHolder {
        TextView txtDocName, txtTime;
        ImageView imgMessageTick;
        CardView cardRoot;
        ConstraintLayout parent;

        public MusicSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_layout_Doc_message_send);
            imgMessageTick = itemView.findViewById(R.id.iv_layout_Doc_message_recived_tick);
            txtTime = itemView.findViewById(R.id.txtTime_layout_Doc_message_send);
            cardRoot = itemView.findViewById(R.id.cardRoot_LayoutDocMessageSend);
            parent = itemView.findViewById(R.id.constraint_layout_docSend_parent);

        }

        void holder(SendMessageViewModel messageData, int position) {
            txtDocName.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);
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

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });


        }
    }
    public class MusicGetterViewHolder extends RecyclerView.ViewHolder {
        TextView txtDocName, txtTime;
        CircularImageView imgDownload;
        ProgressBar prg;
        CardView cardRoot;
        ConstraintLayout parent;

        public MusicGetterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_layout_Doc_message_recived);
            txtTime = itemView.findViewById(R.id.txtTime_layout_Doc_message_recived);
            imgDownload = itemView.findViewById(R.id.img_layout_Doc_message_recived);
            prg = itemView.findViewById(R.id.prg_layout_Doc_message_recived);
            cardRoot = itemView.findViewById(R.id.cardRoot_LayoutDocMessageSend);
            parent = itemView.findViewById(R.id.constraint_layout_docRecived_parent);


        }

        void holder(SendMessageViewModel messageData,int position) {
            txtDocName.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);
       /*     String fname = root + "/" +messageData.getMessage1();
            File file = new File(fname);
            if (file.canRead()){
                imgDownload.setImageResource(R.drawable.ic_doc3);
                imgDownload.setContentDescription("after");
            }
            if (file.exists()){
                imgDownload.setImageResource(R.drawable.ic_doc3);
                imgDownload.setContentDescription("after");
            }*/

            if (isDocDownload(Integer.parseInt(messageData.getId()))) {
                imgDownload.setImageResource(R.drawable.ic_music_play);
                prg.setVisibility(View.GONE);

            } else {
                imgDownload.setImageResource(R.drawable.ic_download2);
                prg.setVisibility(View.GONE);
            }

            imgDownload.setOnClickListener(v -> {
                if (imgDownload.getContentDescription().equals("after")) {
                   /* String fname = root + "/" +messageData.getMessage1();
                    File file = new File(fname);

                    openFile(file);*/


                } else {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadMusic(prg, imgDownload, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())), messageData);
                }

            });


            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);
            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });


        }
    }




    public class ReplyMsgSenderViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ImageView imagSeen;
        ConstraintLayout parent;
        LinearLayout oldMessage;

        public ReplyMsgSenderViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            imagSeen = itemView.findViewById(R.id.iv_message_sent_tick);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMessage = itemView.findViewById(R.id.rel_oldMessage);
        }

        void holder(SendMessageViewModel messageData,int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);
            switch (messageData.getStatus()) {
                case "1":
                    imagSeen.setImageResource(R.drawable.ic_tick);
                    break;
                case "2":
                    imagSeen.setImageResource(R.drawable.ic_tick_done);
                    break;
                case "3":
                    imagSeen.setImageResource(R.drawable.ic_tick_seen);
                    break;
            }

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

            oldMessage.setOnClickListener(v -> {
                int oldPosition = getOldMsgPosition(messageData.getReplyMsg());
                if (oldPosition != -1) {
                    onClickMessage.scrollToCertainPosition(oldPosition);
                }
            });

        }
    }

    public class ReplyImgMsgSenderViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ImageView imagSeen, imgOld;
        ConstraintLayout parent;
        LinearLayout oldMessage;


        public ReplyImgMsgSenderViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            imagSeen = itemView.findViewById(R.id.iv_message_sent_tick);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMessage = itemView.findViewById(R.id.rel_oldMessage);
            imgOld = itemView.findViewById(R.id.img_messageReply_oldValue);
        }

        void holder(SendMessageViewModel messageData,int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);
            String oldId = getOldMessageid(messageData.getReplyMsg());

            Glide.with(mContext).load(generateUrl(Integer.parseInt(oldId))).placeholder(R.drawable.image_placeholder).into(imgOld);
            switch (messageData.getStatus()) {
                case "1":
                    imagSeen.setImageResource(R.drawable.ic_tick);
                    break;
                case "2":
                    imagSeen.setImageResource(R.drawable.ic_tick_done);
                    break;
                case "3":
                    imagSeen.setImageResource(R.drawable.ic_tick_seen);
                    break;
            }


            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

            oldMessage.setOnClickListener(v -> {
                int oldPosition = getOldMsgPosition(messageData.getReplyMsg());
                if (oldPosition != -1) {
                    onClickMessage.scrollToCertainPosition(oldPosition);
                }
            });

        }
    }

    public class ReplyDocMsgSenderViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ImageView imagSeen;
        ConstraintLayout parent;
        LinearLayout oldMessage;


        public ReplyDocMsgSenderViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            imagSeen = itemView.findViewById(R.id.iv_message_sent_tick);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMessage = itemView.findViewById(R.id.rel_oldMessage);
        }

        void holder(SendMessageViewModel messageData,int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);
            switch (messageData.getStatus()) {
                case "1":
                    imagSeen.setImageResource(R.drawable.ic_tick);
                    break;
                case "2":
                    imagSeen.setImageResource(R.drawable.ic_tick_done);
                    break;
                case "3":
                    imagSeen.setImageResource(R.drawable.ic_tick_seen);
                    break;
            }


            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

            oldMessage.setOnClickListener(v -> {
                int oldPosition = getOldMsgPosition(messageData.getReplyMsg());
                if (oldPosition != -1) {
                    onClickMessage.scrollToCertainPosition(oldPosition);
                }
            });

        }
    }
    public class ReplyMusicMsgSenderViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ImageView imagSeen;
        ConstraintLayout parent;
        LinearLayout oldMessage;


        public ReplyMusicMsgSenderViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            imagSeen = itemView.findViewById(R.id.iv_message_sent_tick);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMessage = itemView.findViewById(R.id.rel_oldMessage);
        }

        void holder(SendMessageViewModel messageData,int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);
            switch (messageData.getStatus()) {
                case "1":
                    imagSeen.setImageResource(R.drawable.ic_tick);
                    break;
                case "2":
                    imagSeen.setImageResource(R.drawable.ic_tick_done);
                    break;
                case "3":
                    imagSeen.setImageResource(R.drawable.ic_tick_seen);
                    break;
            }


            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

            oldMessage.setOnClickListener(v -> {
                int oldPosition = getOldMsgPosition(messageData.getReplyMsg());
                if (oldPosition != -1) {
                    onClickMessage.scrollToCertainPosition(oldPosition);
                }
            });

        }
    }

    public class ReplyMsgGetterViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ConstraintLayout parent;
        LinearLayout oldMsg;

        public ReplyMsgGetterViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMsg = itemView.findViewById(R.id.rel_oldMessage);
        }

        void holder(SendMessageViewModel messageData,int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

            oldMsg.setOnClickListener(v -> {
                int oldPosition = getOldMsgPosition(messageData.getReplyMsg());
                if (oldPosition != -1) {
                    onClickMessage.scrollToCertainPosition(oldPosition);
                }
            });

        }
    }

    public class ReplyImgMsgGetterViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ImageView imgOld;
        ConstraintLayout parent;
        LinearLayout oldMessage;


        public ReplyImgMsgGetterViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMessage = itemView.findViewById(R.id.rel_oldMessage);
            imgOld = itemView.findViewById(R.id.img_messageReply_oldValue);
        }

        void holder(SendMessageViewModel messageData,int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);
            String oldId = getOldMessageid(messageData.getReplyMsg());

            Glide.with(mContext).load(generateUrl(Integer.parseInt(oldId))).placeholder(R.drawable.image_placeholder).into(imgOld);

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

            oldMessage.setOnClickListener(v -> {
                int oldPosition = getOldMsgPosition(messageData.getReplyMsg());
                if (oldPosition != -1) {
                    onClickMessage.scrollToCertainPosition(oldPosition);
                }
            });

        }
    }

    public class ReplyDocMsgGetterViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ConstraintLayout parent;
        LinearLayout oldMessage;


        public ReplyDocMsgGetterViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMessage = itemView.findViewById(R.id.rel_oldMessage);
        }

        void holder(SendMessageViewModel messageData,int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

            oldMessage.setOnClickListener(v -> {
                int oldPosition = getOldMsgPosition(messageData.getReplyMsg());
                if (oldPosition != -1) {
                    onClickMessage.scrollToCertainPosition(oldPosition);
                }
            });

        }
    }
    public class ReplyMusicMsgGetterViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ConstraintLayout parent;
        LinearLayout oldMessage;


        public ReplyMusicMsgGetterViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMessage = itemView.findViewById(R.id.rel_oldMessage);
        }

        void holder(SendMessageViewModel messageData,int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode) {
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    } else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(),position);


                    }
                }
            });

            oldMessage.setOnClickListener(v -> {
                int oldPosition = getOldMsgPosition(messageData.getReplyMsg());
                if (oldPosition != -1) {
                    onClickMessage.scrollToCertainPosition(oldPosition);
                }
            });

        }
    }


    public class WaitForUploadImageHolder extends RecyclerView.ViewHolder {
        ImageView imgMessageSend;
        TextView txtImageMessageSend;
        RelativeLayout relProgressBar;

        public WaitForUploadImageHolder(@NonNull View itemView) {
            super(itemView);
            imgMessageSend = itemView.findViewById(R.id.img_layout_imageMessageSentWait);
            txtImageMessageSend = itemView.findViewById(R.id.txt_layout_imageMessageSentWait);
            relProgressBar = itemView.findViewById(R.id.relativeLayout_progressbar_sendImageMessageWait);
        }

        void holder(Bitmap bitmap, String caption, int position) {
            imgWaitPosition = position;
            Glide.with(mContext).load(bitmap)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgMessageSend);


            txtImageMessageSend.setText(caption);
            relProgressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessageVM.compositeDisposable.dispose();
                    messageViewModels.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }

    }

    public class WaitForUploadDocHolder extends RecyclerView.ViewHolder {
        TextView txtDocName;


        public WaitForUploadDocHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_layout_docMsgSendWait);

        }

        void holder(String docName, int position) {
            docWaitPosition =position;
            txtDocName.setText(docName);
        }

    }
    public class WaitForUploadMusicHolder extends RecyclerView.ViewHolder {
        TextView txtMusicName;


        public WaitForUploadMusicHolder(@NonNull View itemView) {
            super(itemView);
            txtMusicName = itemView.findViewById(R.id.txt_layout_docMsgSendWait);

        }

        void holder(String docName, int position) {
            musicWaitPosition =position;
            txtMusicName.setText(docName);
        }

    }

    public class LakeSupportSenderHolder extends RecyclerView.ViewHolder {

        TextView txtTime;
        ImageView imgTik;


        public LakeSupportSenderHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.tv_message_sent_time);
            imgTik = itemView.findViewById(R.id.iv_message_sent_tick);

        }

        void holder(SendMessageViewModel messageData) {

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);

            switch (messageData.getStatus()) {
                case "1":
                    imgTik.setImageResource(R.drawable.ic_tick);
                    break;
                case "2":
                    imgTik.setImageResource(R.drawable.ic_tick_done);
                    break;
                case "3":
                    imgTik.setImageResource(R.drawable.ic_tick_seen);
                    break;
            }


        }

    }

    public class LakeSupportGetterHolder extends RecyclerView.ViewHolder {

        TextView txtTime;


        public LakeSupportGetterHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.tv_message_sent_time);
        }

        void holder(SendMessageViewModel messageData) {

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);

        }

    }

    public class DateHolder extends RecyclerView.ViewHolder {

        TextView txtTime;


        public DateHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtDateMessage);
        }

 /*       void holder(SendMessageViewModel messageData) {

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);

        }*/

    }


    public void updateMessage(List<SendMessageViewModel> _messageViewModels) {
        if (_messageViewModels == null) {
            return;
        }

        if (_messageViewModels.size() == 0) {
            return;
        }
        messageViewModels.addAll(_messageViewModels);
        notifyDataSetChanged();
    }

    private String generateUrl(int chatId) {
        String url = baseCodeClass.BASE_URL + "User/DownloadCahtFile?Chatid=" + chatId;
        return url;


    }

    private void showFullScreenImage(String chatId) {
        Intent intent = new Intent(mContext, ActivityShowFullScreenImage.class);
        intent.putExtra("image_url", generateUrl(Integer.parseInt(chatId)));
        mContext.startActivity(intent);
    }


    public void initWaitValue(Bitmap bitmap, String caption, SendMessageVM sendMessageVM) {
        this.bitmap = bitmap;
        this.caption = caption;
        this.sendMessageVM = sendMessageVM;
    }

    public void initWaitValueDoc(String docName, SendMessageVM sendMessageVM) {
        this.docName = docName;
        this.sendMessageVM = sendMessageVM;
    }
    public void initWaitValueMusic(String musicName, SendMessageVM sendMessageVM) {
        this.musicName = musicName;
        this.sendMessageVM = sendMessageVM;
    }

    private void downloadDoc(ProgressBar progressBar, CircularImageView imageView, String name, String link, SendMessageViewModel messageData) {
        progressBar.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(link);
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("در حال دانلود");
        request.setDescription("لطفا منتظر بمانید...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

/*
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, fileName);
*/

        request.setDestinationInExternalFilesDir(mContext, root, name);

        final long id = downloadManager.enqueue(request);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    long totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    final int percent = (int) ((downloadedBytes * 100) / totalBytes);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(percent);
                            if (percent == 100) {
                                Toast.makeText(mContext, "دانلود با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                timer.purge();
                                timer.cancel();
                                for (int item : downloadList) {
                                    if (Integer.parseInt(messageData.getId()) == item) {
                                        imageView.setImageResource(R.drawable.ic_doc3);
                                        imageView.setContentDescription("after");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }


                            }
                        }
                    });
                }


            }
        }, 0, 100);
    }
    private void downloadMusic(ProgressBar progressBar, CircularImageView imageView, String name, String link, SendMessageViewModel messageData) {
        progressBar.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(link);
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("در حال دانلود");
        request.setDescription("لطفا منتظر بمانید...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

/*
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, fileName);
*/

        request.setDestinationInExternalFilesDir(mContext, root, name);

        final long id = downloadManager.enqueue(request);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    long totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    final int percent = (int) ((downloadedBytes * 100) / totalBytes);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(percent);
                            if (percent == 100) {
                                Toast.makeText(mContext, "دانلود با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                timer.purge();
                                timer.cancel();
                                for (int item : downloadList) {
                                    if (Integer.parseInt(messageData.getId()) == item) {
                                        imageView.setImageResource(R.drawable.ic_music_play);
                                        imageView.setContentDescription("after");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }


                            }
                        }
                    });
                }


            }
        }, 0, 100);
    }

    private void openFile(File url) {

        try {

/*
            Uri uri = Uri.fromFile(url);
*/
            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")) {
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((AppCompatActivity) mContext).startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public boolean isSelecetedMessage(int id) {
        for (int i : messageIdList) {
            if (i == id) {
                return true;
            }

        }
        return false;
    }

    public void selectedFalse() {
        isSelectedMode = false;
    }

    public boolean isDocDownload(int id) {
        for (int i : downloadList) {
            if (i == id) {
                return true;
            }

        }
        return false;
    }


    public interface OnClickMessage {
        void messageSelected(ConstraintLayout parent, String messageId, SendMessageViewModel messageData, int messageType,int position);

        void messageUnSelected(String id);

        void scrollToCertainPosition(int position);
    }

    public String getOldMessage(String id) {
        for (SendMessageViewModel message : messageViewModels) {
            if (message.getId().equals(id)) {
                return message.getMessage1();
            }

        }

        return "پیام حذف شده";
    }

    public String getOldMessageid(String id) {
        for (SendMessageViewModel message : messageViewModels) {
            if (message.getId().equals(id)) {
                return message.getId();
            }

        }

        return "-1";
    }

    public int getOldMsgPosition(String id) {
        int i = 0;
        for (SendMessageViewModel message : messageViewModels) {
            if (message.getId().equals(id)) {
                return i;

            }
            i++;
        }

        return -1;

    }

    public boolean isDataLoaded(String id) {
        for (SendOrderClass sendOrderClass : loadedObject) {
            if (sendOrderClass.getOrderID().equals(id)) {
                return true;
            }
        }

        return false;

    }

    public void initOrder(SendOrderClass sendOrderClasses, OrderGetterViewHolder orderGetterViewHolder) {
        if (sendOrderClasses == null) {
            return;
        }
        if (!isDataLoaded(sendOrderClasses.getOrderID())) {
            loadedObject.add(sendOrderClasses);
        }

        String orderDate = TimeUtils.getPersianCleanDate(sendOrderClasses.getOrderDate());
        orderGetterViewHolder.txtOrderDate.setText(orderDate);
        orderGetterViewHolder.txtOrderId.setText(sendOrderClasses.getId());
        orderGetterViewHolder.txtCustomerName.setText(sendOrderClasses.getSpare2());
        orderGetterViewHolder.txtOrderAddress.setText(sendOrderClasses.getSpare1());
        orderGetterViewHolder.txtSumPrice.setText(sendOrderClasses.getSumPrice());
        int quantity = sendOrderClasses.getOrder_Details().get(0).getQuantity();
        String orderCount = "اقلام: " + quantity;


        orderGetterViewHolder.txtProductCount.setText(orderCount);
        String urlProductImage = null;
        if (sendOrderClasses.Order_Details.size() > 0) {
            urlProductImage = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + sendOrderClasses.Order_Details.get(0).getProductID() + "&fileNumber=1";
        }
        String urlCustomerImage = baseCodeClass.BASE_URL + "User/DownloadCustomerFile?CustomerID=" + sendOrderClasses.getCustomerID() + "&fileNumber=" + 1;

        if (urlProductImage != null) {
            Glide.with(mContext).load(urlProductImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.logodehkade).into(orderGetterViewHolder.imgProduct);
        }
        Glide.with(mContext).load(urlCustomerImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_profile).into(orderGetterViewHolder.imgCustomer);


        orderGetterViewHolder.txtDetails.setOnClickListener(v -> {
            BaseCodeClass.myStoreSelectedOrder = sendOrderClasses;

            Intent intent = new Intent(mContext, MyStoreOrderDetail.class);

            intent.putExtra("User", true);

            mContext.startActivity(intent);
        });
    }

    public void initOrder2(SendOrderClass sendOrderClasses, OrderSenderViewHolder orderSenderViewHolder) {
        if (sendOrderClasses == null) {
            return;
        }
        if (!isDataLoaded(sendOrderClasses.getOrderID())) {
            loadedObject.add(sendOrderClasses);
        }

        String orderDate = TimeUtils.getPersianCleanDate(sendOrderClasses.getOrderDate());
        orderSenderViewHolder.txtOrderDate.setText(orderDate);
        orderSenderViewHolder.txtOrderId.setText(sendOrderClasses.getId());
        orderSenderViewHolder.txtCustomerName.setText(sendOrderClasses.getSpare2());
        orderSenderViewHolder.txtOrderAddress.setText(sendOrderClasses.getSpare1());
        orderSenderViewHolder.txtSumPrice.setText(sendOrderClasses.getSumPrice());
        int quantity = sendOrderClasses.getOrder_Details().get(0).getQuantity();
        String orderCount = "اقلام: " + quantity;


        orderSenderViewHolder.txtProductCount.setText(orderCount);
        String urlProductImage = null;
        if (sendOrderClasses.Order_Details.size() > 0) {
            urlProductImage = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + sendOrderClasses.Order_Details.get(0).getProductID() + "&fileNumber=1";
        }
        String urlCustomerImage = baseCodeClass.BASE_URL + "User/DownloadCustomerFile?CustomerID=" + sendOrderClasses.getCustomerID() + "&fileNumber=" + 1;

        if (urlProductImage != null) {
            Glide.with(mContext).load(urlProductImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.logodehkade).into(orderSenderViewHolder.imgProduct);
        }
        Glide.with(mContext).load(urlCustomerImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_profile).into(orderSenderViewHolder.imgCustomer);


        orderSenderViewHolder.txtDetails.setOnClickListener(v -> {
            BaseCodeClass.myStoreSelectedOrder = sendOrderClasses;

            Intent intent = new Intent(mContext, MyStoreOrderDetail.class);

            intent.putExtra("User", true);

            mContext.startActivity(intent);
        });
    }


    private SendOrderClass loadOrder(String id) {
        for (SendOrderClass sendOrderClass : loadedObject) {
            if (sendOrderClass.getOrderID() == id) {
                return sendOrderClass;
            }
        }

        return null;
    }

/*
    private void generateDate(String time, DateHolder holder) {
        Date date = TimeUtils.convertStrToDate(time);
        newDay = date.getDay();
        oldDay = 0;
        newMonth = date.getMonth();
        oldMonth = 0;


        if (newDay != oldDay && newMonth != oldMonth) {
*/
/*
            DateHolder dateHolder =(DateHolder) holder;
*//*

            String strTime = TimeUtils.getPersianCleanDate(time);
            holder.txtTime.setText(strTime);
            oldDay = newDay;
            oldMonth = newMonth;
        }
    }
*/


}
