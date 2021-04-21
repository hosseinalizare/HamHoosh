package com.example.koohestantest1.adapter;

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

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.R;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.FilterRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class BrandFilterRecyclerViewAdapter extends RecyclerView.Adapter<BrandFilterRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mName = new ArrayList<>();
    private List<Integer> mImage = new ArrayList<>();

    BaseCodeClass baseCodeClass = new BaseCodeClass();
    private String selectedFilter;
    private static LoadProductApi filterRecyclerViewAdapter;
    private static ViewHolder lastHolder;

    public BrandFilterRecyclerViewAdapter(Context mContext, List<String> mName,List<Integer> mImage, String s, LoadProductApi itemListener) {
        this.mContext = mContext;
        this.mName = mName;
        this.mImage=mImage;
        this.selectedFilter = s;
        this.filterRecyclerViewAdapter = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_category, parent, false);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            holder.tvName.setText(mName.get(position));
            holder.img.setImageResource(mImage.get(position));
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        RelativeLayout layout;
        CardView cardView;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            try {

                tvName = itemView.findViewById(R.id.categoryName);
                layout = itemView.findViewById(R.id.filterLayout);
                cardView = itemView.findViewById(R.id.CategoryCardView);
                img = itemView.findViewById(R.id.CategoryImage);

            } catch (Exception e) {
                baseCodeClass.logMessage("viewHolderFilterAdapter >> " + e.getMessage(), mContext);
            }
        }
    }

}
