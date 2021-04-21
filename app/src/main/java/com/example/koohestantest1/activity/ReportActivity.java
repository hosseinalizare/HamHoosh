package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.InfoDirectory.MessageManagerClass;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.ViewModels.SendReportViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.HardwareIdsMobile;
import com.example.koohestantest1.databinding.ActivityReportBinding;
import com.example.koohestantest1.model.Report;
import com.example.koohestantest1.viewModel.ReportViewModel;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

import java.util.List;

import retrofit2.Call;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class ReportActivity extends AppCompatActivity implements MessageApi {

    private final String TAG = ReportActivity.class.getSimpleName();
    private ActivityReportBinding binding;
    private ReportViewModel reportViewModel;
    MessageManagerClass messageManagerClass;
    BaseCodeClass baseCodeClass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        messageManagerClass = new MessageManagerClass(ReportActivity.this,this);
        baseCodeClass = new BaseCodeClass();

        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        binding.ivReportBack.setOnClickListener(v -> {
            finish();
        });
        binding.ivSendReport.setOnClickListener(v -> {
            String title = binding.etReportTitle.getText().toString();
            String description = binding.etReportDescription.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, "فیلد عنوان نمیتواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.isEmpty()) {

                Toast.makeText(this, "فیلد توضیحات نمیتواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "در حال ارسال", Toast.LENGTH_SHORT).show();
            try {
                messageManagerClass.sendReport(new SendReportViewModel(baseCodeClass.getUserID(), baseCodeClass.getToken(), "12",
                        description, "1", baseCodeClass.getUserID(), title, "",
                        BaseCodeClass.CompanyID+"&"+getString(R.string.LastVer)+"&"+ baseCodeClass.getDeviceModel()
                        , BaseCodeClass.userProfile.getMobilePhone(), "", "1"));


            } catch (Exception e) {
            }
        });


    }

    @Override
    public Call<GetResualt> sendMessage(SendMessageViewModel sendMessage) {
        return null;
    }

    @Override
    public void onResponseSendMessage(GetResualt getResualt) {

    }

    @Override
    public Call<GetResualt> sendReport(SendReportViewModel sendReportViewModel) {
        return null;
    }

    @Override
    public void onResponseSendReport(GetResualt getResualt) {
        if (getResualt.getResualt().equals("100")){
            Toast.makeText(ReportActivity.this, "گزارش شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
            binding.etReportDescription.setText("");
            binding.etReportTitle.setText("");
        }else {
            Toast.makeText(ReportActivity.this, getResualt.getResualt().toString(), Toast.LENGTH_SHORT).show();

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
}