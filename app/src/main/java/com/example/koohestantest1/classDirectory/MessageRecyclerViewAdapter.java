package com.example.koohestantest1.classDirectory;

import android.annotation.SuppressLint;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.viewModel.SendMessageVM;
import com.mikhaellopez.circularimageview.CircularImageView;

import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public List<SendMessageViewModel> messageViewModels;
    private String TAG = MessageRecyclerViewAdapter.class.getSimpleName();
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    public  static boolean WAIT_FOR_UPLOAD_IMAGE  = false;
    private Bitmap bitmap;
    private String caption;
    private SendMessageVM sendMessageVM;
    private String docName;
    private File  myDir;
    private String root;
    private File myFile;
    private final int SENDER = 0;
    private final int GETTER = 1;
    private final int IMAGE_SENDER = 2;
    private final int IMAGE_GETTER = 3;
    private final int DOC_SENDER = 4;
    private final int DOC_GETTER = 5;
    private final int WAIT_FOR_UPLOAD = 100;
    private final int WAIT_FOR_UPLOAD_DOC = 200;

    OnClickMessage onClickMessage;
    public List<Integer> messageIdList;
    private List<Integer> downloadList;
    private boolean isSelectedMode = false;

    public MessageRecyclerViewAdapter(Context mContext, List<SendMessageViewModel> messageViewModels, OnClickMessage onClickMessage) {
        this.mContext = mContext;
        this.messageViewModels = messageViewModels;
        this.onClickMessage = onClickMessage;
        messageIdList = new ArrayList<>();
        downloadList = new ArrayList<>();
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
        myDir = new File(root );
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
        } else if (viewType == IMAGE_SENDER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_message_sent, parent, false);
            return new ImageSenderViewHolder(view);
        }else if (viewType == DOC_GETTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doc_message_recived, parent, false);
            return new DocGetterViewHolder(view);
        }else if (viewType == DOC_SENDER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doc_message_send, parent, false);
            return new DocSenderViewHolder(view);
        }else if (viewType == WAIT_FOR_UPLOAD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wait_for_send_image_message_sent, parent, false);
            return new WaitForUploadImageHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wait_for_send_doc_message_sent, parent, false);
            return new WaitForUploadDocHolder(view);
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
            }else if (msgType == BaseCodeClass.variableType.File_.getValue()){
                if (userSender.equals(MessageActivity.senderId)) {
                    DocGetterViewHolder docGetterViewHolder = (DocGetterViewHolder) holder;
                    docGetterViewHolder.holder(messageViewModels.get(position));

                } else {
                    DocSenderViewHolder docSenderViewHolder = (DocSenderViewHolder) holder;
                    docSenderViewHolder.holder(messageViewModels.get(position),position);
                }
            }else if (msgType ==222){
                WaitForUploadImageHolder waitForUploadImageHolder = (WaitForUploadImageHolder) holder;
                waitForUploadImageHolder.holder(bitmap,caption,position);

            }else if (msgType == 333){
                WaitForUploadDocHolder waitForUploadDocHolder = (WaitForUploadDocHolder) holder;
                waitForUploadDocHolder.holder(docName,position);

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
        }else if (msgType == BaseCodeClass.variableType.File_.getValue()){
            if (userSender.equals(MessageActivity.senderId)){
                return DOC_GETTER;
            }else {
                return DOC_SENDER;
            }
        }

        else if (msgType ==222){
            return WAIT_FOR_UPLOAD;
        }else if (msgType == 333){
            return WAIT_FOR_UPLOAD_DOC;

        }else return 656;


      /*  if (messageViewModels.get(position).getUserSender().equals(MessageActivity.senderId)) {
            return GETTER;
        } else {
            return SENDER;
        }*/
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

        void holder(SendMessageViewModel messageData) {
            textReceived.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            timeReceived.setText(time);

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            }else{
                parent.setBackgroundColor(Color.TRANSPARENT);
            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent,messageData.getId(),messageData);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode){
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    }else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent,messageData.getId(),messageData);


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

        void holder(SendMessageViewModel messageData) {
            Glide.with(mContext).load(generateUrl(Integer.parseInt(messageData.getId()))).placeholder(R.drawable.placeholder).into(imgMessageRecived);
            txtImageMessageRecived.setText(messageData.getMessage1());
            String time = TimeUtils.getCleanHourAndMinByStringV2(messageData.getDateSend());
            txtImageMessageTimeRecived.setText(time);
            imgMessageRecived.setOnClickListener(v -> showFullScreenImage(messageData.getId()));

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                imgMsgRecParent.setBackgroundColor(Color.parseColor("#81BDC6"));
            }else{
                imgMsgRecParent.setBackgroundColor(Color.TRANSPARENT);
            }

            imgMsgRecParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    imgMsgRecParent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(imgMsgRecParent,messageData.getId(),messageData);
                    return true;
                }
            });
            imgMsgRecParent.setOnClickListener(v -> {
                if (isSelectedMode){
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                        imgMsgRecParent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    }else {
                        imgMsgRecParent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(imgMsgRecParent,messageData.getId(),messageData);


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


            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            }else{
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent,messageData.getId(),messageData);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode){
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    }else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent,messageData.getId(),messageData);
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

        void holder(SendMessageViewModel messageData) {
            Glide.with(mContext).load(generateUrl(Integer.parseInt(messageData.getId()))).placeholder(R.drawable.placeholder).into(imgMessageSend);
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

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                imgMsgSendParent.setBackgroundColor(Color.parseColor("#81BDC6"));
            }else{
                imgMsgSendParent.setBackgroundColor(Color.TRANSPARENT);

            }

            imgMsgSendParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    imgMsgSendParent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(imgMsgSendParent,messageData.getId(),messageData);
                    return true;
                }
            });
            imgMsgSendParent.setOnClickListener(v -> {
                if (isSelectedMode){
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                        imgMsgSendParent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    }else {
                        imgMsgSendParent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(imgMsgSendParent,messageData.getId(),messageData);
                    }
                }
            });




        }

    }
    public class DocSenderViewHolder extends RecyclerView.ViewHolder{
        TextView txtDocName,txtTime;
        ImageView imgMessageTick;
        CardView cardRoot;
        ConstraintLayout parent;

        public DocSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_layout_Doc_message_send);
            imgMessageTick = itemView.findViewById(R.id.iv_layout_Doc_message_recived_tick);
            txtTime = itemView.findViewById(R.id.txtTime_layout_Doc_message_send);
            cardRoot = itemView.findViewById(R.id.cardRoot_LayoutDocMessageSend);
            parent=itemView.findViewById(R.id.constraint_layout_docSend_parent);

        }

        void holder(SendMessageViewModel messageData,int position){
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

            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            }else{
                parent.setBackgroundColor(Color.TRANSPARENT);

            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent,messageData.getId(),messageData);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode){
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    }else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent,messageData.getId(),messageData);


                    }
                }
            });


        }
    }

    public class DocGetterViewHolder extends RecyclerView.ViewHolder{
        TextView txtDocName,txtTime;
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

        void holder(SendMessageViewModel messageData){
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

            if (isDocDownload(Integer.parseInt(messageData.getId()))){
                imgDownload.setImageResource(R.drawable.ic_doc3);
                prg.setVisibility(View.GONE);

            }else{
                imgDownload.setImageResource(R.drawable.ic_download2);
                prg.setVisibility(View.GONE);
            }

           imgDownload.setOnClickListener(v -> {
               if (imgDownload.getContentDescription().equals("after")){
                   Toast.makeText(mContext, "فایل قبلا دانلود شده است.", Toast.LENGTH_SHORT).show();

               }else {
                   downloadList.add(Integer.parseInt(messageData.getId()));
                   downloadDoc(prg,imgDownload,messageData.getMessage1(),generateUrl(Integer.parseInt(messageData.getId())),messageData);
               }

           });


            if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                parent.setBackgroundColor(Color.parseColor("#81BDC6"));
            }else{
                parent.setBackgroundColor(Color.TRANSPARENT);
            }

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectedMode = true;
                    parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                    onClickMessage.messageSelected(parent,messageData.getId(),messageData);
                    return true;
                }
            });
            parent.setOnClickListener(v -> {
                if (isSelectedMode){
                    if (isSelecetedMessage(Integer.parseInt(messageData.getId()))){
                        parent.setBackgroundColor(Color.TRANSPARENT);
                        onClickMessage.messageUnSelected(messageData.getId());
                    }else {
                        parent.setBackgroundColor(Color.parseColor("#81BDC6"));
                        onClickMessage.messageSelected(parent,messageData.getId(),messageData);


                    }
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

        void holder(Bitmap bitmap,String caption,int position) {
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

        void holder(String docName,int position) {
            txtDocName.setText(docName);
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


    public void initWaitValue(Bitmap bitmap ,String caption,SendMessageVM sendMessageVM){
        this.bitmap = bitmap;
        this.caption = caption;
        this.sendMessageVM = sendMessageVM;
    }
    public void initWaitValueDoc(String docName,SendMessageVM sendMessageVM){
        this.docName = docName;
        this.sendMessageVM = sendMessageVM;
    }

    private void downloadDoc(ProgressBar progressBar,CircularImageView imageView,String name,String link,SendMessageViewModel messageData){
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

        request.setDestinationInExternalFilesDir(mContext,root,name);

        final long id = downloadManager.enqueue(request);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()){
                    long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    long totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    final int percent = (int) ((downloadedBytes * 100) / totalBytes);
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(percent);
                            if (percent ==100){
                                Toast.makeText(mContext, "دانلود با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                timer.purge();
                                timer.cancel();
                                for (int item :downloadList){
                                    if (Integer.parseInt(messageData.getId()) ==item){
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
        },0,100);
    }

    private void openFile(File url) {

        try {

/*
            Uri uri = Uri.fromFile(url);
*/
            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider",url);
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
            } else if (url.toString().contains(".rar")){
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
            ((AppCompatActivity)mContext).startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public boolean isSelecetedMessage(int id){
        for (int i:messageIdList){
            if (i ==id){
                return true;
            }

        }
        return false;
    }
    public void selectedFalse(){
        isSelectedMode = false;
    }

    public boolean isDocDownload(int id){
        for (int i:downloadList){
            if (i ==id){
                return true;
            }

        }
        return false;
    }


    public interface OnClickMessage {
        void messageSelected(ConstraintLayout parent,String messageId,SendMessageViewModel messageData);
        void messageUnSelected(String id);
    }





}
