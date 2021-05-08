package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.Main2Fragment;
import com.example.koohestantest1.R;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.model.network.RetrofitInstance;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsLetterActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private JsonApi jsonApi;
    private DBViewModel dbViewModel;
    private static int timeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_letter);

        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);

        dbViewModel.getLastUpdate().observe(this, s -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            try {
                Date date = format.parse(s);
                timeStamp = (int)date.getTime() / 1000;
                getAllNews(BaseCodeClass.CompanyID, timeStamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        dbViewModel.getAllNews().observe(this, new Observer<List<NewsLetter>>() {
            @Override
            public void onChanged(List<NewsLetter> newsLetters) {

            }
        });

    }

    private void getAllNews(String companyId, long time) {
        retrofit = RetrofitInstance.getRetrofit();
        jsonApi = retrofit.create(JsonApi.class);
        Call<List<NewsLetter>> call = jsonApi.getAllNews(companyId, time);
        call.enqueue(new Callback<List<NewsLetter>>() {
            @Override
            public void onResponse(Call<List<NewsLetter>> call, Response<List<NewsLetter>> response) {
                if (response.body() == null || response.body().size() == 0) {
                    Toast.makeText(NewsLetterActivity.this, "هیچ خبری نیست", Toast.LENGTH_SHORT).show();
                } else
                    insertNewsLetterIntoLocalDB(response.body());
            }

            @Override
            public void onFailure(Call<List<NewsLetter>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void insertNewsLetterIntoLocalDB(List<NewsLetter> newsLetters) {
        for (NewsLetter newsLetter : newsLetters) {
            dbViewModel.insertNewsLetter(newsLetter);
        }
        //dbViewModel.insertNewsLetter(newsLetters.get(0));
    }


}