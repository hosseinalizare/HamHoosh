package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;

import java.util.ArrayList;
import java.util.List;

public class ProductPropertiesRecyclerViewAdapter extends RecyclerView.Adapter<ProductPropertiesRecyclerViewAdapter.ViewHolder>{

    Context mContext;
    public ArrayList<String> mName;
    public ArrayList<String> mValues;
    private  int enable;
    private List<String> propertyValue = new ArrayList<>();

    public ProductPropertiesRecyclerViewAdapter(Context mContext, ArrayList<String> mName, ArrayList<String> mValues, int Enable) {
        if(mName != null)
            this.mName = mName;
        else
            this.mName = new ArrayList<>();
        if(mValues != null)
            this.mValues = mValues;
        else
            this.mValues = new ArrayList<>();

        this.mContext = mContext;


        this.enable = Enable;
        propertyValue = new ArrayList<>();
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
            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mValues.remove(position);
                    mValues.add(position,s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });
            if (enable == 1){
                holder.editText.setEnabled(false);
            }
        }catch (Exception e){
            BaseCodeClass.logMessage("ProductPropertiesAdapater 100 >> " + e.getMessage(), mContext);
        }
    }

    public List<String> getPropertyName(){

        return mValues;
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
