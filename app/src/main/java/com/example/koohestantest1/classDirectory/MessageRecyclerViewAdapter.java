package com.example.koohestantest1.classDirectory;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.koohestantest1.ActivityShowFullScreenImage;
import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.MyStoreOrderDetail;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.Utils.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.activity.ActivityVideoPlay;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.SendMessageVM;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.jetbrains.annotations.NotNull;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements EasyPermissions.PermissionCallbacks {

    private Context mContext;
    public List<SendMessageViewModel> messageViewModels;
    private String TAG = MessageRecyclerViewAdapter.class.getSimpleName();
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    private Bitmap bitmap;
    private String caption;
    private SendMessageVM sendMessageVM;
    private String docName;
    private String musicName;
    private String videoName;
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
    private final int VIDEO_SENDER = 19;
    private final int VIDEO_GETTER = 20;
    private final int VIDEO_SENDER_REPLY = 21;
    private final int VIDEO_GETTER_REPLY = 22;
    private final int LAKE_SUPPORT_SENDER = 805;
    private final int LAKE_SUPPORT_GETTER = 806;
    private final int WAIT_FOR_UPLOAD = 100;
    private final int WAIT_FOR_UPLOAD_DOC = 200;
    private final int WAIT_FOR_UPLOAD_MUSIC = 300;
    private final int WAIT_FOR_UPLOAD_VIDEO = 400;

    OnClickMessage onClickMessage;
    public List<Integer> messageIdList;
    private List<Integer> downloadList;
    private List<Integer> playedList;
    private boolean isSelectedMode = false;
    private List<SendOrderClass> loadedObject;
    public int imgWaitPosition;
    public int docWaitPosition;
    public int musicWaitPosition;
    public int videoWaitPosition;
    public List<Integer> deletePositionList;
    private MediaPlayer mediaPlayer;
    private String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int READ_STORAGE_PERMISSION_REQUEST = 130587;

    private boolean playSender = false;
    private boolean playGeter = false;
    private boolean playTwo = false;
    private CircularImageView cr_send, cr_get;
    private Map<Integer, CircularImageView> cr_getMap, cr_sendMap;
    private int playPosition;
    private YoYo.YoYoString yoYoString;

    private File myFolderCatch;
/*
    private String root = getCacheDirectory(mContext).getPath() + "/chat";
*/


    public MessageRecyclerViewAdapter(Context mContext, List<SendMessageViewModel> messageViewModels, OnClickMessage onClickMessage) {
        this.mContext = mContext;
        this.messageViewModels = messageViewModels;
        this.onClickMessage = onClickMessage;
        messageIdList = new ArrayList<>();
        downloadList = new ArrayList<>();
        loadedObject = new ArrayList<>();
        deletePositionList = new ArrayList<>();
        playedList = new ArrayList<>();
        cr_getMap = new HashMap<>();
        cr_sendMap = new HashMap<>();

/*
        createCatchDirectory(getCacheDirectory(mContext).getPath() + "/chat");
*/
     /*   root = getCacheDirectory(mContext).getPath()+"/chat";
        myDir = new File(root);
        if (!myDir.exists()){
            myDir.mkdir();
        }*/
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
        } else if (viewType == VIDEO_SENDER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_message_reply_send, parent, false);
            return new ReplyVideoMsgSenderViwHolder(view);
        } else if (viewType == VIDEO_GETTER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_message_reply_get, parent, false);
            return new ReplyVideoMsgGetterViwHolder(view);

        } else if (viewType == WAIT_FOR_UPLOAD_DOC) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wait_for_send_doc_message_sent, parent, false);
            return new WaitForUploadDocHolder(view);
        } else if (viewType == WAIT_FOR_UPLOAD_MUSIC) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wait_for_send_music_message_sent, parent, false);
            return new WaitForUploadMusicHolder(view);
        } else if (viewType == WAIT_FOR_UPLOAD_VIDEO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_message_sent, parent, false);
            return new WaitForUploadVideoHolder(view);
        } else if (viewType == MUSIC_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_message_send, parent, false);
            return new MusicSenderViewHolder(view);
        } else if (viewType == MUSIC_GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doc_message_recived, parent, false);
            return new MusicGetterViewHolder(view);
        } else if (viewType == MUSIC_SENDER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_message_reply_send, parent, false);
            return new ReplyMusicMsgSenderViwHolder(view);

        } else if (viewType == MUSIC_GETTER_REPLY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_message_reply_get, parent, false);
            return new ReplyMusicMsgGetterViwHolder(view);
        } else if (viewType == VIDEO_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_message_sent, parent, false);
            return new VideoSenderViewHolder(view);
        } else if (viewType == VIDEO_GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_message_get, parent, false);
            return new VideoGetterViewHolder(view);
        } else if (viewType == DATE_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_date_message, parent, false);
            return new DateHolder(view);
        } else if (viewType == LAKE_SUPPORT_GETTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lack_of_message_recieved, parent, false);
            return new LakeSupportGetterHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lack_of_message_send, parent, false);
            return new LakeSupportSenderHolder(view);
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

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                        ReplyMsgGetterViwHolder replyMsgGetterViwHolder = (ReplyMsgGetterViwHolder) holder;
                        replyMsgGetterViwHolder.holder(sendMessageViewModel, position);

                    } else {
                        GetterViewHolder getterViewHolder = (GetterViewHolder) holder;
                        getterViewHolder.holder(sendMessageViewModel, position);
                    }

                } else {
                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                        ReplyMsgSenderViwHolder replyMsgSenderViwHolder = (ReplyMsgSenderViwHolder) holder;
                        replyMsgSenderViwHolder.holder(sendMessageViewModel, position);
                    } else {
                        SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                        senderViewHolder.holder(sendMessageViewModel, position);
                    }

                }


            } else if (msgType == BaseCodeClass.variableType.Image_.getValue()) {
                if (userSender.equals(MessageActivity.senderId)) {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                        ReplyImgMsgGetterViwHolder replyImgMsgGetterViwHolder = (ReplyImgMsgGetterViwHolder) holder;
                        replyImgMsgGetterViwHolder.holder(sendMessageViewModel, position);
                    } else {
                        ImageGetterViewHolder imageGetterViewHolder = (ImageGetterViewHolder) holder;
                        imageGetterViewHolder.holder(sendMessageViewModel, position);
                    }

                } else {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyImgMsgSenderViwHolder replyImgMsgSenderViwHolder = (ReplyImgMsgSenderViwHolder) holder;
                        replyImgMsgSenderViwHolder.holder(sendMessageViewModel, position);
                    } else {

                        ImageSenderViewHolder imageSenderViewHolder = (ImageSenderViewHolder) holder;
                        imageSenderViewHolder.holder(sendMessageViewModel, position);
                    }

                }
            } else if (msgType == BaseCodeClass.variableType.File_.getValue()) {
                if (userSender.equals(MessageActivity.senderId)) {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyDocMsgGetterViwHolder replyDocMsgGetterViwHolder = (ReplyDocMsgGetterViwHolder) holder;
                        replyDocMsgGetterViwHolder.holder(sendMessageViewModel, position);
                    } else {

                        DocGetterViewHolder docGetterViewHolder = (DocGetterViewHolder) holder;
                        docGetterViewHolder.holder(messageViewModels.get(position), position);

                    }
                } else {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyDocMsgSenderViwHolder replyDocMsgSenderViwHolder = (ReplyDocMsgSenderViwHolder) holder;
                        replyDocMsgSenderViwHolder.holder(sendMessageViewModel, position);
                    } else {
                        DocSenderViewHolder docSenderViewHolder = (DocSenderViewHolder) holder;
                        docSenderViewHolder.holder(messageViewModels.get(position), position);
                    }
                }
            } else if (msgType == BaseCodeClass.variableType.Video_.getValue()) {

                if (userSender.equals(MessageActivity.senderId)) {


                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyVideoMsgGetterViwHolder replyVideoMsgGetterViwHolder = (ReplyVideoMsgGetterViwHolder) holder;
                        replyVideoMsgGetterViwHolder.holder(sendMessageViewModel, position);
                    } else {
                        VideoGetterViewHolder videoGetterViewHolder = (VideoGetterViewHolder) holder;
                        videoGetterViewHolder.holder(messageViewModels.get(position), position);
                    }
                } else {
                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyVideoMsgSenderViwHolder replyVideoMsgSenderViwHolder = (ReplyVideoMsgSenderViwHolder) holder;
                        replyVideoMsgSenderViwHolder.holder(sendMessageViewModel, position);
                    } else {
                        VideoSenderViewHolder videoSenderViewHolder = (VideoSenderViewHolder) holder;
                        videoSenderViewHolder.holder(messageViewModels.get(position), position);
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
            } else if (msgType == BaseCodeClass.variableType.Music_.getValue()) {

                if (userSender.equals(MessageActivity.senderId)) {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyMusicMsgGetterViwHolder replyMusicMsgGetterViwHolder = (ReplyMusicMsgGetterViwHolder) holder;
                        replyMusicMsgGetterViwHolder.holder(sendMessageViewModel, position);
                    } else {

                        MusicGetterViewHolder musicGetterViewHolder = (MusicGetterViewHolder) holder;
                        musicGetterViewHolder.holder(messageViewModels.get(position), position);

                    }
                } else {

                    if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {

                        ReplyMusicMsgSenderViwHolder replyMusicMsgSenderViwHolder = (ReplyMusicMsgSenderViwHolder) holder;
                        replyMusicMsgSenderViwHolder.holder(sendMessageViewModel, position);
                    } else {
                        MusicSenderViewHolder musicSenderViewHolder = (MusicSenderViewHolder) holder;
                        musicSenderViewHolder.holder(messageViewModels.get(position), position);
                    }
                }

            } else if (msgType == BaseCodeClass.variableType.time.getValue()) {

                DateHolder dateHolder = (DateHolder) holder;
                dateHolder.txtTime.setText(messageViewModels.get(position).getMessage1());

            } else if (msgType == 222) {
                WaitForUploadImageHolder waitForUploadImageHolder = (WaitForUploadImageHolder) holder;
                waitForUploadImageHolder.holder(bitmap, caption, position);

            } else if (msgType == 333) {
                WaitForUploadDocHolder waitForUploadDocHolder = (WaitForUploadDocHolder) holder;
                waitForUploadDocHolder.holder(docName, position);

            } else if (msgType == 444) {
                WaitForUploadMusicHolder waitForUploadMusicHolder = (WaitForUploadMusicHolder) holder;
                waitForUploadMusicHolder.holder(musicName, position);

            } else if (msgType == 555) {
                WaitForUploadVideoHolder waitForUploadVideoHolder = (WaitForUploadVideoHolder) holder;
                waitForUploadVideoHolder.holder(videoName, position);

            } else {
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
        } else if (msgType == BaseCodeClass.variableType.Video_.getValue()) {
            if (userSender.equals(MessageActivity.senderId)) {

                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return VIDEO_GETTER_REPLY;
                } else {
                    return VIDEO_GETTER;
                }
            } else {
                if (!StringUtils.textIsEmpty(messageViewModels.get(position).getReplyMsg())) {
                    return VIDEO_SENDER_REPLY;
                } else {
                    return VIDEO_SENDER;
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

        } else if (msgType == BaseCodeClass.variableType.time.getValue()) {

            return DATE_MESSAGE;

        } else if (msgType == 222) {
            return WAIT_FOR_UPLOAD;
        } else if (msgType == 333) {
            return WAIT_FOR_UPLOAD_DOC;

        } else if (msgType == 444) {
            return WAIT_FOR_UPLOAD_MUSIC;

        } else if (msgType == 555) {
            return WAIT_FOR_UPLOAD_VIDEO;

        } else {
            if (userSender.equals(MessageActivity.senderId)) {
                return LAKE_SUPPORT_GETTER;


            } else {
                return LAKE_SUPPORT_SENDER;
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull @NotNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) mContext, perms)) {

            new AppSettingsDialog.Builder((Activity) mContext).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(imgMsgRecParent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(imgMsgRecParent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {

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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(imgMsgSendParent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(imgMsgSendParent, messageData.getId(), messageData, messageData.getMsgType(), position);
                    }
                }
            });


        }

    }

    public class DocSenderViewHolder extends RecyclerView.ViewHolder {
        TextView txtDocName, txtTime, txtPersent;
        ImageView imgMessageTick;
        CardView cardRoot;
        ConstraintLayout parent;
        CircularImageView img;
        ProgressBar prg;

        public DocSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_layout_Doc_message_send);
            txtDocName = itemView.findViewById(R.id.txt_layout_Doc_message_send);
            imgMessageTick = itemView.findViewById(R.id.iv_layout_Doc_message_recived_tick);
            txtTime = itemView.findViewById(R.id.txtTime_layout_Doc_message_send);
            cardRoot = itemView.findViewById(R.id.cardRoot_LayoutDocMessageSend);
            parent = itemView.findViewById(R.id.constraint_layout_docSend_parent);
            prg = itemView.findViewById(R.id.prg_layout_Doc_message_recived);
            txtPersent = itemView.findViewById(R.id.txtPersent);


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

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Dehkadeh/" + messageData.getMessage1());

            if (file.exists()) {
                img.setImageResource(R.drawable.ic_doc);
                img.setContentDescription("downloaded");
            } else {
                img.setImageResource(R.drawable.ic_download_green);
                img.setContentDescription("not_downloaded");
            }

            img.setOnClickListener(v -> {
                if (img.getContentDescription().equals("downloaded")) {
                    openFile(mContext, file);


                } else {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadFile(prg, img, txtPersent, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())), "doc_sender");
                }

            });

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


                    }
                }
            });


        }
    }

    public class DocGetterViewHolder extends RecyclerView.ViewHolder {
        TextView txtDocName, txtTime, txtPersent;
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
            txtPersent = itemView.findViewById(R.id.txtPersent);


        }

        void holder(SendMessageViewModel messageData, int position) {
            txtDocName.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Dehkadeh/" + messageData.getMessage1());

            if (file.exists()) {
                imgDownload.setImageResource(R.drawable.ic_doc3);
                imgDownload.setContentDescription("downloaded");
            } else {
                imgDownload.setImageResource(R.drawable.ic_download2);
                imgDownload.setContentDescription("not_downloaded");
            }

           /* if (isDocDownload(Integer.parseInt(messageData.getId()))) {
                imgDownload.setImageResource(R.drawable.ic_doc3);
                prg.setVisibility(View.GONE);

            } else {
                imgDownload.setImageResource(R.drawable.ic_download2);
                prg.setVisibility(View.GONE);
            }*/

            imgDownload.setOnClickListener(v -> {
                if (imgDownload.getContentDescription().equals("downloaded")) {
                    openFile(mContext, file);


                } else {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadFile(prg, imgDownload, txtPersent, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())), "doc_geter");
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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
        TextView txtDocName, txtTime, txtPersent;
        ImageView imgMessageTick;
        CardView cardRoot;
        ConstraintLayout parent;
        ProgressBar prg;
        CircularImageView imageView;

        public MusicSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_layout_Doc_message_send);
            imgMessageTick = itemView.findViewById(R.id.iv_layout_Doc_message_recived_tick);
            txtTime = itemView.findViewById(R.id.txtTime_layout_Doc_message_send);
            cardRoot = itemView.findViewById(R.id.cardRoot_LayoutDocMessageSend);
            parent = itemView.findViewById(R.id.constraint_layout_docSend_parent);
            prg = itemView.findViewById(R.id.prg_layout_Doc_message_recived);
            txtPersent = itemView.findViewById(R.id.txtPersent);
            imageView = itemView.findViewById(R.id.img_layout_Doc_message_send);


        }

        void holder(SendMessageViewModel messageData, int position) {
            cr_send = imageView;
            cr_sendMap.put(position, imageView);
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

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Dehkadeh/" + messageData.getMessage1());

            if (file.exists()) {
                imageView.setImageResource(R.drawable.ic_play_music_green);
                imageView.setContentDescription("downloaded");
            } else {
                imageView.setImageResource(R.drawable.ic_download_green);
                imageView.setContentDescription("not_downloaded");
            }

            if (isPlayedMusic(Integer.parseInt(messageData.getId()))) {
                imageView.setImageResource(R.drawable.ic_pause_music_green);
            } else {
                imageView.setImageResource(R.drawable.ic_play_music_green);
            }

            imageView.setOnClickListener(v -> {
                if (imageView.getContentDescription().equals("downloaded")) {
                    if (!playSender) {
                        if (playGeter) {
                            stop(mediaPlayer, "music_geter");
                            initPlayedGetMusic();
                            playPosition = position;
                            yoYoString.stop(true);
                            playedList.add(Integer.parseInt(messageData.getId()));
                            playMusic(file.getAbsolutePath(), imageView, "music_sender");
                        } else {
                            playedList.add(Integer.parseInt(messageData.getId()));
                            playPosition = position;
                            playMusic(file.getAbsolutePath(), imageView, "music_sender");
                        }
                    } else {
                        initPlayedSendMusic();
                        imageView.setImageResource(R.drawable.ic_play_music_green);
                        yoYoString.stop(true);
                        stop(mediaPlayer, "music_sender");
                        playedList.remove(new Integer(Integer.parseInt(messageData.getId())));
                        playPosition = -1;
                    }

                } else {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadFile(prg, imageView, txtPersent, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())), "music_sender");
                }

            });

/*
            imageView.setOnClickListener(v -> {
                if (imageView.getContentDescription().equals("downloaded")) {
                    if (!playSender) {
                        if (playGeter) {
                            stop(mediaPlayer, "music_geter");
                            cr_get.setImageResource(R.drawable.ic_music_play);
                            yoYoString.stop(true);
                            playedList.add(Integer.parseInt(messageData.getId()));
                            playMusic(file.getAbsolutePath(), imageView, "music_sender");
                        }else {
                            playedList.add(Integer.parseInt(messageData.getId()));
                            playMusic(file.getAbsolutePath(), imageView, "music_sender");
                        }
                    } else {
                        imageView.setImageResource(R.drawable.ic_play_music_green);
                        yoYoString.stop(true);
                        stop(mediaPlayer, "music_sender");
                        playedList.remove(new Integer(Integer.parseInt(messageData.getId())));
                    }

                } else {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadFile(prg, imageView, txtPersent, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())), "music_sender");
                }

            });
*/

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


                    }
                }
            });


        }
    }

    public class MusicGetterViewHolder extends RecyclerView.ViewHolder {
        TextView txtDocName, txtTime, txtPersent;
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
            prg = itemView.findViewById(R.id.prg_layout_Doc_message_recived);
            txtPersent = itemView.findViewById(R.id.txtPersent);


        }

        void holder(SendMessageViewModel messageData, int position) {
            cr_get = imgDownload;
            cr_getMap.put(position, imgDownload);
            txtDocName.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTime.setText(time);

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Dehkadeh/" + messageData.getMessage1());
            if (file.exists()) {
                imgDownload.setImageResource(R.drawable.ic_music_play);
                imgDownload.setContentDescription("downloaded");
            } else {
                imgDownload.setImageResource(R.drawable.ic_download2);
                imgDownload.setContentDescription("not_downloaded");
            }


            if (isPlayedMusic(Integer.parseInt(messageData.getId()))) {
                imgDownload.setImageResource(R.drawable.ic_pause_yellow);
            } else {
                imgDownload.setImageResource(R.drawable.ic_music_play);
            }


            imgDownload.setOnClickListener(v -> {
                if (imgDownload.getContentDescription().equals("downloaded")) {
                    if (!playGeter) {
                        if (playSender) {
                            stop(mediaPlayer, "music_sender");
                            initPlayedSendMusic();
                            playPosition = position;
                            yoYoString.stop(true);
                            playedList.add(Integer.parseInt(messageData.getId()));
                            playMusic(file.getAbsolutePath(), imgDownload, "music_geter");
                        } else {
                            playMusic(file.getAbsolutePath(), imgDownload, "music_geter");
                            playedList.add(Integer.parseInt(messageData.getId()));
                            playPosition = position;
                        }
                    } else {
                        initPlayedGetMusic();
                        imgDownload.setImageResource(R.drawable.ic_music_play);
                        yoYoString.stop(true);
                        stop(mediaPlayer, "music_geter");
                        playedList.remove(new Integer(Integer.parseInt(messageData.getId())));
                        playPosition = -1;
                    }
                } else {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadFile(prg, imgDownload, txtPersent, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())), "music_geter");
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
                    }
                }
            });


        }
    }

    public class VideoSenderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTumb, imgPlay, imgSeenTick;
        TextView txtVideoName, txtTimeSend, txtPersent;
        RelativeLayout imgCancel;
        CircularProgressBar circularProgressBar;
        ConstraintLayout parent;

        public VideoSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTumb = itemView.findViewById(R.id.img_layout_imageMessageSent);
            imgPlay = itemView.findViewById(R.id.img_layout_imgPlay);
            imgCancel = itemView.findViewById(R.id.img_cancelUpload);
            imgSeenTick = itemView.findViewById(R.id.iv_imageMessage_sent_tick);
            txtVideoName = itemView.findViewById(R.id.txt_layout_imageMessageSent);
            txtTimeSend = itemView.findViewById(R.id.txtTime_layout_imageMessageSent);
            txtPersent = itemView.findViewById(R.id.txt_video_persent);
            circularProgressBar = itemView.findViewById(R.id.circularProgressBar);
            parent = itemView.findViewById(R.id.constraint_layout_videoMsgSend_parent);


        }

        void holder(SendMessageViewModel messageData, int position) {
            imgCancel.setVisibility(View.GONE);
            imgPlay.setVisibility(View.VISIBLE);
            imgSeenTick.setVisibility(View.VISIBLE);
            txtTimeSend.setVisibility(View.VISIBLE);
            circularProgressBar.setVisibility(View.GONE);

            Glide.with(mContext).load(generateThumNailUrl(Integer.parseInt(messageData.getId()))).placeholder(R.drawable.image_placeholder).into(imgTumb);
            txtVideoName.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSend.setText(time);
            switch (messageData.getStatus()) {
                case "1":
                    imgSeenTick.setImageResource(R.drawable.ic_tick);
                    break;
                case "2":
                    imgSeenTick.setImageResource(R.drawable.ic_tick_done);
                    break;
                case "3":
                    imgSeenTick.setImageResource(R.drawable.ic_tick_seen);
                    break;
            }

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Dehkadeh/" + messageData.getMessage1());
            if (file.exists()) {
                imgPlay.setImageResource(R.drawable.ic_play_icon);
                imgPlay.setContentDescription("downloaded");
            } else {
                imgPlay.setImageResource(R.drawable.donwloadvideo);
                imgPlay.setContentDescription("not_downloaded");
            }

            imgPlay.setOnClickListener(v -> {
                if (imgPlay.getContentDescription().equals("not_downloaded")) {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadVideo(circularProgressBar, imgPlay, txtPersent, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())));

                } else {
                    playVideo(file.getAbsolutePath());
                    /*Intent intent = new Intent(mContext, ActivityVideoPlay.class);
                    intent.putExtra("videoName",messageData.getMessage1());
                    mContext.startActivity(intent);*/
                }

            });

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            if (isDocDownload(Integer.parseInt(messageData.getId()))) {
                circularProgressBar.setVisibility(View.VISIBLE);
            } else {
                circularProgressBar.setVisibility(View.GONE);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


                    }
                }
            });

        }

    }

    public class VideoGetterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTumb, imgPlay;
        TextView txtVideoName, txtTimeSend, txtPersent;
        RelativeLayout imgCancel;
        CircularProgressBar circularProgressBar;
        ConstraintLayout parent;

        public VideoGetterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTumb = itemView.findViewById(R.id.img_layout_imageMessageSent);
            imgPlay = itemView.findViewById(R.id.img_layout_imgPlay);
            imgCancel = itemView.findViewById(R.id.img_cancelUpload);
            txtVideoName = itemView.findViewById(R.id.txt_layout_imageMessageSent);
            txtTimeSend = itemView.findViewById(R.id.txtTime_layout_imageMessageSent);
            txtPersent = itemView.findViewById(R.id.txt_video_persent);
            circularProgressBar = itemView.findViewById(R.id.circularProgressBar);
            parent = itemView.findViewById(R.id.constraint_layout_videoMsgSend_parent);


        }

        void holder(SendMessageViewModel messageData, int position) {
            imgCancel.setVisibility(View.GONE);
            imgPlay.setVisibility(View.VISIBLE);
            txtTimeSend.setVisibility(View.VISIBLE);
            circularProgressBar.setVisibility(View.GONE);

            Glide.with(mContext).load(generateThumNailUrl(Integer.parseInt(messageData.getId()))).placeholder(R.drawable.image_placeholder).into(imgTumb);
            txtVideoName.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSend.setText(time);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Dehkadeh/" + messageData.getMessage1());
            if (file.exists()) {
                imgPlay.setImageResource(R.drawable.ic_play_icon);
                imgPlay.setContentDescription("downloaded");
            } else {
                imgPlay.setImageResource(R.drawable.donwloadvideo);
                imgPlay.setContentDescription("not_downloaded");
            }

            imgPlay.setOnClickListener(v -> {
                if (imgPlay.getContentDescription().equals("not_downloaded")) {
                    downloadList.add(Integer.parseInt(messageData.getId()));
                    downloadVideo(circularProgressBar, imgPlay, txtPersent, messageData.getMessage1(), generateUrl(Integer.parseInt(messageData.getId())));

                } else {
                    playVideo(file.getAbsolutePath());
                   /* Intent intent = new Intent(mContext, ActivityVideoPlay.class);
                    intent.putExtra("videoName",messageData.getMessage1());
                    mContext.startActivity(intent);*/
                }

            });

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))) {
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            } else {
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            if (isDocDownload(Integer.parseInt(messageData.getId()))) {
                circularProgressBar.setVisibility(View.VISIBLE);
            } else {
                circularProgressBar.setVisibility(View.GONE);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


                    }
                }
            });

        }

    }

    private void playVideo(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        intent.setDataAndType(Uri.parse(path), "video/*");
        mContext.startActivity(intent);
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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

    public class ReplyVideoMsgSenderViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ImageView imagSeen, imgOld;
        ConstraintLayout parent;
        LinearLayout oldMessage;


        public ReplyVideoMsgSenderViwHolder(@NonNull View itemView) {
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

        void holder(SendMessageViewModel messageData, int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);
            String oldId = getOldMessageid(messageData.getReplyMsg());

            Glide.with(mContext).load(generateThumNailUrl(Integer.parseInt(oldId))).placeholder(R.drawable.image_placeholder).into(imgOld);
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

        void holder(SendMessageViewModel messageData, int position) {
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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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

    public class ReplyVideoMsgGetterViwHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtOldMsg, txtNewMsg, txtTimeSent;
        ImageView imgOld;
        ConstraintLayout parent;
        LinearLayout oldMessage;


        public ReplyVideoMsgGetterViwHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.tv_messageReply_nameUser);
            txtOldMsg = itemView.findViewById(R.id.tv_messageReply_oldValue);
            txtNewMsg = itemView.findViewById(R.id.tv_messageReply_replyValue);
            txtTimeSent = itemView.findViewById(R.id.tv_message_sent_time);
            parent = itemView.findViewById(R.id.layout_messageSentReply_parent);
            oldMessage = itemView.findViewById(R.id.rel_oldMessage);
            imgOld = itemView.findViewById(R.id.img_messageReply_oldValue);
        }

        void holder(SendMessageViewModel messageData, int position) {
            txtUserName.setText(messageData.getUserSender());
            txtNewMsg.setText(messageData.getMessage1());
            txtOldMsg.setText(getOldMessage(messageData.getReplyMsg()));

            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtTimeSent.setText(time);
            String oldId = getOldMessageid(messageData.getReplyMsg());

            Glide.with(mContext).load(generateThumNailUrl(Integer.parseInt(oldId))).placeholder(R.drawable.image_placeholder).into(imgOld);

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
                    onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);
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
                        onClickMessage.messageSelected(parent, messageData.getId(), messageData, messageData.getMsgType(), position);


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
            docWaitPosition = position;
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
            musicWaitPosition = position;
            txtMusicName.setText(docName);
        }

    }

    public class WaitForUploadVideoHolder extends RecyclerView.ViewHolder {
        ImageView imgTumb, imgPlay, imgSeenTick;
        RelativeLayout imgCancel;
        TextView txtVideoName, txtTimeSend;
        CircularProgressBar circularProgressBar;

        public WaitForUploadVideoHolder(@NonNull View itemView) {
            super(itemView);
            imgTumb = itemView.findViewById(R.id.img_layout_imageMessageSent);
            imgPlay = itemView.findViewById(R.id.img_layout_imgPlay);
            imgCancel = itemView.findViewById(R.id.img_cancelUpload);
            imgSeenTick = itemView.findViewById(R.id.iv_imageMessage_sent_tick);
            txtVideoName = itemView.findViewById(R.id.txt_layout_imageMessageSent);
            txtTimeSend = itemView.findViewById(R.id.txtTime_layout_imageMessageSent);
            circularProgressBar = itemView.findViewById(R.id.circularProgressBar);

        }

        void holder(String caption, int position) {
            imgCancel.setVisibility(View.VISIBLE);
            imgPlay.setVisibility(View.GONE);
            imgSeenTick.setVisibility(View.GONE);
            txtTimeSend.setVisibility(View.GONE);
            circularProgressBar.setVisibility(View.GONE);

            videoWaitPosition = position;
          /*  Glide.with(mContext).load(bitmap)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgMessageSend);*/


            txtVideoName.setText(caption);
            imgCancel.setOnClickListener(v -> {
                sendMessageVM.compositeDisposable.dispose();
                messageViewModels.remove(position);
                notifyItemRemoved(position);
            });
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

    private String generateThumNailUrl(int chatId) {
        String url = baseCodeClass.BASE_URL + "User/Downloadthumbnails?Chatid=" + chatId;
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

    public void initWaitValueVideo(String videoName, SendMessageVM sendMessageVM) {
        this.videoName = videoName;
        this.sendMessageVM = sendMessageVM;
    }

    private void downloadFile(ProgressBar progressBar, CircularImageView imageView, TextView txtPersent, String name, String link, String type) {
        if (EasyPermissions.hasPermissions(mContext, permission)) {

            download(progressBar, imageView, txtPersent, name, link, type);


        } else {
            EasyPermissions.requestPermissions((Activity) mContext, "Our App Requires a permission to access your storage", READ_STORAGE_PERMISSION_REQUEST, permission);

        }

    }

    private void download(ProgressBar progressBar, CircularImageView imageView, TextView txtPersent, String name, String link, String type) {
        progressBar.setVisibility(View.VISIBLE);
        txtPersent.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(link);
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("در حال دانلود");
        request.setDescription("لطفا منتظر بمانید...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Dehkadeh/" + name);

/*
        request.setDestinationInExternalFilesDir(mContext,getCacheDirectory(mContext).getPath(),"/chat/" + name);
*/

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
                            txtPersent.setText(" % " + percent);
                            if (percent == 100) {
                                Toast.makeText(mContext, "دانلود با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                timer.purge();
                                timer.cancel();
                                imageView.setContentDescription("downloaded");
                                progressBar.setVisibility(View.GONE);
                                txtPersent.setVisibility(View.GONE);
                                if (type.equals("doc_geter")) {
                                    imageView.setImageResource(R.drawable.ic_doc3);

                                } else if (type.equals("doc_sender")) {
                                    imageView.setImageResource(R.drawable.ic_doc);

                                } else if (type.equals("music_geter")) {
                                    imageView.setImageResource(R.drawable.ic_music_play);

                                } else if (type.equals("music_sender")) {
                                    imageView.setImageResource(R.drawable.ic_play_music_green);
                                }

                         /*       for (int item : downloadList) {
                                    if (Integer.parseInt(messageData.getId()) == item) {
                                        imageView.setImageResource(R.drawable.ic_doc3);
                                        imageView.setContentDescription("after");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }*/


                            }
                        }
                    });
                }


            }
        }, 0, 100);
    }


    private void downloadVideo(CircularProgressBar progressBar, ImageView imageView, TextView txtPersent, String name, String link) {
        if (EasyPermissions.hasPermissions(mContext, permission)) {

            download2(progressBar, imageView, txtPersent, name, link);


        } else {
            EasyPermissions.requestPermissions((Activity) mContext, "Our App Requires a permission to access your storage", READ_STORAGE_PERMISSION_REQUEST, permission);

        }

    }

    private void download2(CircularProgressBar progressBar, ImageView imageView, TextView txtPersent, String name, String link) {
        progressBar.setVisibility(View.VISIBLE);
        txtPersent.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(link);
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("در حال دانلود");
        request.setDescription("لطفا منتظر بمانید...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Dehkadeh/" + name);

/*
       request.setDestinationInExternalFilesDir(mContext, root, name);
*/

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
                            txtPersent.setText(" % " + percent);
                            if (percent == 100) {
                                Toast.makeText(mContext, "دانلود با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                timer.purge();
                                timer.cancel();
                                imageView.setContentDescription("downloaded");
                                progressBar.setVisibility(View.GONE);
                                txtPersent.setVisibility(View.GONE);
                                imageView.setImageResource(R.drawable.ic_play_icon);


                         /*       for (int item : downloadList) {
                                    if (Integer.parseInt(messageData.getId()) == item) {
                                        imageView.setImageResource(R.drawable.ic_doc3);
                                        imageView.setContentDescription("after");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }*/


                            }
                        }
                    });
                }


            }
        }, 0, 100);
    }

    /// set animation
    private void setAnimation(Techniques animation, Long duration, int repeat, View view) {
        yoYoString = YoYo.with(animation)
                .repeat(repeat)
                .duration(duration)
                .playOn(view);


    }

    /// opene file
    public void openFile(Context context, File file) {
        String typeFile = getTypeFile(file);

        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), typeFile);
            PackageManager pm = context.getPackageManager();
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType(typeFile);
            Intent openInChooser = Intent.createChooser(intent, "Choose");
            List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
            if (resInfo.size() > 0) {
                try {
                    context.startActivity(openInChooser);
                } catch (Throwable throwable) {
                    Toast.makeText(context, "برنامه ای که بتواند این فایل را باز کند پیدا نشد!", Toast.LENGTH_SHORT).show();
                    // PDF apps are not installed
                }
            } else {
                Toast.makeText(context, "برنامه ای که بتواند این فایل را باز کند پیدا نشد!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getTypeFile(File file) {

        if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
            // Word document
            return "application/msword";
        } else if (file.toString().contains(".pdf")) {
            // PDF file
            return "application/pdf";
        } else if (file.toString().contains(".ppt") || file.toString().contains(".pptx")) {
            // Powerpoint file
            return "application/vnd.ms-powerpoint";
        } else if (file.toString().contains(".xls") || file.toString().contains(".xlsx")) {
            // Excel file
            return "application/vnd.ms-excel";
        } else if (file.toString().contains(".zip")) {
            // ZIP file
            return "application/zip";
        } else if (file.toString().contains(".rar")) {
            // RAR file
            return "application/x-rar-compressed";
        } else if (file.toString().contains(".rtf")) {
            // RTF file
            return "application/rtf";
        } else if (file.toString().contains(".wav") || file.toString().contains(".mp3")) {
            // WAV audio file
            return "android.intent.action.MUSIC_PLAYER";
        } else if (file.toString().contains(".gif")) {
            // GIF file
            return "image/gif";
        } else if (file.toString().contains(".jpg") || file.toString().contains(".jpeg") || file.toString().contains(".png")) {
            // JPG file
            return "image/jpeg";
        } else if (file.toString().contains(".txt")) {
            // Text file
            return "text/plain";
        } else if (file.toString().contains(".3gp") || file.toString().contains(".mpg") ||
                file.toString().contains(".mpeg") || file.toString().contains(".mpe") || file.toString().contains(".mp4") || file.toString().contains(".avi")) {
            // Video files
            return "video/*";
        } else {
            return "*/*";
        }

    }

    private void playMusic(String root, CircularImageView imageView, String type) {
        try {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(root);
            mediaPlayer.prepare();
            mediaPlayer.start();
            setAnimation(Techniques.Tada, 1000L, YoYo.INFINITE, imageView);
            if (type.equals("music_sender")) {
                playSender = true;
                imageView.setImageResource(R.drawable.ic_pause_music_green);
            } else if (type.equals("music_geter")) {
                playGeter = true;
                imageView.setImageResource(R.drawable.ic_pause_yellow);

            }

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop(mp, type);
                    if (type.equals("music_sender")) {
                        imageView.setImageResource(R.drawable.ic_play_music_green);
                    } else if (type.equals("music_geter")) {
                        imageView.setImageResource(R.drawable.ic_music_play);

                    }


                }
            });
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void stop(MediaPlayer mediaPlayer, String type) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            if (type.equals("music_sender")) {
                playSender = false;

            } else if (type.equals("music_geter")) {
                playGeter = false;
            }
        }
    }


    public boolean isSelecetedMessage(int id) {
        for (int i : messageIdList) {
            if (i == id) {
                return true;
            }

        }
        return false;
    }

    public boolean isPlayedMusic(int id) {
        for (int i : playedList) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    private void initPlayedSendMusic() {
        for (Map.Entry<Integer, CircularImageView> entry : cr_sendMap.entrySet()) {
            int key = entry.getKey();
            CircularImageView value = entry.getValue();
            if (key == playPosition) {
                value.setImageResource(R.drawable.ic_play_music_green);
                stop(mediaPlayer, "music_sender");
                yoYoString.stop(true);
            }


        }
    }

    private void initPlayedGetMusic() {
        for (Map.Entry<Integer, CircularImageView> entry : cr_getMap.entrySet()) {
            int key = entry.getKey();
            CircularImageView value = entry.getValue();
            if (key == playPosition) {
                value.setImageResource(R.drawable.ic_music_play);
                stop(mediaPlayer, "music_geter");
                yoYoString.stop(true);
            }
        }
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
        void messageSelected(ConstraintLayout parent, String messageId, SendMessageViewModel messageData, int messageType, int position);

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

    private void createCatchDirectory(String root) {
        myFolderCatch = new File(root);
        if (!myFolderCatch.exists()) {
            myFolderCatch.mkdir();
        }
    }
}
