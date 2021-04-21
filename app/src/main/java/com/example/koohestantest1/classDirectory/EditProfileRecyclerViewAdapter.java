package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.fragments.bottomsheet.EditBottomSheet;

import java.util.List;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProfileField;

public class EditProfileRecyclerViewAdapter extends RecyclerView.Adapter<EditProfileRecyclerViewAdapter.ViewHolder> {

    private List<EditProfileField> fields;
    private Context mContext;
    private FragmentManager fragmentManager;
    private String TAG = EditProfileRecyclerViewAdapter.class.getSimpleName();

    public EditProfileRecyclerViewAdapter(List<EditProfileField> fields, Context mContext, FragmentManager fragmentManager) {
        this.fields = fields;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_field_profile_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        try {
            holder.textView.setText(fields.get(position).Explain);
            if (fields.get(position).Value.isEmpty() || fields.get(position).Value.equals("null")) {
                holder.editText.setText("");
            } else {
                String value = fields.get(position).Value;

                //check for if this value is time, cleans the string format
                if (fields.get(position).Cenum == BaseCodeClass.CompanyEnum.U_Birthdate) {
                    StringBuilder stringBuilder = new StringBuilder()
                            .append(TimeUtils.getPersianCleanDate(value));
                    holder.editText.setText(stringBuilder.toString());

                } else holder.editText.setText(value);

            }

            if (holder.editText.getText().toString().equals("null")) {
                holder.editText.setText("");
            }

        } catch (Exception e) {
//            Toast.makeText(mContext, "onbind >> " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView, editText;
        private ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.itemName);
            editText = itemView.findViewById(R.id.itemValue);
            ivEdit = itemView.findViewById(R.id.iv_edit_profile_item);

            itemView.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (fields.get(getAdapterPosition()).EditAble) {
                selectedProfileField = fields.get(getAdapterPosition());
                Log.d(TAG, "onBindViewHolder: ");
                EditBottomSheet editBottomSheet = new EditBottomSheet();
                editBottomSheet.show(fragmentManager, "TAG_EDIT");
            }
        }
    }
}
