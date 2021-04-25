package com.example.koohestantest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.constants.EmployeeStatus;
import com.example.koohestantest1.model.EmployeeAdding;
import com.example.koohestantest1.model.IranProvince;
import com.example.koohestantest1.viewModel.CompanyViewModel;
import com.example.koohestantest1.viewModel.SendMessageVM;
import com.example.koohestantest1.viewModel.UserProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
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

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class MessageActivity extends AppCompatActivity implements MessageApi {
    public static final int PICK_IMAGE = 123;
    public static final int CAMERA_REQUEST_CODE = 5;
    public static final int CAMERA_PERMISSION_CODE = 101;
    ImageView imgMessage;
    private Bitmap mainBitmap = null;


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
    int pEmployee=EmployeeStatus.OPERATOR;

    MessageActivity messageActivity;

    String senderUser, getterUser;
    List<SendMessageViewModel> sendMessageViewModels = new ArrayList<>();

    Handler handler = new Handler();


    private CompanyViewModel companyViewModel;

    private TextView tvName, tvLastSeen;

    private int latestMsgSize;

    private UserProfileViewModel userProfileViewModel;

    private String TAG = MessageActivity.class.getSimpleName();

    public static String senderId;

    private int CURRENT_USER_ROLE = -1;
    private CircleImageView circleImageView;
    private SendMessageVM sendMessageVM;

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
            imgSendFile=findViewById(R.id.imgSendFile);
            setSupportActionBar(toolbar);
//
//            menuButton.setOnClickListener(v -> {
//                PopupMenu popupMenu = new PopupMenu(MessageActivity.this, menuButton);
//                popupMenu.getMenuInflater().inflate(R.menu.chat_menu, popupMenu.getMenu());
//
//                popupMenu.setOnMenuItemClickListener(item -> false);
//
//                popupMenu.show();
//            });

            messageActivity = this;

            mContext = MessageActivity.this;
            baseCodeClass = new BaseCodeClass();
            //current user (me)
            senderUser = getIntent().getStringExtra("sender");
            //another user Id(Friend)
            getterUser = getIntent().getStringExtra("getter");

            String url = baseCodeClass.pBASE_URL + "User/DownloadFile?UserID=" + getterUser + "&fileNumber=" + 1;
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

                if (!edMessage.getText().toString().isEmpty()) {
                    new MessageManagerClass(mContext, messageActivity).sendMessage(new SendMessageViewModel(baseCodeClass.getToken(), baseCodeClass.getUserID(), "", senderUser, getterUser,
                            edMessage.getText().toString(), "", "", "", 1, "", 1, 100));
                    edMessage.setText("");

                }
            });
            imgSendFile.setOnClickListener(v -> {
                CropImage.startPickImageActivity(MessageActivity.this);


            });
            new MessageManagerClass(mContext, this).getMessage(senderUser, getterUser);

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

    public void initMessageRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            layoutManager.setStackFromEnd(true);
            messageRecycler.setLayoutManager(layoutManager);
            latestMsgSize = sendMessageViewModels.size();
            adapter = new MessageRecyclerViewAdapter(mContext, sendMessageViewModels);
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
    public Single<GetResualt> uploadMessageImage(int MsgId, MultipartBody.Part file) {
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
            if (adapter == null) {
                sendMessageViewModels = sendMessageViewModelss;
                initMessageRecyclerView();
//            baseCodeClass.logMessage("onRes >> " + sendMessageViewModelss.size() , mContext);
            } else {
                sendMessageViewModels = sendMessageViewModelss;
                adapter.updateMessage(sendMessageViewModelss);

                for (SendMessageViewModel model:
                     sendMessageViewModelss) {
                    Log.d(TAG, "onResponseGetMessage: msg == " + model.getMessage1());
                    Log.d(TAG, "onResponseGetMessage: time == " + model.getDateSend());
                }
                /*
                handler for showing latest message at the end of list
                 */
                int nowItem = adapter.getItemCount();
                if (nowItem != latestMsgSize) {
                    messageRecycler.scrollToPosition(nowItem - 1);
                    latestMsgSize = nowItem;
                }
            }
            handler.postDelayed(() -> new MessageManagerClass(mContext, messageActivity).getMessage(senderUser, getterUser), 700);
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
                auEdtPosition =dialog.findViewById(R.id.atSetPosition);
                txtConfirmPosition = dialog.findViewById(R.id.txtConfirmPosition);
                radioGroup = dialog.findViewById(R.id.radios);

                auEdtPosition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus){
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, positionsTitle);
                            auEdtPosition.setAdapter(adapter);
                            auEdtPosition.showDropDown();
                        }
                    }
                });
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                      switch (checkedId){
                          case R.id.fullAccess:
                              pEmployee =EmployeeStatus.FULL_PERMISSION;
                              break;

                          case R.id.admin:
                              pEmployee =EmployeeStatus.ADMIN;
                              break;

                          case R.id.operator:
                              pEmployee =EmployeeStatus.OPERATOR;
                              break;
                      }
                    }
                });

                txtConfirmPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String positionTitle = auEdtPosition.getText().toString();
                        if (positionTitle.isEmpty()){
                            Toast.makeText(mContext, "سمت کارمند را مشخص کنید!", Toast.LENGTH_SHORT).show();
                        }else {
                            dialog.dismiss();
                            EmployeeAdding employeeAdding = new EmployeeAdding(baseCodeClass.getToken(), senderId, baseCodeClass.getCompanyID(),
                                    baseCodeClass.getMobileNumber(), "Note", "", "", positionTitle, pEmployee);
                            companyViewModel.callForAddingEmployeeToCo(employeeAdding);
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

                    SendImageMessageBottomSheetDialog sheetDialog =new SendImageMessageBottomSheetDialog(bitmap,mainBitmap,senderUser,getterUser);
                    sheetDialog.show(getSupportFragmentManager(),null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



}
