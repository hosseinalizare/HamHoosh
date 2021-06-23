package com.example.koohestantest1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.UserProfile;

public class EditProfileDetailActivity extends AppCompatActivity {

    BaseCodeClass baseCodeClass;
    UserProfile usProfile;

    TextView Name;
    EditText edValue;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_detail);

        baseCodeClass = new BaseCodeClass();
        baseCodeClass.LoadBaseData(this);

        Name = (TextView)findViewById(R.id.textView);
        edValue = (EditText) findViewById(R.id.editText);

        try {
            String newString = null;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    baseCodeClass = null;
                } else {
                    newString = extras.getString("detailName");
//                    usProfile = (UserProfile) extras.getSerializable("detailName");
                }
            } else {
                newString = (String) savedInstanceState.getSerializable("detailName");
            }

//            Intent intent = this.getIntent();
//            Bundle bundle = intent.getExtras();

//            usProfile = (UserProfile) bundle.getSerializable("detailName");

            Name.setText(newString);
            edValue.setText(baseCodeClass.userProfile.getMobilePhone());
        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }
    }

    public void btnClick(View view) {



    }


    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
