package com.example.koohestantest1.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.R;
import com.example.koohestantest1.activity.NewsLetterActivity;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.NewsLetterAdapter;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.local_db.entity.NewsLetterImage;
import com.example.koohestantest1.model.FilledNewsImage;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class NewsLetterFragment extends Fragment {
    private DBViewModel dbViewModel;
    private static long timeStamp;
    private RecyclerView rvNews;
    private NewsLetterAdapter adapter;
    private BaseCodeClass baseCodeClass;
    List<Uri> imageUriList;
    List<String> partNames;
    public static boolean isGet = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_letter, container, false);
        imageUriList = new ArrayList<>();
        partNames = new ArrayList<>();
        FloatingActionButton fbtnAdd = view.findViewById(R.id.fbtn_newsLetterFragent_addNews);
        rvNews = view.findViewById(R.id.rv_newsLetterFragment_newsList);
        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        baseCodeClass = new BaseCodeClass();
        if(baseCodeClass.getEmployeeID(baseCodeClass.getUserID()).equals("false")){
            fbtnAdd.setVisibility(View.GONE);
        }else{
            fbtnAdd.setVisibility(View.VISIBLE);
        }
        //*************************Fetching received data from Gallery*****************************/

        ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {

                                ArrayList<MediaFile> files = result.getData().getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                                for (MediaFile mediaFile : files) {
                                    String name = mediaFile.getName();
                                    Uri uri1 = mediaFile.getUri();
                                    imageUriList.add(uri1);
                                    partNames.add(name);
                                }

                        }

                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("uriList", (ArrayList<? extends Parcelable>) imageUriList);
                        Fragment addNewsFragment = new NewsLetterAddFragment();
                        addNewsFragment.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frm_newsLetterActivity_layout,addNewsFragment);
                        transaction.commit();

                    }
                }
        );

        //*************************Intent to Gallery For Choose Images and Videos******************/
        ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        Intent intent = new Intent(getContext(), FilePickerActivity.class);
                        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                                .setCheckPermission(true)
                                .setSkipZeroSizeFiles(true)
                                .setShowImages(true)
                                .setSingleChoiceMode(false)
                                .setMaxSelection(5)
                                .setShowAudios(false)
                                .setShowVideos(false)
                                .setShowFiles(false)
                                .build());

                        mLauncher.launch(intent);
                    } else {
                        Toast.makeText(getContext(), "FAIL", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        //****************************************************************************************/

        //********************************Get the last news letter from local DB******************/

        //*****************************************************************************************/

        //****************************Get all news from Local DB***********************************/
        dbViewModel.getAllNews().observe(getViewLifecycleOwner(), new Observer<List<NewsLetter>>() {
            @Override
            public void onChanged(List<NewsLetter> newsLetters) {
                if (!isGet) {
                    if (newsLetters.isEmpty() || newsLetters.size() == 0)
                        getFirstNews(BaseCodeClass.CompanyID);
                    else {
                        if(adapter != null){
                               adapter.setData(newsLetters);
                        }else {
                            adapter = new NewsLetterAdapter(newsLetters, getContext(), getActivity().getSupportFragmentManager(), dbViewModel);
                            // adapter.notifyDataSetChanged();
                            rvNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            rvNews.setAdapter(adapter);
                            isGet = true;
                        }

                        dbViewModel.getLastUpdate().observe(getViewLifecycleOwner(), new Observer<Long>() {
                            @Override
                            public void onChanged(Long s) {

                                getAllNews(BaseCodeClass.CompanyID,s);
                            }
                        });

                    }
                }
            }
        });
        //*****************************************************************************************/

        fbtnAdd.setOnClickListener(v -> {

            mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        });

        // Inflate the layout for this fragment
        return view;
    }

    //************Get recent news letter from Server Base on the last news letter in Local DB******/
    private void getAllNews(String companyId, long time) {
        Retrofit retrofit = RetrofitInstance.getRetrofit();
        JsonApi jsonApi = retrofit.create(JsonApi.class);
        Call<List<NewsLetter>> call = jsonApi.getAllNews(companyId, time,BaseCodeClass.userID);
        call.enqueue(new Callback<List<NewsLetter>>() {
            @Override
            public void onResponse(Call<List<NewsLetter>> call, Response<List<NewsLetter>> response) {
                if (response.body() == null || response.body().size() == 0) {
                    Log.d("Error","Success");
                } else {
                    isGet = false;
                    insertNewsLetterIntoLocalDB(response.body());


                }
            }

            @Override
            public void onFailure(Call<List<NewsLetter>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void getFirstNews(String companyId){
        Retrofit retrofit = RetrofitInstance.getRetrofit();
        JsonApi jsonApi = retrofit.create(JsonApi.class);
        Call<List<NewsLetter>> call = jsonApi.getFirstNews(companyId, 1,50,BaseCodeClass.userID);
        call.enqueue(new Callback<List<NewsLetter>>() {
            @Override
            public void onResponse(Call<List<NewsLetter>> call, Response<List<NewsLetter>> response) {
                if (response.body() == null || response.body().size() == 0) {
                    Toast.makeText(getContext(), "?????? ???????? ????????", Toast.LENGTH_SHORT).show();
                } else {
                    insertNewsLetterIntoLocalDB(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<NewsLetter>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
    //*********************************************************************************************/

    private void insertNewsLetterIntoLocalDB(List<NewsLetter> newsLetters) {
        for (NewsLetter newsLetter : newsLetters) {
            dbViewModel.insertNewsLetter(newsLetter);
            /*dbViewModel.insertNewsImage(newsLetter);*/
        }

    }
}