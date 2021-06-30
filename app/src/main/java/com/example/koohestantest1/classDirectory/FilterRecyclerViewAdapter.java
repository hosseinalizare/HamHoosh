package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.R;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mName = new ArrayList<>();
    private ArrayList<Integer> mImage = new ArrayList<>();
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    private String selectedFilter;
    private static LoadProductApi filterRecyclerViewAdapter;
    private static ViewHolder lastHolder;

    public RelativeLayout defaultLayout;

    public FilterRecyclerViewAdapter(Context mContext, List<String> mName, ArrayList<Integer> mImage, String s, LoadProductApi itemListener) {
        this.mContext = mContext;
        this.mName = mName;
        this.mImage = mImage;
        this.selectedFilter = s;
        this.filterRecyclerViewAdapter = itemListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        defaultLayout = getRelativeLayout(mName,holder.layout);

        try {
            holder.tvName.setText(mName.get(position));

            if (mImage.get(position)==R.drawable.unknown){
                String url = baseCodeClass.BASE_URL + "Products/DownloadCatIcon?cat=" + mName.get(position)+"&fileNumber=1";
                Glide.with(mContext).load(url).into(holder.image);

            }else {
                holder.image.setImageResource(mImage.get(position));

            }

//            baseCodeClass.logMessage("binding", mContext);
            if (mName.get(position).equals("همه") || lastHolder == null) {
                lastHolder = holder;
            }
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundGray));
            if (mName.get(position).equals(selectedFilter)) {
                holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.LighterBlue));

            } else {
                holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundGray));
                if (lastHolder != null) {
                    lastHolder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundGray));
                }
            }

            holder.cardView.setOnClickListener(v -> {
                try {

                    if (lastHolder != holder) {
                        if (lastHolder != null) {
                            lastHolder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundGray));
                        }
                        lastHolder = holder;
                    }
                    holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.LighterBlue));
                    filterRecyclerViewAdapter.recyclerViewListClicked(v, mName.get(position), false);
                } catch (Exception e) {
                    baseCodeClass.logMessage(e.getMessage(), mContext);
                }
            });
        } catch (Exception e) {
            baseCodeClass.logMessage("filterAdapter >> " + e.getMessage(), mContext);
        }
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public interface SetDefault{
        RelativeLayout setDefault();
    }

    public RelativeLayout getRelativeLayout(List<String> names,RelativeLayout relativeLayout){
        RelativeLayout rel = new RelativeLayout(mContext);
        for(String name:names){
            if(name.equals("همه")){
                rel =  relativeLayout;
            }
        }
        return rel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView image;
        RelativeLayout layout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            try {

                tvName = itemView.findViewById(R.id.categoryName);
                image = itemView.findViewById(R.id.CategoryImage);
                layout = itemView.findViewById(R.id.filterLayout);
                cardView = itemView.findViewById(R.id.CategoryCardView);
            } catch (Exception e) {
                baseCodeClass.logMessage("viewHolderFilterAdapter >> " + e.getMessage(), mContext);
            }
        }
    }
}
