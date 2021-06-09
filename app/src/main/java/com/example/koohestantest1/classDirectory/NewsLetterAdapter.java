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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.posters.RemoteVideo;
import com.asura.library.views.PosterSlider;
import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.fragments.bottomsheet.CommentsBottomSheet;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.model.DeleteNewsLetter;
import com.example.koohestantest1.model.network.RetrofitInstance;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class NewsLetterAdapter extends RecyclerView.Adapter<NewsLetterAdapter.NewsLetterViewHolder> {
    List<NewsLetter> newsLetterList;

    private static List<Poster> posterList;
    //private List<String> posterList;
    Context context;
    FragmentManager manager;
    DBViewModel dbViewModel;
    boolean likeIt = false;
    String newsId = "";
    List<String> imageList;

    public NewsLetterAdapter(List<NewsLetter> newsLetterList, Context context,
                             FragmentManager manager, DBViewModel dbViewModel) {


        this.newsLetterList = newsLetterList;

        this.context = context;
        this.manager = manager;
        this.dbViewModel = dbViewModel;
        posterList = new ArrayList<>();
        //posterList = new ArrayList<>();
        imageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public NewsLetterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_letter_item, parent, false);
        return new NewsLetterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsLetterViewHolder holder, int position) {
        RemoteImage remoteImage;
        RemoteVideo remoteVideo;


        newsId = newsLetterList.get(position).NewsID;
        String likeCount = newsLetterList.get(position).LikeCount + "";
        String viewCount = newsLetterList.get(position).ViewedCount + "";
        holder.txtLikeCount.setText(likeCount);
        holder.txtViewCount.setText(viewCount);

        try {
            JSONArray jsonArray = new JSONArray(newsLetterList.get(position).Jsonsrc);
//            posterList.clear();
            posterList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getString(i).contains(".jpg") || jsonArray.getString(i).contains(".jpeg") || jsonArray.getString(i).contains(".png")) {
                    remoteImage = new RemoteImage(jsonArray.getString(i));
                    remoteImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    posterList.add(remoteImage);
                } else {
                    remoteVideo = new RemoteVideo(Uri.parse(jsonArray.getString(i)));

                    Log.d("Video", Uri.parse(jsonArray.getString(i)) + "");
                    posterList.add(remoteVideo);
                }
                Log.d("Video", Uri.parse(jsonArray.getString(i)) + "");
                //posterList.add(jsonArray.getString(i));

            }

            holder.slider.setPosters(posterList);
            //NewsLetterViewPagerAdapter adapter = new NewsLetterViewPagerAdapter(context, posterList);
            //adapter.notifyDataSetChanged();
            //holder.slider.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.txtNewsTitle.setText(newsLetterList.get(position).Title);
        holder.txtNewsDesc.setText(newsLetterList.get(position).Description);
        if (!newsLetterList.get(position).ActiveComment)
            holder.imgLike.setEnabled(false);

        if (newsLetterList.get(position).Likeit)
            holder.imgLike.setImageResource(R.drawable.ic_liked);
        else
            holder.imgLike.setImageResource(R.drawable.ic_like);

        if (!newsLetterList.get(position).ActiveComment)
            holder.imgComment.setEnabled(false);

        if (!newsLetterList.get(position).ActiveSave)
            holder.imgBookmark.setEnabled(false);

        if (newsLetterList.get(position).Saveit)
            holder.imgBookmark.setImageResource(R.drawable.ic_bookmarked);
        else
            holder.imgBookmark.setImageResource(R.drawable.ic_bookmark);


        holder.imgBookmark.setOnClickListener(v -> {
            NewsLetter newsLetter = newsLetterList.get(position);
            Bitmap bitmap = ((BitmapDrawable) holder.imgBookmark.getDrawable()).getBitmap();
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.ic_bookmarked);
            Bitmap bitmap1 = ((BitmapDrawable) myDrawable).getBitmap();
            if (bitmap.sameAs(bitmap1)) {
                newsLetter.Saveit = false;
                holder.imgBookmark.setImageResource(R.drawable.ic_bookmark);
            } else {
                newsLetter.Saveit = true;
                holder.imgBookmark.setImageResource(R.drawable.ic_bookmarked);
            }

            dbViewModel.updateNewsLetter(newsLetter);
        });

        holder.imgDelete.setOnClickListener(v -> deleteNewsLetter(newsLetterList.get(position)));

        holder.imgComment.setOnClickListener(v -> {
            CommentsBottomSheet commentsBottomSheet = new CommentsBottomSheet(newsLetterList.get(position).NewsID,
                    BaseCodeClass.userID, BaseCodeClass.token, BaseCodeClass.CompanyID);
            commentsBottomSheet.show(manager, "TAG_COMMENT_SHEET");
        });

        holder.imgLike.setOnClickListener(v -> {
            NewsLetter newsLetter = newsLetterList.get(position);
            Bitmap bitmapImgLike = ((BitmapDrawable) holder.imgLike.getDrawable()).getBitmap();
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_liked);
            Bitmap bitmapLike = ((BitmapDrawable) drawable).getBitmap();
            if (bitmapImgLike.sameAs(bitmapLike)) {
                holder.imgLike.setImageResource(R.drawable.ic_like);
                likeIt = false;
                if (newsLetter.LikeCount > 0)
                    newsLetter.LikeCount--;
                newsLetter.Likeit = likeIt;
                likePost(newsLetterList.get(position).NewsID, BaseCodeClass.userID, false, newsLetter);
            } else {
                holder.imgLike.setImageResource(R.drawable.ic_liked);
                likeIt = true;
                newsLetter.LikeCount++;
                newsLetter.Likeit = likeIt;
                likePost(newsLetterList.get(position).NewsID, BaseCodeClass.userID, true, newsLetter);

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


    class NewsLetterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBookmark, imgLike, imgComment, imgViewCount, imgDelete;
        PosterSlider slider;
        TextView txtNewsTitle, txtNewsDesc, txtLikeCount,txtViewCount;

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
            imgDelete = itemView.findViewById(R.id.img_newsLetter_delete);
            txtViewCount = itemView.findViewById(R.id.txt_newsComment_viewCount);
        }
    }

    private void likePost(String pid, String uid, boolean viewCount, NewsLetter newsLetter) {
        LoadProductApi loadProductApi;
        Retrofit retrofit = RetrofitInstance.getRetrofit();
        loadProductApi = retrofit.create(LoadProductApi.class);
        try {
            Log.d(TAG, "likePost: " + viewCount);
            /**
             * Ask question about like count in server site
             */
            PostLikeViewModel viewModel = new PostLikeViewModel(uid, pid, String.valueOf(viewCount), newsLetter.LikeCount);
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
            Log.d("Error", e.getMessage());
        }
    }

    private void deleteNewsLetter(NewsLetter newsLetter) {
        JsonApi jsonApi;
        Retrofit retrofit = RetrofitInstance.getRetrofit();
        jsonApi = retrofit.create(JsonApi.class);
        dbViewModel.deleteOneNewsLetter(newsLetter);
        DeleteNewsLetter deleteNewsLetter = new DeleteNewsLetter();
        deleteNewsLetter.setCompanyId(newsLetter.SupplierID);
        deleteNewsLetter.setNewsId(newsLetter.NewsID);
        deleteNewsLetter.setToken(BaseCodeClass.token);
        deleteNewsLetter.setUserId(newsLetter.CreatorUserID);
        Call<GetResualt> call = jsonApi.deleteOneNewsLetter(deleteNewsLetter);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                if (response.body().getResualt().equals("100")) {
                    Toast.makeText(context, "خبر با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}
