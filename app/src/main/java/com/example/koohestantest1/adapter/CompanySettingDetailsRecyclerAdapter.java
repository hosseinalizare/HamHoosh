package com.example.koohestantest1.adapter;

import android.animation.TypeConverter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;
import com.example.koohestantest1.adapter.recyclerinterface.ICompanySettingAdapter;
import com.example.koohestantest1.adapter.recyclerinterface.ISettingEditAdapter;
import com.example.koohestantest1.constants.SettingValueTypes;
import com.example.koohestantest1.model.CompanySetting;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CompanySettingDetailsRecyclerAdapter extends RecyclerView.Adapter<CompanySettingDetailsRecyclerAdapter.ContentViewHolder> {

    private List<CompanySetting> settingList;
    private String TAG = CompanySettingDetailsRecyclerAdapter.class.getSimpleName();
    private ISettingEditAdapter iSettingEditAdapter;
    private Context context;

    public CompanySettingDetailsRecyclerAdapter(ISettingEditAdapter iSettingEditAdapter, Context context) {
        this.iSettingEditAdapter = iSettingEditAdapter;
        this.context = context;
    }

    public void setUpData(List<CompanySetting> data) {
        settingList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_company_setting, parent, false);
        return new ContentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        holder.bindData(settingList.get(position));
    }


    @Override
    public int getItemCount() {
        return settingList != null ? settingList.size() : 0;
    }

    class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback {
        private TextView tvTitle, tvContent, tvExplain;
        private ImageView ivEdit;
        private ConstraintLayout constraintLayout;

        private MapView mapView;
        private GoogleMap map;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_setting_title);
            tvContent = itemView.findViewById(R.id.tv_setting_content);
            tvExplain = itemView.findViewById(R.id.tv_setting_explanation);
            ivEdit = itemView.findViewById(R.id.iv_edit_setting);
            constraintLayout = itemView.findViewById(R.id.cl_setting_detail);
            mapView = itemView.findViewById(R.id.map_view_setting);

            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);

            }

            ivEdit.setOnClickListener(this);
        }

        void bindData(CompanySetting companySetting) {
            String value = companySetting.getValue();
            int valueType = companySetting.getValueType();

            if (valueType == SettingValueTypes.location) {
                mapView.setVisibility(View.VISIBLE);
                mapView.onResume();
                tvExplain.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                setUpLocation();
            } else {
                mapView.setVisibility(View.GONE);

                if (value == null) value = "محتوایی انتخاب نشده!";

                else if (value.equals("True"))
                    value = "فعال";
                else if (value.equals("False")) value = "غیرفعال";
            }

            tvTitle.setText(companySetting.getTitle());
            tvContent.setText(value);
            tvExplain.setText(companySetting.getExplain());

            if (companySetting.isEditable())
                ivEdit.setVisibility(View.VISIBLE);
            else ivEdit.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            iSettingEditAdapter.onEditClicked(settingList.get(getAdapterPosition()), getAdapterPosition());
            Log.d(TAG, "onClick: " + settingList.get(getAdapterPosition()).getTitle());
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            map = googleMap;

            setUpLocation();
        }

        private void setUpLocation() {
            if (map == null) return;

            if (settingList.get(getAdapterPosition()).getValueType() != SettingValueTypes.location)
                return;

            /*String[] location = settingList.get(getAdapterPosition()).getValue().split(":");
            // Add a marker for this item and set the camera

            LatLng latLng = new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
            map.addMarker(new MarkerOptions().position(latLng));
            map.getUiSettings().setAllGesturesEnabled(false);*/

        }

    }
}
