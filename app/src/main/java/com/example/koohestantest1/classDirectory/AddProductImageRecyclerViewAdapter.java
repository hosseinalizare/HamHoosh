package com.example.koohestantest1.classDirectory;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;

import java.util.ArrayList;
import java.util.List;

public class AddProductImageRecyclerViewAdapter extends RecyclerView.Adapter<AddProductImageRecyclerViewAdapter.ViewHolder> {

    private List<Uri> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }


    public void setData(List<Uri> images) {
        data = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_product_image, parent, false);


        return new AddProductImageRecyclerViewAdapter.ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_product_photo);
            cardView = itemView.findViewById(R.id.AddProductImageCardView);
        }

        void bindData(Uri uri) {
            imageView.setImageURI(uri);
        }
    }
}
