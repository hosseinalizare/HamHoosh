package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.R;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.fragments.NewsLetterFragment;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.model.network.RetrofitInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsLetterActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    Fragment newsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_letter);
        newsFragment = new NewsLetterFragment();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frm_newsLetterActivity_layout,newsFragment);
        transaction.commit();
    }

}