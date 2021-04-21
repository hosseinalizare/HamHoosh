package com.example.koohestantest1;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.koohestantest1.model.network.RetrofitInstance;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.TermsAndConditions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TermsDialog {
    private Activity activity;
    private AlertDialog dialog;
    private String termsText;
    private String termsTitle;
    private TextView tvTerms;
    private TextView tvOk;
    private TextView tvTermsTitle;
    private ProgressBar pbLoading;
    private Retrofit retrofit;
    private JsonApi jsonApi;

    public TermsDialog(Activity activity) {
        this.activity = activity;
    }

    public void startTermsDialog() {
        retrofit = RetrofitInstance.getRetrofit();
        jsonApi = retrofit.create(JsonApi.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_terms, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
        tvOk = dialog.findViewById(R.id.tv_termsDialog_ok);
        pbLoading = dialog.findViewById(R.id.pb_termsDialog_loading);
        tvTerms = dialog.findViewById(R.id.tv_termsDialog_terms);
        tvTermsTitle = dialog.findViewById(R.id.tv_termsDialog_title);

        tvTerms.setLineSpacing(2f, 2f);

        Call<TermsAndConditions> call = jsonApi.getTerms(BaseCodeClass.CompanyID);
        call.enqueue(new Callback<TermsAndConditions>() {
            @Override
            public void onResponse(Call<TermsAndConditions> call, Response<TermsAndConditions> response) {
                termsText = response.body().getData();
                termsTitle = response.body().getTitle();
                tvTerms.setText(termsText);
                tvTermsTitle.setText(termsTitle);
                pbLoading.setVisibility(View.GONE);
                tvTermsTitle.setVisibility(View.VISIBLE);
                tvTerms.setVisibility(View.VISIBLE);
                tvOk.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<TermsAndConditions> call, Throwable t) {
                termsTitle = "لطفا دوباره تلاش کنید";
                tvTermsTitle.setText(termsTitle);
                tvTermsTitle.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                Log.i("LOG", t.toString());
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        tvOk.setOnClickListener(v -> {
            cancelTermsDialog();
        });
    }

    public void cancelTermsDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
