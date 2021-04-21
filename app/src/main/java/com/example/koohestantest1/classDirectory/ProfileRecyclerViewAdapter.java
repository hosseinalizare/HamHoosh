package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.R;

import java.util.ArrayList;

public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ProfileRecyclerViewAdap";

    //vars
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<Integer> mImageName = new ArrayList<>();
    private Context mContext;
    int mLayout;
    private OnListListener mOnListListener;

    public ProfileRecyclerViewAdapter(Context mContext, ArrayList<String> mName, ArrayList<Integer> mImageName, int mLayout, OnListListener onListListener) {
        this.mName = mName;
        this.mImageName = mImageName;
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mOnListListener = onListListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolder(view, mOnListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .load(mImageName.get(position))
                .into(holder.image);
        holder.name.setText(mName.get(position));
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked on image: " + mName.get(position));
//                if (mName.get(position) == "اطلاعات حساب کاربری"){
//                    Intent i = new Intent(mContext, EditProfileActivity.class);
//                    mContext.startActivity(i);
//                }
//                else {
//                    Toast.makeText(mContext, mName.get(position), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mImageName.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        CardView cardView;
        OnListListener onListListener;

        public ViewHolder(@NonNull View itemView, OnListListener onListListener) {
            super(itemView);
            image = itemView.findViewById(R.id.ProductImage);
            name = itemView.findViewById(R.id.cardName);
            cardView = itemView.findViewById(R.id.cardView);
            this.onListListener = onListListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListListener.OnListClick(getAdapterPosition());
        }
    }

    public interface OnListListener{
        void OnListClick(int position);
    }
}
