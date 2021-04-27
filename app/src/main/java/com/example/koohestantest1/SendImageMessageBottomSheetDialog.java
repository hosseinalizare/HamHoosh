package com.example.koohestantest1;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.viewModel.SendMessageVM;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SendImageMessageBottomSheetDialog extends BottomSheetDialogFragment {
    private View view;
    private ImageView imageView;
    private EditText editText;
    private ImageView imgCancel;
    FloatingActionButton fbSend;
    private Bitmap bitmap;
    private Bitmap mainBitmap = null;
    private String senderUser,getterUser;
    private BaseCodeClass baseCodeClass;
    private SendMessageVM sendMessageVM;
    private RelativeLayout progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          view =  inflater.inflate(R.layout.layout_send_image_message_bottom_sheet_dialog,container,false);

          imageView = view.findViewById(R.id.imgSendImageMessage_dialog);
          editText = view.findViewById(R.id.edtCaption_dialog);
          fbSend = view.findViewById(R.id.fab_dialog_sendMessage);
          imgCancel = view.findViewById(R.id.img_cancelMessage_dialog);
          progressBar = view.findViewById(R.id.relativeLayout_progressbar_sendImageMessage);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendMessageVM = new ViewModelProvider(requireActivity()).get(SendMessageVM.class);


    }

    public SendImageMessageBottomSheetDialog(Bitmap bitmap, Bitmap mainBitmap, String senderUser, String getterUser) {
        this.bitmap = bitmap;
        this.mainBitmap = mainBitmap;
        this.senderUser = senderUser;
        this.getterUser = getterUser;
        baseCodeClass = new BaseCodeClass();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(this).load(bitmap)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);


        imgCancel.setOnClickListener(v -> dismiss());

        fbSend.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            SendMessageViewModel sendMessageViewModel = new SendMessageViewModel(baseCodeClass.getToken(), baseCodeClass.getUserID(), "", senderUser, getterUser,
                    editText.getText().toString(), "", "", "", BaseCodeClass.variableType.Image_.getValue(), "", 1, 100);
            LiveData<GetResualt> resualtLiveData = sendMessageVM.sendMessage(sendMessageViewModel);
            resualtLiveData.observe(requireActivity(), new Observer<GetResualt>() {
                @Override
                public void onChanged(GetResualt getResualt) {
                    if (getResualt.getResualt().equals("100")){
                        sendImageMessage(Integer.parseInt(getResualt.getMsg()));

                    }
                }
            });



        });
        progressBar.setOnClickListener(v -> {

            sendMessageVM.compositeDisposable.dispose();
            progressBar.setVisibility(View.GONE);


        });
    }


    private void sendImageMessage(final int msgId) {
        try {

            Cache cache = new Cache(getContext());
            File file = cache.saveToCacheAndGetFile(mainBitmap,msgId+"" );

            Bitmap imageBitmap = new Compressor(getContext())
                    .setMaxWidth(1080)
                    .setMaxHeight(1080)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToBitmap(file);

            Cache cacheCompressed = new Cache(getContext());
            File compressedFile = cacheCompressed.saveToCacheAndGetFile(imageBitmap, msgId+"");

            RequestBody requestBody = RequestBody.create(compressedFile, MediaType.parse("multipart/form-data"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", compressedFile.getName(), requestBody);

            sendMessageVM.sendImageMessage(msgId,body).observe(this, new Observer<GetResualt>() {
                @Override
                public void onChanged(GetResualt getResualt) {
                    if (getResualt.getResualt().equals("100")){
                        progressBar.setVisibility(View.GONE);
                        dismiss();
                    }else {
                        Toast.makeText(getContext(),  "خطای نا شناخته", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } catch (Exception e) {
            Log.d("LOG",e.getMessage().toString());
        }

    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}