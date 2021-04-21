package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;

import java.util.ArrayList;

public class ProductPropertiesRecyclerViewAdapter extends RecyclerView.Adapter<ProductPropertiesRecyclerViewAdapter.ViewHolder>{

    Context mContext;
    private ArrayList<String> mName;
    private ArrayList<String> mValues;
    private  int enable;

    public ProductPropertiesRecyclerViewAdapter(Context mContext, ArrayList<String> mName, ArrayList<String> mValues, int Enable) {
        this.mContext = mContext;
        this.mName = mName;
        this.mValues = mValues;
        this.enable = Enable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_profile_list, parent, false);

        return new ProductPropertiesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            holder.textView.setText(mName.get(position));
            holder.editText.setText(mValues.get(position));
            if (enable == 1){
                holder.editText.setEnabled(false);
            }
        }catch (Exception e){
            BaseCodeClass.logMessage("ProductPropertiesAdapater 100 >> " + e.getMessage(), mContext);
        }
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        EditText editText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.itemName);
            editText = itemView.findViewById(R.id.itemValue);
        }
    }
}
