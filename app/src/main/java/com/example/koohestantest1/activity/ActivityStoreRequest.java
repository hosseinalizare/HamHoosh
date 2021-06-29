package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.InfoDirectory.MessageManagerClass;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.ValidationCheck;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.ViewModels.SendReportViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.example.koohestantest1.model.DeleteMessageM;
import com.example.koohestantest1.model.ForwardMsgM;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class ActivityStoreRequest extends AppCompatActivity implements MessageApi {
    private Button btnSendRequest;
    private EditText edtPhone, edtMessage;
    private MessageManagerClass messageManagerClass;
    private BaseCodeClass baseCodeClass;
    private ViewFlipper vf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_request);
        btnSendRequest = findViewById(R.id.sendRequestBtn);
        edtPhone = findViewById(R.id.txtMobile);
        edtMessage = findViewById(R.id.edMessage);
        vf = findViewById(R.id.StoreRequestVf);

        messageManagerClass = new MessageManagerClass(ActivityStoreRequest.this, this);
        baseCodeClass = new BaseCodeClass();


        btnSendRequest.setOnClickListener(v -> {
            try {
                if (edtPhone.getText().toString().isEmpty() || edtMessage.getText().toString().isEmpty()){
                    Toast.makeText(ActivityStoreRequest.this, "پر کردن تمامی فیلدها الزامی است!", Toast.LENGTH_SHORT).show();
                }else {

                    if (ValidationCheck.mobileValidation(edtPhone.getText().toString()) || ValidationCheck.phoneValidation(edtPhone.getText().toString()) ){
                        sendRequest();
                    }else {
                        Toast.makeText(this, "شماره تلفن نا معتبر است", Toast.LENGTH_LONG).show();

                    }



                }

            } catch (Exception e) {
                logMessage("MyStoreFragment 300 >> " + e.getMessage(), ActivityStoreRequest.this);
            }
        });
    }

    private void sendRequest() {
        messageManagerClass.sendReport(new SendReportViewModel(baseCodeClass.getUserID(), baseCodeClass.getToken(), "8",
                edtMessage.getText().toString(), "1", baseCodeClass.getUserID(), "درخواست ثبت فروشگاه", "",
                "", edtPhone.getText().toString(), "", "1"));
    }

    @Override
    public Call<GetResualt> sendMessage(SendMessageViewModel sendMessage) {
        return null;
    }

    @Override
    public void onResponseSendMessage(GetResualt getResualt) {

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
        if (getResualt.getResualt().equals("100")){
            vf.setDisplayedChild(1);

        }

    }

    @Override
    public Call<List<SendMessageViewModel>> getMessage(SendMessageViewModel sendMessageViewModel) {
        return null;
    }

    @Override
    public void onResponseGetMessage(List<SendMessageViewModel> sendMessageViewModels) {

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
}