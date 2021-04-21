package com.example.koohestantest1.classDirectory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;

import java.util.ArrayList;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.filterValue;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> mCategory = new ArrayList<>();
    private static LoadProductApi itemListener;
    boolean enableFilter = true;

    private Context mContext;
    BaseCodeClass baseCodeClass = new BaseCodeClass();

    public CategoryRecyclerViewAdapter(Context mContext, ArrayList<String> mCategory, LoadProductApi itemListener) {
        this.mContext = mContext;
        this.mCategory = mCategory;
        this.itemListener = itemListener;
        enableFilter = true;
    }

    public CategoryRecyclerViewAdapter(Context mContext, ArrayList<String> mCategory) {
        this.mContext = mContext;
        this.mCategory = mCategory;
        enableFilter = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);

        return new ViewHolder(view);
    }

    ViewHolder lastHolder;
    int row = -1;

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (enableFilter && position == 0){
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.LighterBlue));
            lastHolder = holder;
        }

        holder.textView.setText(mCategory.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                row = position;
                filterValue = mCategory.get(position);
                if (enableFilter){
                    itemListener.recyclerViewListClicked(v, mCategory.get(position), false);
//                    if (lastHolder != null){
//                        lastHolder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//                    }
//                    lastHolder = holder;
//                    holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.LighterBlue));
                }
                notifyDataSetChanged();
            }
        });

        if(row != -1){
            if (row == position){
                holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.LighterBlue));
            }
            else {
                holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView cardView;
        RelativeLayout layout;
        @SuppressLint("ResourceAsColor")
        public ViewHolder(@Nullable View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.productName);
            cardView = itemView.findViewById(R.id.cardViewCategory);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    baseCodeClass.logMessage(mCategory.get(getAdapterPosition()), mContext);
//                    itemListener.recyclerViewListClicked(v, mCategory.get(getAdapterPosition()));
//                }
//            });
        }
    }


}
