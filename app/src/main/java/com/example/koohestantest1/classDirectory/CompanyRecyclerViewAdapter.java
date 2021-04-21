package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.R;

import java.util.ArrayList;

public class CompanyRecyclerViewAdapter extends RecyclerView.Adapter<CompanyRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<Integer> mImageName = new ArrayList<>();

    public CompanyRecyclerViewAdapter(Context mContext, ArrayList<String> mName, ArrayList<Integer> mImageName) {
        this.mContext = mContext;
        this.mName = mName;
        this.mImageName = mImageName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mImageName.get(position))
                .into(holder.image);
        holder.name.setText(mName.get(position));
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ProductImage);
            name = itemView.findViewById(R.id.cardName);
        }
    }
}
