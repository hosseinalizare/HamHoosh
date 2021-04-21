package com.example.koohestantest1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koohestantest1.R;
import com.example.koohestantest1.fragments.transinterface.ISettingClick;

public class IconListViewAdapter extends BaseAdapter {

    private Context mContext;
    private String[] Title;
    private int[] imge;
    private ISettingClick iSettingClick;

    public IconListViewAdapter(Context context, String[] text1, int[] imageIds, ISettingClick iSettingClick) {
        mContext = context;
        Title = text1;
        imge = imageIds;
        this.iSettingClick = iSettingClick;

    }

    public int getCount() {
        return Title.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row;
        row = inflater.inflate(R.layout.list_view_text_icon_layout, parent, false);
        TextView title;
        ImageView i1;
        i1 = row.findViewById(R.id.imgIcon);
        title = row.findViewById(R.id.txtTitle);
        title.setText(Title[position]);
        i1.setImageResource(imge[position]);
        row.setOnClickListener(v -> {
            if (position == 0)
                iSettingClick.onUpdateClicked();
            else if (position == 1)
                iSettingClick.onReportClicked();
        });
        return (row);
    }
}
