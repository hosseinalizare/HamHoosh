package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.posters.RemoteVideo;
import com.asura.library.views.PosterSlider;
import com.bumptech.glide.Glide;
import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewProductActivity;
import com.example.koohestantest1.fragments.bottomsheet.CommentsBottomSheet;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.local_db.entity.NewsLetterImage;
import com.example.koohestantest1.model.FilledNewsImage;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productDataList;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;

public class NewsLetterAdapter extends RecyclerView.Adapter<NewsLetterAdapter.NewsLetterViewHolder> {
    List<NewsLetter> newsLetterList;

    private static List<Poster> posterList;
    Context context;
    FragmentManager manager;
    DBViewModel dbViewModel;
    boolean likeIt = false;
    String newsId = "";
    List<String> imageList;
    public NewsLetterAdapter(List<NewsLetter> newsLetterList,Context context,
                             FragmentManager manager,DBViewModel dbViewModel) {
        this.newsLetterList = newsLetterList;

        this.context = context;
        this.manager = manager;
        this.dbViewModel = dbViewModel;
        posterList = new ArrayList<>();
        imageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public NewsLetterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_letter_item,parent,false);
        return new NewsLetterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsLetterViewHolder holder, int position) {
        RemoteImage remoteImage;
        RemoteVideo remoteVideo;
        newsId = newsLetterList.get(position).NewsID;
        try {
            JSONArray jsonArray = new JSONArray(newsLetterList.get(position).Jsonsrc);
            posterList.clear();
            for(int i = 0;i < jsonArray.length(); i++){
                if(jsonArray.getString(i).contains(".jpg") || jsonArray.getString(i).contains(".jpeg") || jsonArray.getString(i).contains(".png")) {
                    remoteImage = new RemoteImage(jsonArray.getString(i));
                    remoteImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    posterList.add(remoteImage);
                }
                else {
                    remoteVideo = new RemoteVideo(Uri.parse(jsonArray.getString(i)));
                    posterList.add(remoteVideo);
                }
//
            }

            holder.slider.setPosters(posterList);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.txtNewsTitle.setText(newsLetterList.get(position).Title);
        holder.txtNewsDesc.setText(newsLetterList.get(position).Description);
        if(!newsLetterList.get(position).ActiveComment)
            holder.imgLike.setEnabled(false);

        if(newsLetterList.get(position).Likeit)
            holder.imgLike.setImageResource(R.drawable.ic_liked);
        else
            holder.imgLike.setImageResource(R.drawable.ic_like);

        if(!newsLetterList.get(position).ActiveComment)
            holder.imgComment.setEnabled(false);

        if(!newsLetterList.get(position).ActiveSave)
            holder.imgBookmark.setEnabled(false);


        holder.imgBookmark.setOnClickListener(v -> {
            Bitmap bitmap = ((BitmapDrawable)holder.imgBookmark.getDrawable()).getBitmap();
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.ic_bookmarked);
            Bitmap bitmap1 = ((BitmapDrawable)myDrawable).getBitmap();
            if(bitmap.sameAs(bitmap1)){
                holder.imgBookmark.setImageResource(R.drawable.ic_bookmark);
            }else{
                holder.imgBookmark.setImageResource(R.drawable.ic_bookmarked);
            }
        });

        holder.imgComment.setOnClickListener(v -> {
            CommentsBottomSheet commentsBottomSheet = new CommentsBottomSheet(newsLetterList.get(position).NewsID,
                    BaseCodeClass.userID,BaseCodeClass.token,BaseCodeClass.CompanyID);
            commentsBottomSheet.show(manager,"TAG_COMMENT_SHEET");
        });

        holder.imgLike.setOnClickListener(v -> {
            NewsLetter newsLetter = new NewsLetter();
            newsLetter = newsLetterList.get(position);
            Bitmap bitmapImgLike = ((BitmapDrawable)holder.imgLike.getDrawable()).getBitmap();
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_liked);
            Bitmap bitmapLike = ((BitmapDrawable)drawable).getBitmap();
            if(bitmapImgLike.sameAs(bitmapLike)){
                holder.imgLike.setImageResource(R.drawable.ic_like);
                likeIt = false;

                newsLetter.Likeit = likeIt;
                likePost(newsLetterList.get(position).NewsID,BaseCodeClass.userID,false,newsLetter);
            }else{
                holder.imgLike.setImageResource(R.drawable.ic_liked);
                likeIt = true;
                newsLetter.Likeit = likeIt;
                likePost(newsLetterList.get(position).NewsID,BaseCodeClass.userID,true,newsLetter);

            }
        });
        /*if(newsLetterImageList.get(position).getNewsId().equals(newsLetterList.get(position).NewsID)
        && newsLetterImageList.get(position).getSrc() != null){
            for(int i = 0;newsLetterImageList.get(position).getSrc().size() > i;i++){
                posterList.add(new RemoteImage(newsLetterImageList.get(position).getSrc().get(i)));
            }
            holder.slider.setPosters(posterList);
        }*/


    }

    @Override
    public int getItemCount() {
        return newsLetterList.size();
    }

    class NewsLetterViewHolder extends RecyclerView.ViewHolder{
        ImageView imgBookmark,imgLike,imgComment,imgViewCount;
        PosterSlider slider;
        TextView txtNewsTitle,txtNewsDesc,txtLikeCount;
        public NewsLetterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBookmark = itemView.findViewById(R.id.newsBookmark);
            imgLike = itemView.findViewById(R.id.newsLike);
            imgComment = itemView.findViewById(R.id.img_comment_news);
            imgViewCount = itemView.findViewById(R.id.img_newsComment_viewCount);
            slider = itemView.findViewById(R.id.ps_newsItem);
            txtNewsTitle = itemView.findViewById(R.id.txt_newsTitle);
            txtNewsDesc = itemView.findViewById(R.id.newsDescription);
            txtLikeCount = itemView.findViewById(R.id.txt_newsLike_count);
        }
    }

    private void likePost(String pid, String uid, boolean viewCount,NewsLetter newsLetter) {
        LoadProductApi loadProductApi;
        Retrofit retrofit = RetrofitInstance.getRetrofit();
        loadProductApi = retrofit.create(LoadProductApi.class);
        try {
            Log.d(TAG, "likePost: " + viewCount);

            PostLikeViewModel viewModel = new PostLikeViewModel(uid, pid, String.valueOf(viewCount));
            Call<GetResualt> call = loadProductApi.likeProduct(viewModel);
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
//                    toastMessage("like", 5);
                    Log.d(TAG, "onResponse: " + response.body().toString());

                    if (response.body().getResualt().equals("100")) {
                        dbViewModel.updateNewsLetter(newsLetter);
                    }
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {

                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }
    }

    private void setImageInFlipper(String imgUrl,NewsLetterViewHolder holder){
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(imgUrl).into(imageView);
        holder.slider.addView(imageView);
    }
}
