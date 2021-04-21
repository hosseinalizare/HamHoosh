package com.example.koohestantest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.CompanyApi;
import com.example.koohestantest1.ApiDirectory.UserProfileApi;
import com.example.koohestantest1.ViewModels.CompanyProfileFieldViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProfileField;

public class EditFieldActivity extends AppCompatActivity {

    TextView explain, des;
    EditText value;
    BaseCodeClass baseCodeClass;
    CompanyApi companyApi;
    UserProfileApi userProfileApi;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);

        explain = (TextView) findViewById(R.id.explainText);
        des = findViewById(R.id.desText);
        value = findViewById(R.id.valueText);

        baseCodeClass = new BaseCodeClass();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseCodeClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        companyApi = retrofit.create(CompanyApi.class);
        userProfileApi = retrofit.create(UserProfileApi.class);

        explain.setText(selectedProfileField.Explain);
        value.setText(selectedProfileField.Value);

    }

    public void cancel(View view) {
        finish();
    }

    public void saveField(View view) {
        try {
            if (selectedProfileField.mode == BaseCodeClass.editMode.companyProfile) {
                editCompany();
            } else if (selectedProfileField.mode == BaseCodeClass.editMode.userProfile) {
                editUSer();
            }
        } catch (Exception e) {
            logMessage("EditField 100 >> " + e.getMessage(), this);
        }
    }

    public void editCompany() {
        try {
            Call<GetResualt> call = companyApi.updateCompanyProfileField(new CompanyProfileFieldViewModel(baseCodeClass.getUserID(),
                    baseCodeClass.getToken(),
                    baseCodeClass.getCompanyID(),
                    value.getText().toString(),
                    String.valueOf(selectedProfileField.getCenum())));
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                    if (response.body().getResualt().equals("100")) {
//                    getOnlineInformationClass.readCompany(baseCodeClass.getCompanyID());
                        Toast.makeText(EditFieldActivity.this, "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    baseCodeClass.logMessage("onFailure >> " + t.getMessage(), EditFieldActivity.this);
                }
            });
        } catch (Exception e) {
            logMessage("EditField 100 >> " + e.getMessage(), this);
        }
    }

    public void editUSer() {
        try {
            Call<GetResualt> call = userProfileApi.updateUserProfileField(new CompanyProfileFieldViewModel(baseCodeClass.getUserID(),
                    baseCodeClass.getToken(),
                    baseCodeClass.getUserID(),
                    value.getText().toString(),
                    String.valueOf(selectedProfileField.getCenum())));
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                    if (response.body().getResualt().equals("100")) {
//                    getOnlineInformationClass.readCompany(baseCodeClass.getCompanyID());
                        Toast.makeText(EditFieldActivity.this, "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    baseCodeClass.logMessage("onFailure >> " + t.getMessage(), EditFieldActivity.this);
                }
            });
        } catch (Exception e) {
            logMessage("EditField 100 >> " + e.getMessage(), this);
        }
    }
}
