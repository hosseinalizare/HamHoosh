package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
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
import com.example.koohestantest1.AddAddressActivity;
import com.example.koohestantest1.EditProfileActivity;
import com.example.koohestantest1.ProfileSettingActivity;
import com.example.koohestantest1.R;

import java.util.ArrayList;

public class ProfileSettingRecyclerViewAdapter extends RecyclerView.Adapter<ProfileSettingRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ProfileRecyclerViewAdap";

    //vars
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<Integer> mImageName = new ArrayList<>();
    private Context mContext;
    int mLayout;
//    private OnListListener mOnListListener;

    public ProfileSettingRecyclerViewAdapter(Context mContext, ArrayList<String> mName, ArrayList<Integer> mImageName, int mLayout/*, OnListListener onListListener*/) {
        this.mName = mName;
        this.mImageName = mImageName;
        this.mContext = mContext;
        this.mLayout = mLayout;
//        this.mOnListListener = onListListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolder(view/*, mOnListListener*/);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .load(mImageName.get(position))
                .into(holder.image);
        holder.name.setText(mName.get(position));
        holder.cardView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on image: " + mName.get(position));

            if (position == 0/*mName.get(position) == "اطلاعات حساب کاربری"*/) {
                Intent i = new Intent(mContext, EditProfileActivity.class);
                i.putExtra("mode", BaseCodeClass.CompanyEnum.Umenu_Setting.toString());
                mContext.startActivity(i);
            }
            if (position == 1/*mName.get(position) == "اطلاعات تماس"*/) {
                Intent i = new Intent(mContext, EditProfileActivity.class);

                i.putExtra("mode", BaseCodeClass.CompanyEnum.Umenu_CallInfo.toString());
                mContext.startActivity(i);
            }
            if (position == 2/*mName.get(position) == "اطلاعات حساب کاربری"*/) {
                Intent i = new Intent(mContext, EditProfileActivity.class);

                i.putExtra("mode", BaseCodeClass.CompanyEnum.Umenu_Money.toString());
                mContext.startActivity(i);
            }


            if (position == 3) {

                Intent intent = new Intent(mContext, ProfileSettingActivity.class);
                mContext.startActivity(intent);
            }

            if (position == 4) {
                Intent j = new Intent(mContext, AddAddressActivity.class);
                mContext.startActivity(j);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageName.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {
        ImageView image;
        TextView name;
        CardView cardView;
//        OnListListener onListListener;

        public ViewHolder(@NonNull View itemView/*, OnListListener onListListener*/) {
            super(itemView);
            image = itemView.findViewById(R.id.ProductImage);
            name = itemView.findViewById(R.id.cardName);
            cardView = itemView.findViewById(R.id.cardView);
//            this.onListListener = onListListener;

//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            onListListener.OnListClick(getAdapterPosition());
//        }
    }

//    public interface OnListListener{
//        void OnListClick(int position);
//    }
}
