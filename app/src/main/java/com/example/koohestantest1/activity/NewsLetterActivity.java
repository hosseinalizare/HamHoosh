package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.R;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.model.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsLetterActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private JsonApi jsonApi;
    private DBViewModel dbViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_letter);
        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        dbViewModel.getAllNews().observe(this, new Observer<List<NewsLetter>>() {
            @Override
            public void onChanged(List<NewsLetter> newsLetters) {
                if(newsLetters == null || newsLetters.isEmpty()){
                    getAllNews(BaseCodeClass.CompanyID,1577865695);
                }else{
                    Toast.makeText(getApplicationContext(),"Has data",Toast.LENGTH_SHORT).show();
                }
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
                if (response.body() == null) {
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
    }
}