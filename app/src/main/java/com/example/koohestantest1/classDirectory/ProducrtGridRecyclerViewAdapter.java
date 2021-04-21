package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;

public class ProducrtGridRecyclerViewAdapter extends RecyclerView.Adapter<ProducrtGridRecyclerViewAdapter.ViewHolder> {

    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext;
    Bitmap[] pImage;
//    private ArrayList<Post>

    public ProducrtGridRecyclerViewAdapter(Context mContext, Bitmap[] pImage) {
        this.mContext = mContext;
        this.pImage = pImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_gridview, parent, false);
        return new ProducrtGridRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.gridImages.setImageBitmap(pImage[position]);
    }

    @Override
    public int getItemCount() {
        return pImage.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView gridImages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gridImages = (ImageView) itemView.findViewById(R.id.ImageGridProduct);

//            int gridWidth = mContext.getResources().getDisplayMetrics().widthPixels;
//            int imageWidth = gridWidth/NUM_GRID_COLUMNS;
//            gridImages.setMaxHeight(imageWidth);
//            gridImages.setMaxWidth(imageWidth);
        }
    }
}
