package com.example.koohestantest1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;
import com.example.koohestantest1.adapter.recyclerinterface.ICompanySettingAdapter;

import java.util.List;

public class CompanySettingHeaderRecyclerAdapter extends RecyclerView.Adapter<CompanySettingHeaderRecyclerAdapter.HeaderViewHolder> {

    private List<String> titles;
    private String TAG = CompanySettingHeaderRecyclerAdapter.class.getSimpleName();
    private ICompanySettingAdapter iCompanySettingAdapter;

    public CompanySettingHeaderRecyclerAdapter(ICompanySettingAdapter iCompanySettingAdapter) {
        this.iCompanySettingAdapter = iCompanySettingAdapter;
    }

    public void setUpData(List<String> data) {
        titles = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_setting_title, parent, false);
        return new HeaderViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {
        holder.bindData(titles.get(position));
    }


    @Override
    public int getItemCount() {
        return titles != null ? titles.size() : 0;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvType;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_setting_type);
            itemView.setOnClickListener(this);
        }

        public void bindData(String value) {
            tvType.setText(value);
        }

        @Override
        public void onClick(View v) {
            iCompanySettingAdapter.onHeaderClicked(titles.get(getAdapterPosition()));
        }
    }

}
