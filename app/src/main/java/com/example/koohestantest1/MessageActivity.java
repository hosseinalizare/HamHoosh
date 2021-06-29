package com.example.koohestantest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.Utils.FileUtils;
import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.activity.ActivityShowContact;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.example.koohestantest1.constants.EmployeeStatus;
import com.example.koohestantest1.model.DeleteMessageM;
import com.example.koohestantest1.model.EmployeeAdding;
import com.example.koohestantest1.model.ForwardMsgM;
import com.example.koohestantest1.viewModel.CompanyViewModel;
import com.example.koohestantest1.viewModel.SendMessageVM;
import com.example.koohestantest1.viewModel.UserProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.InfoDirectory.GetOnlineInformationClass;
import com.example.koohestantest1.InfoDirectory.MessageManagerClass;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.ViewModels.SendReportViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.MessageRecyclerViewAdapter;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class MessageActivity extends AppCompatActivity implements MessageApi, SendImageMessageBottomSheetDialog.OnclickOnFloatingButtonMessageBsheet, EasyPermissions.PermissionCallbacks, MessageRecyclerViewAdapter.OnClickMessage {
    public static final int PICK_IMAGE = 123;
    public static final int CAMERA_REQUEST_CODE = 5;
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CHOSE_DOC_REQUEST_CODE = 13870;
    public static final int CHOSE_MUSIC_REQUEST_CODE = 13880;
    public static final int CHOSE_VIDEO_REQUEST_CODE = 18480;
    public static final int READ_STORAGE_PERMISSION_REQUEST = 1387;
    ImageView imgMessage;
    private Bitmap mainBitmap = null;
    SendImageMessageBottomSheetDialog sheetDialog;
    private boolean isLoading = true;
    String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    String permission2 = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    AutoCompleteTextView auEdtPosition;
    TextView txtConfirmPosition;
    RadioGroup radioGroup;
    private List<String> positionsTitle;
    ImageView imgSendFile;

    FloatingActionButton btnSend;
    EditText edMessage;
    RecyclerView messageRecycler;
    ImageButton menuButton;
    Toolbar toolbar;
    Context mContext;
    BaseCodeClass baseCodeClass;
    MessageRecyclerViewAdapter adapter;
    int pEmployee = EmployeeStatus.OPERATOR;

    MessageActivity messageActivity;

    String senderUser, getterUser;
    List<SendMessageViewModel> sendMessageViewModels = new ArrayList<>();

    Handler handler = new Handler();
    LinearLayoutManager layoutManager;


    private CompanyViewModel companyViewModel;

    private TextView tvName, tvLastSeen;

    private int latestMsgSize;

    private UserProfileViewModel userProfileViewModel;

    private String TAG = MessageActivity.class.getSimpleName();

    public static String senderId;

    private int CURRENT_USER_ROLE = -1;
    private CircleImageView circleImageView;
    private SendMessageVM sendMessageVM;
    private ImageView imgDeleteMessage, imgForwardMessage, imgReplyMessage;
    private ConstraintLayout rootToolbarProfile;
    private RelativeLayout relReplyMessage;
    private ImageView imgCloseReplyMessage;
    private TextView txtUserNameReplyMessage, txtValueReplyMessage;
    private boolean isReplyMode = false;
    private String replyMessageId = "";
    private String messageId;
    private int replyMessageType;
    private SendOrderClass sendOrderClassData;
    private String lastUpdateTime = "2020-05-17T10:54:33.037";
    private Date dateTime;
    private String childDirectory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Log.d(TAG, "onCreate: ");

        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        sendMessageVM = new ViewModelProvider(this).get(SendMessageVM.class);

        positionsTitle = new ArrayList<>();
        positionsTitle.add("مدیر مالی");
        positionsTitle.add("مدیر فروش");
        positionsTitle.add("مدیر خرید");
        positionsTitle.add("مدیر تبلیغات");

        try {

            btnSend = findViewById(R.id.sendBtn);
            edMessage = findViewById(R.id.EdMessage);
            messageRecycler = findViewById(R.id.listOfMessage);
            toolbar = findViewById(R.id.AddProductToolbar);
//            menuButton = findViewById(R.id.btnMenu);
            tvLastSeen = findViewById(R.id.someDetailTxt);
            tvName = findViewById(R.id.profileNameTxt);
            circleImageView = findViewById(R.id.profilePhoto);
            imgSendFile = findViewById(R.id.imgSendFile);
            imgDeleteMessage = findViewById(R.id.img_deleteMessage);
            imgForwardMessage = findViewById(R.id.img_forwardMessage);
            imgReplyMessage = findViewById(R.id.img_replyMessage);
            relReplyMessage = findViewById(R.id.rel_activityMessage_relativeReplay);
            imgCloseReplyMessage = findViewById(R.id.img_activityMessage_imgCloseReplay);
            txtUserNameReplyMessage = findViewById(R.id.txt_activityMessage_txtUserNameReplay);
            txtValueReplyMessage = findViewById(R.id.txt_activityMessage_txtMessageValueReplay);
            rootToolbarProfile = findViewById(R.id.root_profileToolbar);
            setSupportActionBar(toolbar);

            messageActivity = this;


            mContext = MessageActivity.this;
            baseCodeClass = new BaseCodeClass();
            //current user (me)
            senderUser = getIntent().getStringExtra("sender");
            //another user Id(Friend)
            getterUser = getIntent().getStringExtra("getter");

            childDirectory = mContext.getResources().getString(R.string.app_name);


            String url = baseCodeClass.BASE_URL + "User/DownloadFile?UserID=" + getterUser + "&fileNumber=" + 1;
            Glide.with(this).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_profile).into(circleImageView);

            //get user role in order of showing menu items
            // default is ShowStoreActivity.COMPANY_USER
            CURRENT_USER_ROLE = getIntent().getIntExtra(ShowStoreActivity.STATE_MESSAGE_SENDER, ShowStoreActivity.COMPANY_USER);

            if (getterUser != null) {
                userProfileViewModel.callForUserProfile(baseCodeClass.getUserID(), baseCodeClass.getToken(), getterUser);
                senderId = getterUser;
            } else
                Toast.makeText(this, "خطایی رخ داده", Toast.LENGTH_SHORT).show();


            btnSend.setOnClickListener(v -> {
                if (!isReplyMode) {
                    if (!edMessage.getText().toString().isEmpty()) {
                        new MessageManagerClass(mContext, messageActivity).sendMessage(new SendMessageViewModel(baseCodeClass.getToken(), baseCodeClass.getUserID(), "", senderUser, getterUser,
                                edMessage.getText().toString(), "", "", "", BaseCodeClass.variableType.string_.getValue(), "", 1, 100));
                        edMessage.setText("");

                    }

                } else {

                    if (!edMessage.getText().toString().isEmpty()) {
                        SendMessageViewModel sendMessageViewModel = new SendMessageViewModel(baseCodeClass.getToken(), baseCodeClass.getUserID(), "", senderUser, getterUser,
                                edMessage.getText().toString(), "", "", replyMessageId, replyMessageType, "", 1, 100);
                        sendMessageVM.sendMessage(sendMessageViewModel).observe(MessageActivity.this, new Observer<GetResualt>() {
                            @Override
                            public void onChanged(GetResualt getResualt) {
                                edMessage.setText("");
                                relReplyMessage.setVisibility(View.GONE);
                                messageUnSelected(replyMessageId);
                            }
                        });

                    }


                }
            });


            imgSendFile.setOnClickListener(v -> {
                if (EasyPermissions.hasPermissions(this, permission, permission2)) {
                    showBottomSheetFilePicker();
                } else {

                    EasyPermissions.requestPermissions(this, "Our App Requires a permission to access your storage", READ_STORAGE_PERMISSION_REQUEST, permission, permission2);

                }


            });
            new MessageManagerClass(mContext, this).getMessage(senderUser, getterUser, lastUpdateTime);

            userProfileViewModel.getUserProfileLiveData().observe(this, userProfile -> {
                if (userProfile != null) {
                    //Showing the last seen time
                    String strLastSeen = userProfile.getLastSeen();
                    Date lastSeenDate = TimeUtils.convertStrToDate(strLastSeen);
                    Date now = new Date(System.currentTimeMillis());
                    String durationTime = TimeUtils.getDurationExpression(lastSeenDate, now);
                    tvLastSeen.setText(durationTime);

                    //showing user name
                    String lastName = userProfile.getLastName();
                    String firstName = userProfile.getFirstName();
                    if (lastName == null && firstName == null)
                        tvName.setText(userProfile.getMobilePhone());
                    else
                        tvName.setText(firstName + " " + lastName);
                }
            });

            userProfileViewModel.getErrorLiveData().observe(this, s -> {
                Toast.makeText(this, "خطایی رخ داده است.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCreate: " + s);
            });

            companyViewModel.getAddEmployeeResLive().observe(this, resualt -> {
                if (resualt.getResualt().equals("100")) {
                    Toast.makeText(this, "با موفقیت اضافه شد", Toast.LENGTH_SHORT).show();
                    invalidateOptionsMenu();
                    new GetOnlineInformationClass(this).companyEmployee(baseCodeClass.getCompanyID());
                }
            });
            companyViewModel.getErrorLive().observe(this, s -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("خطایی رخ داده است").append("\n").append(s);
                Toast.makeText(this, stringBuilder, Toast.LENGTH_SHORT).show();
            });

        } catch (Exception e) {
            logMessage(e.getMessage() + " >> 2", mContext);
        }
    }

    private void showBottomSheetFilePicker() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View viewBottomSheetDialog = getLayoutInflater().from(mContext).inflate(R.layout.layout_bottom_sheet_filepicker, null, false);
        RelativeLayout relPicture, relAudio, relFile, relVideo;
        relPicture = viewBottomSheetDialog.findViewById(R.id.rel_layoutBottomSheetFilePicker_Picture);
        relFile = viewBottomSheetDialog.findViewById(R.id.rel_layoutBottomSheetFilePicker_File);
        relAudio = viewBottomSheetDialog.findViewById(R.id.rel_layoutBottomSheetFilePicker_Audio);
        relVideo = viewBottomSheetDialog.findViewById(R.id.rel_layoutBottomSheetFilePicker_Video);

        bottomSheetDialog.setContentView(viewBottomSheetDialog);
        bottomSheetDialog.show();

        relPicture.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            CropImage.startPickImageActivity(MessageActivity.this);
        });
        relFile.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            Intent intent = new Intent(this, FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission(true)
                    .setMaxSelection(1)
                    .setSkipZeroSizeFiles(true)
                    .setShowImages(false)
                    .setSingleChoiceMode(true)
                    .setShowAudios(false)
                    .setShowVideos(false)
                    .setShowFiles(true)
                    .setSuffixes("txt", "pdf", "html", "rtf", "csv", "xml",
                            "zip", "tar", "gz", "rar", "7z", "torrent",
                            "doc", "docx", "odt", "ott",
                            "ppt", "pptx", "pps",
                            "xls", "xlsx", "ods", "ots")
                    .build());
            startActivityForResult(intent, CHOSE_DOC_REQUEST_CODE);


        });
        relAudio.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            Intent intent = new Intent(this, FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission(true)
                    .setMaxSelection(1)
                    .setSkipZeroSizeFiles(true)
                    .setShowImages(false)
                    .setSingleChoiceMode(true)
                    .setShowAudios(true)
                    .setShowVideos(false)
                    .setShowFiles(false)
                    .build());
            startActivityForResult(intent, CHOSE_MUSIC_REQUEST_CODE);

        });
        relVideo.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(this, FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission(true)
                    .setMaxSelection(1)
                    .setSkipZeroSizeFiles(true)
                    .setShowImages(false)
                    .setSingleChoiceMode(true)
                    .setShowAudios(false)
                    .setShowVideos(true)
                    .setShowFiles(false)
                    .build());
            startActivityForResult(intent, CHOSE_VIDEO_REQUEST_CODE);
        });

    }

    public void initMessageRecyclerView() {
        try {
            layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            layoutManager.setStackFromEnd(true);
            messageRecycler.setLayoutManager(layoutManager);
            latestMsgSize = sendMessageViewModels.size();
            adapter = new MessageRecyclerViewAdapter(mContext, sendMessageViewModels, this);
            messageRecycler.setAdapter(adapter);
        } catch (Exception e) {
            baseCodeClass.logMessage("Recy=>>" + e.getMessage(), mContext);
        }
    }

    @Override
    public Call<GetResualt> sendMessage(SendMessageViewModel sendMessage) {
        return null;
    }

    @Override
    public void onResponseSendMessage(GetResualt getResualt) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public Single<GetResualt> sendAMessage(SendMessageViewModel sendMessage) {
        return null;
    }

    @Override
    public Single<GetResualt> forwardMessage(ForwardMsgM forwardMsgM) {
        return null;
    }

    @Override
    public Single<GetResualt> uploadMessageImage(int MsgId, MultipartBody.Part file) {
        return null;
    }

    @Override
    public Single<GetResualt> sendThumbnail(int MsgId, MultipartBody.Part file) {
        return null;
    }

    @Override
    public Single<GetResualt> deleteMessage(DeleteMessageM deleteMessageM) {
        return null;
    }


    @Override
    public Call<GetResualt> sendReport(SendReportViewModel sendReportViewModel) {
        return null;
    }

    @Override
    public void onResponseSendReport(GetResualt getResualt) {

    }

    @Override
    public Call<List<SendMessageViewModel>> getMessage(SendMessageViewModel sendMessageViewModel) {
        return null;
    }

    @Override
    public void onResponseGetMessage(List<SendMessageViewModel> sendMessageViewModelss) {
        try {
            dateTime = TimeUtils.convertStrToDate(lastUpdateTime);
            Date date = dateTime;

            for (SendMessageViewModel model :
                    sendMessageViewModelss) {
                try {
                    date = TimeUtils.convertStrToDate(model.getDateSend());
                    if (date.getTime() > dateTime.getTime()) {
                        dateTime = date;
                    }

                } catch (Exception e) {

                }
            }
            if (dateTime != null) {
                lastUpdateTime = TimeUtils.getStringFromDate(dateTime);
            }


            if (adapter == null) {
                sendMessageViewModels = sendMessageViewModelss;
                initMessageRecyclerView();
            } else {

                adapter.updateMessage(sendMessageViewModelss);


                /*
                handler for showing latest message at the end of list
                 */
                int nowItem = adapter.getItemCount();
                if (nowItem != latestMsgSize) {
                    messageRecycler.scrollToPosition(nowItem - 1);
                    latestMsgSize = nowItem;
                }
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isLoading) {
                        new MessageManagerClass(mContext, messageActivity).getMessage(senderUser, getterUser, lastUpdateTime);
                    }
                }
            }, 700);


        } catch (Exception e) {
            logMessage("Message 100 >> " + e.getMessage(), mContext);
        }

    }

    @Override
    public Call<List<ContactListViewModel>> getContact(String token, String userID, String objectID) {
        return null;
    }

    @Override
    public void onResponseGetContact(List<ContactListViewModel> contactListViewModels) {

    }

    @Override
    public Call<List<ContactListViewModel>> getContactV2(String token, String userID, String objectID) {
        return null;
    }

    @Override
    public void onResponseGetContactV2(List<ContactListViewModel> contactListViewModels) {

    }

    @Override
    public Single<List<ContactListViewModel>> getcontacts(String token, String userID, String objectID) {
        return null;
    }

    @Override
    public Single<SendOrderClass> getOrderData(String orderId) {
        return null;
    }

    @Override
    public Call<SendOrderClass> getOrderData2(String orderId) {
        return null;
    }


    public void btnBack(View view) {
        finish();
    }

    public void menuBtn(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat_add_employee:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.layout_set_position_employee_dialog, null));
                builder.setCancelable(false);

                Dialog dialog = builder.create();
                dialog.show();
                auEdtPosition = dialog.findViewById(R.id.atSetPosition);
                txtConfirmPosition = dialog.findViewById(R.id.txtConfirmPosition);
                radioGroup = dialog.findViewById(R.id.radios);

                auEdtPosition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, positionsTitle);
                            auEdtPosition.setAdapter(adapter);
                            auEdtPosition.showDropDown();
                        }
                    }
                });
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.fullAccess:
                                pEmployee = EmployeeStatus.FULL_PERMISSION;
                                break;

                            case R.id.admin:
                                pEmployee = EmployeeStatus.ADMIN;
                                break;

                            case R.id.operator:
                                pEmployee = EmployeeStatus.OPERATOR;
                                break;
                        }
                    }
                });

                txtConfirmPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String positionTitle = auEdtPosition.getText().toString();
                        if (positionTitle.isEmpty()) {
                            Toast.makeText(mContext, "سمت کارمند را مشخص کنید!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            EmployeeAdding employeeAdding = new EmployeeAdding(baseCodeClass.getToken(), senderId, baseCodeClass.getCompanyID(),
                                    baseCodeClass.getMobileNumber(), "Note", "", "", positionTitle, pEmployee);
                            companyViewModel.callForAddingEmployeeToCo(employeeAdding);
                        }

                    }
                });

                break;

            case R.id.menu_chat_delete_chat_history:
                DeleteMessageM deleteMessageM = new DeleteMessageM(baseCodeClass.getToken(), senderUser, getterUser, null);

                sendMessageVM.deleteMessage(deleteMessageM).observe(this, new Observer<GetResualt>() {
                    @Override
                    public void onChanged(GetResualt getResualt) {
                        if (getResualt.getResualt().equals("100")) {
                            adapter.messageViewModels.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);

        String getSenderPersonEmployeeId = baseCodeClass.getEmployeeID(senderId);

        Log.d(TAG, "onCreateOptionsMenu: " + getSenderPersonEmployeeId);

        boolean DontshowMenu = false;
        try {
            if (!baseCodeClass.getPermissions().get(0).isState())
                DontshowMenu = true;
        } catch (Exception e) {

        }
        // it is regular user so don't show menu item adding
        if (CURRENT_USER_ROLE == ShowStoreActivity.REGULAR_USER) {
            removeAddEmployeeMenu(menu);
        }

        // it has been added as an employee so, don't show menu item adding

        if (!getSenderPersonEmployeeId.equals("false") || DontshowMenu) {
            removeAddEmployeeMenu(menu);
        }
        return true;
    }

    private void removeAddEmployeeMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_chat_add_employee);
        menuItem.setVisible(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

    }

    public void cropRequest(Uri uri) {
        try {
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(this);
        } catch (Exception e) {
            logMessage("AddProduct 399 >> " + e.getMessage(), this);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imgMessage.setImageBitmap(image);
            }

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);
                cropRequest(imageUri);
            }
        } catch (Exception e) {
            logMessage("AddProduct 603 >> " + e.getMessage(), this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    mainBitmap = bitmap;

                    sheetDialog = new SendImageMessageBottomSheetDialog(mainBitmap, this::onClickFloating);
                    sheetDialog.show(getSupportFragmentManager(), null);


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CHOSE_DOC_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {

                    ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                    Uri uri = files.get(0).getUri();
                    File file = FileUtils.getFile(mContext, uri);

                    long fileSizeInBytes = file.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;

                    sendDocMessage(files.get(0).getName(), file, uri);


                } catch (Exception e) {
                    Toast.makeText(mContext, "catch is run", Toast.LENGTH_SHORT).show();

                }


            }
        }
        if (requestCode == CHOSE_MUSIC_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {

                    ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                    Uri uri = files.get(0).getUri();
                    File file = FileUtils.getFile(mContext, uri);

                    long fileSizeInBytes = file.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;

                    sendMusicMessage(files.get(0).getName(), file, uri);


                } catch (Exception e) {
                    Toast.makeText(mContext, "catch is run", Toast.LENGTH_SHORT).show();

                }


            }
        }

        if (requestCode == CHOSE_VIDEO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {

                    ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                    Uri uri = files.get(0).getUri();
                    File file = FileUtils.getFile(mContext, uri);

                    long fileSizeInBytes = file.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;

                    sendVideoMessage(files.get(0).getName(), file, uri);


                } catch (Exception e) {
                    Toast.makeText(mContext, "catch is run", Toast.LENGTH_SHORT).show();

                }


            }
        }

    }


    private void sendImageMessage(final int msgId) {
        try {
/*
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
*/

            Cache cache = new Cache(mContext);
            File file = cache.saveToCacheAndGetFile(mainBitmap, msgId + "");

            Bitmap imageBitmap = new Compressor(mContext)
                    .setMaxWidth(1080)
                    .setMaxHeight(1080)
                    .setQuality(50)
                    .compressToBitmap(file);

            Cache cacheCompressed = new Cache(mContext);
            File compressedFile = cacheCompressed.saveToCacheAndGetFile(imageBitmap, msgId + "");

            RequestBody requestBody = RequestBody.create(compressedFile, MediaType.parse("multipart/form-data"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", compressedFile.getName(), requestBody);

            sendMessageVM.sendImageMessage(msgId, body).observe(this, new Observer<GetResualt>() {
                @Override
                public void onChanged(GetResualt getResualt) {
                    if (getResualt.getResualt().equals("100")) {

                        isLoading = true;
                        adapter.messageViewModels.remove(adapter.imgWaitPosition);
                        adapter.notifyItemRemoved(adapter.imgWaitPosition);
                        adapter.notifyItemRangeChanged(adapter.imgWaitPosition, adapter.messageViewModels.size());
                        new MessageManagerClass(mContext, messageActivity).getMessage(senderUser, getterUser, "");

                    } else {
                        Toast.makeText(mContext, "خطای نا شناخته", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } catch (Exception e) {
            Log.d("LOG", e.getMessage().toString());
        }

    }

    private void uploadFile(File file, Uri fileUri, final int msgId, String type, Bitmap thumbnail) {
        if (type.equals("video") && thumbnail != null) {

            Cache cache = new Cache(mContext);
            File thumbnailFile = cache.saveToCacheAndGetFile(thumbnail, msgId + "");

            RequestBody requestBody = RequestBody.create(thumbnailFile, MediaType.parse("multipart/form-data"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", thumbnailFile.getName(), requestBody);

            sendMessageVM.sendThumbnail(msgId, body).observe(this, new Observer<GetResualt>() {
                @Override
                public void onChanged(GetResualt getResualt) {
                    if (getResualt.getResualt().equals("100")) {
                        sendToServer(file, msgId, type, thumbnail);
                    }

                }
            });

        } else {
            sendToServer(file, msgId, type, thumbnail);

        }
    }

    private void sendToServer(File file, int msgId, String type, Bitmap thumbnail) {
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("*/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        sendMessageVM.sendDocMessage(msgId, body).observe(this, getResualt -> {

            if (getResualt.getResualt().equals("100")) {

                //////// copy file in directory after upload

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + childDirectory + "/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String fullName = path + file.getName();
                File file1 = new File(fullName);

                try {
                    copySingleFile(file, file1);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                isLoading = true;
                if (type.equals("doc")) {
                    adapter.messageViewModels.remove(adapter.docWaitPosition);
                    adapter.notifyItemRemoved(adapter.docWaitPosition);
                    adapter.notifyItemRangeChanged(adapter.docWaitPosition, adapter.messageViewModels.size());
                } else if (type.equals("music")) {
                    adapter.messageViewModels.remove(adapter.musicWaitPosition);
                    adapter.notifyItemRemoved(adapter.musicWaitPosition);
                    adapter.notifyItemRangeChanged(adapter.musicWaitPosition, adapter.messageViewModels.size());
                } else if (type.equals("video")) {
                    adapter.messageViewModels.remove(adapter.videoWaitPosition);
                    adapter.notifyItemRemoved(adapter.videoWaitPosition);
                    adapter.notifyItemRangeChanged(adapter.videoWaitPosition, adapter.messageViewModels.size());
                }
                new MessageManagerClass(mContext, messageActivity).getMessage(senderUser, getterUser, "");
                Toast.makeText(mContext, "فایل با موفقیت آپلود شد", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(mContext, "خطای نا شناخته", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClickFloating(String imgCaption) {
        isLoading = false;
        sheetDialog.dismiss();
        SendMessageViewModel sendMessageViewModel2 = new SendMessageViewModel(senderUser, 222);
        adapter.messageViewModels.add(sendMessageViewModel2);
        adapter.initWaitValue(mainBitmap, imgCaption, sendMessageVM);
        messageRecycler.setAdapter(adapter);

        SendMessageViewModel sendMessageViewModel = new SendMessageViewModel(baseCodeClass.getToken(), baseCodeClass.getUserID(), "", senderUser, getterUser,
                imgCaption, "", "", "", BaseCodeClass.variableType.Image_.getValue(), "", 1, 100);
        LiveData<GetResualt> resualtLiveData = sendMessageVM.sendMessage(sendMessageViewModel);
        resualtLiveData.observe(this, getResualt -> {
            if (getResualt.getResualt().equals("100")) {
                sendImageMessage(Integer.parseInt(getResualt.getMsg()));
            }
        });
    }

    private void sendDocMessage(String docName, File file, Uri fileUri) {
        isLoading = false;
        SendMessageViewModel sendMessageViewModel2 = new SendMessageViewModel(senderUser, 333);
        adapter.messageViewModels.add(sendMessageViewModel2);
        adapter.initWaitValueDoc(docName, sendMessageVM);
        messageRecycler.setAdapter(adapter);
        SendMessageViewModel sendMessageViewModel = new SendMessageViewModel(baseCodeClass.getToken(), baseCodeClass.getUserID(), "", senderUser, getterUser,
                docName, "", "", "", BaseCodeClass.variableType.File_.getValue(), "", 1, 100);
        LiveData<GetResualt> resualtLiveData = sendMessageVM.sendMessage(sendMessageViewModel);
        resualtLiveData.observe(this, getResualt -> {
            if (getResualt.getResualt().equals("100")) {
                uploadFile(file, fileUri, Integer.parseInt(getResualt.getMsg()), "doc", null);
            }
        });

    }

    private void sendMusicMessage(String MusicName, File file, Uri fileUri) {
        isLoading = false;
        SendMessageViewModel sendMessageViewModel2 = new SendMessageViewModel(senderUser, 444);
        adapter.messageViewModels.add(sendMessageViewModel2);
        adapter.initWaitValueMusic(MusicName, sendMessageVM);
        messageRecycler.setAdapter(adapter);
        SendMessageViewModel sendMessageViewModel = new SendMessageViewModel(baseCodeClass.getToken(), baseCodeClass.getUserID(), "", senderUser, getterUser,
                MusicName, "", "", "", BaseCodeClass.variableType.Music_.getValue(), "", 1, 100);
        LiveData<GetResualt> resualtLiveData = sendMessageVM.sendMessage(sendMessageViewModel);
        resualtLiveData.observe(this, getResualt -> {
            if (getResualt.getResualt().equals("100")) {
                uploadFile(file, fileUri, Integer.parseInt(getResualt.getMsg()), "music", null);
            }
        });

    }

    private void sendVideoMessage(String videoName, File file, Uri fileUri) {
        isLoading = false;
        ///// create video thumbnail
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);

        SendMessageViewModel sendMessageViewModel2 = new SendMessageViewModel(senderUser, 555);
        adapter.messageViewModels.add(sendMessageViewModel2);
        adapter.initWaitValueVideo(thumb, videoName, sendMessageVM);
        messageRecycler.setAdapter(adapter);
        SendMessageViewModel sendMessageViewModel = new SendMessageViewModel(baseCodeClass.getToken(), baseCodeClass.getUserID(), "", senderUser, getterUser,
                videoName, "", "", "", BaseCodeClass.variableType.Video_.getValue(), "", 1, 100);
        LiveData<GetResualt> resualtLiveData = sendMessageVM.sendMessage(sendMessageViewModel);
        resualtLiveData.observe(this, getResualt -> {
            if (getResualt.getResualt().equals("100")) {
                uploadFile(file, fileUri, Integer.parseInt(getResualt.getMsg()), "video", thumb);
            }
        });
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        showBottomSheetFilePicker();

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {

            new AppSettingsDialog.Builder(this).build().show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void messageSelected(ConstraintLayout parent, String messageId, SendMessageViewModel messageData, int messageType, int position, String senderMsgId) {
        imgForwardMessage.setVisibility(View.VISIBLE);
        imgDeleteMessage.setVisibility(View.VISIBLE);
        imgReplyMessage.setVisibility(View.VISIBLE);
        rootToolbarProfile.setVisibility(View.GONE);
        adapter.messageIdList.add(Integer.parseInt(messageId));
        adapter.deletePositionList.add(position);

        if (adapter.messageIdList.size() > 1) {
            imgReplyMessage.setVisibility(View.GONE);
            relReplyMessage.setVisibility(View.GONE);
            isReplyMode = false;
        }


        imgDeleteMessage.setOnClickListener(v -> {
            DeleteMessageM deleteMessageM = new DeleteMessageM(baseCodeClass.getToken(), senderUser, getterUser, adapter.messageIdList);
            sendMessageVM.deleteMessage(deleteMessageM).observe(this, new Observer<GetResualt>() {
                @Override
                public void onChanged(GetResualt getResualt) {
                    if (getResualt.getResualt().equals("100")) {
                        for (int position : adapter.deletePositionList) {
                            adapter.messageViewModels.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, adapter.messageViewModels.size());
                        }

                        adapter.messageIdList.clear();
                        adapter.deletePositionList.clear();
                        imgForwardMessage.setVisibility(View.GONE);
                        imgDeleteMessage.setVisibility(View.GONE);
                        imgReplyMessage.setVisibility(View.GONE);
                        rootToolbarProfile.setVisibility(View.VISIBLE);

                    }

                }
            });
        });
        imgReplyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReplyMode = true;
                replyMessageId = messageData.getId();
                replyMessageType = generateMsgType(messageType);
                relReplyMessage.setVisibility(View.VISIBLE);
                txtUserNameReplyMessage.setText(messageData.getUserSender());
                txtValueReplyMessage.setText(messageData.getMessage1());
            }
        });
        imgCloseReplyMessage.setOnClickListener(v -> {
            isReplyMode = false;
            relReplyMessage.setVisibility(View.GONE);
        });

        imgForwardMessage.setOnClickListener(v -> {
            try {

                Intent intent = new Intent(mContext, ActivityShowContact.class);
                intent.putExtra("msgId", messageId);
                /*intent.putExtra("senderMsgId", senderUser);*/
                intent.putExtra("senderMsgId", senderUser);
                startActivity(intent);
                finish();
                messageUnSelected(messageId);
                parent.setBackgroundColor(Color.TRANSPARENT);

            } catch (Exception e) {
                Toast.makeText(mContext, "MessageActivity forwardMessage >>>  " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void messageUnSelected(String id) {
        int i = 0;
        for (int item : adapter.messageIdList) {
            if (Integer.parseInt(id) == item) {
                adapter.messageIdList.remove(i);
                adapter.deletePositionList.remove(i);
            }
            i++;
        }
        if (adapter.messageIdList.size() == 0) {
            adapter.selectedFalse();
            imgForwardMessage.setVisibility(View.GONE);
            imgDeleteMessage.setVisibility(View.GONE);
            imgReplyMessage.setVisibility(View.GONE);
            relReplyMessage.setVisibility(View.GONE);
            rootToolbarProfile.setVisibility(View.VISIBLE);
            isReplyMode = false;
        }

        if (adapter.messageIdList.size() == 1) {
            isReplyMode = true;
            imgReplyMessage.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void scrollToCertainPosition(int position) {
        messageRecycler.smoothScrollToPosition(position);
    }

    @Override
    public void userForwarderClicked(String userId) {
        Intent intent = getIntent();
        // getter = Another user
        intent.putExtra("getter", userId);
        // sender = Ourselves
        intent.putExtra("sender", baseCodeClass.getCompanyID());
        startActivity(intent);

    }

    private int generateMsgType(int msgType) {

        for (BaseCodeClass.variableType type : BaseCodeClass.variableType.values()) {
            if (type.getValue() == msgType) {
                return type.getValue();
            }
        }

        return -1;

    }


    @Override
    protected void onPause() {
        super.onPause();

        try {
            if (adapter.mediaPlayer != null) {
                if (adapter.mediaPlayer.isPlaying())
                    adapter.mediaPlayer.stop();
                adapter.mediaPlayer.release();
                adapter.mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void copySingleFile(File sourceFile, File destFile)
            throws IOException {
        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            sourceChannel = new FileInputStream(sourceFile).getChannel();
            destChannel = new FileOutputStream(destFile).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
        } finally {
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            if (destChannel != null) {
                destChannel.close();
            }
        }
    }
}
