package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewProductActivity;
import com.example.koohestantest1.local_db.entity.Product;

import java.util.List;

public class ParticularProductRecyclerViewAdapter extends RecyclerView.Adapter<ParticularProductRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
//    private ArrayList<Bitmap> mImage = new ArrayList<>();
//    ArrayList<String> PID = new ArrayList<>();

//    BaseCodeClass baseCodeClass = new BaseCodeClass();

//    public ParticularProductRecyclerViewAdapter(Context mContext, ArrayList<Bitmap> mImage, ArrayList<String> pid) {
//        this.mContext = mContext;
//        this.mImage = mImage;
//        this.PID = pid;
//    }

    List<Product> producs;
    BaseCodeClass baseCodeClass = new BaseCodeClass();

    public ParticularProductRecyclerViewAdapter(Context context, List<Product> ProductClass) {
        producs = ProductClass;
        this.mContext = context;
    }

    public void newDownloadImage(String pid, ViewHolder holder) {
        String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
        Glide.with(mContext).load(url).into(holder.image);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layou_particular_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            //holder.image.setImageBitmap(producs.get(position).getImage());
            newDownloadImage(producs.get(position).ProductID, holder);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ViewProductActivity.class);
                    intent.putExtra("PID", producs.get(position).ProductID);
                    mContext.startActivity(intent);

                }
            });
        } catch (Exception e) {
            baseCodeClass.logMessage("particularAdapter > onBindViewHolder", mContext);
        }
    }

    @Override
    public int getItemCount() {
        return producs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.particularProductImage);
        }
    }
}
