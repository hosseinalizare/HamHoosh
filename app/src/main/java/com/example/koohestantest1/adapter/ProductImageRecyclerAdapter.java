package com.example.koohestantest1.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.R;

import java.util.List;

public class ProductImageRecyclerAdapter extends RecyclerView.Adapter<ProductImageRecyclerAdapter.ProductImageViewHolder> {

    List<Uri> imageUriList;
    Context context;
    public ProductImageRecyclerAdapter(List<Uri> imageUriList,Context context) {
        this.imageUriList = imageUriList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_image,parent,false);
        return new ProductImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageViewHolder holder, int position) {
        Glide.with(context).load(imageUriList.get(position)).into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return imageUriList.size();
    }

    class ProductImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        public ProductImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_productImageAdapter_product);
        }
    }
}
