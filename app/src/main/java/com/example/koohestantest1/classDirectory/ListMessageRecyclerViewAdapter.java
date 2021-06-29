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
import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.R;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ViewModels.ContactListViewModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListMessageRecyclerViewAdapter extends RecyclerView.Adapter<ListMessageRecyclerViewAdapter.ViewHolder> {
    private String TAG = ListMessageRecyclerViewAdapter.class.getSimpleName();
    private Context mcContext;
    private List<ContactListViewModel> contactListViewModels;
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    private String sender;

    public ListMessageRecyclerViewAdapter(Context mcContext, List<ContactListViewModel> contactListViewModels,String sender) {
        this.mcContext = mcContext;
        this.contactListViewModels = contactListViewModels;
        this.sender=sender;
    }

    public void updateData(List<ContactListViewModel> contactListViewModels) {
        this.contactListViewModels = contactListViewModels;
        notifyDataSetChanged();
    }

    public void searchInList(String word) {
        List<ContactListViewModel> searchedItem = new ArrayList<>();
        for (int i = 0; i < contactListViewModels.size(); i++) {
            if (contactListViewModels.get(i).getObjectName().contains(word)) {
                searchedItem.add(contactListViewModels.get(i));
            }
        }
        contactListViewModels = searchedItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_see_message, parent, false);

        return new ListMessageRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        boolean showNumberPermission =false;
        if (baseCodeClass.getPermissions() !=null && baseCodeClass.getPermissions().size()>0){
            showNumberPermission =        baseCodeClass.getPermissions().get(BaseCodeClass.EmploeeAccessLevel.ViewCustomerMobilePhone.getValue()).isState();
;

        }

        if(!showNumberPermission){
            String objectNumber = contactListViewModels.get(position).getObjectName();
            if(objectNumber.startsWith("09")) {
                StringBuilder hidingNumber = new StringBuilder(contactListViewModels.get(position).getObjectName());
                for (int i = 7; i < hidingNumber.length(); i++) {
                    hidingNumber.setCharAt(i, 'x');
                }
                String hiddenNumber = hidingNumber.toString();
                holder.name.setText(hiddenNumber);
            }else{
                holder.name.setText(contactListViewModels.get(position).getObjectName());
            }
        }
        else{
            holder.name.setText(contactListViewModels.get(position).getObjectName());
        }
        //holder.name.setText(contactListViewModels.get(position).getObjectName());
        holder.text.setText(contactListViewModels.get(position).getMessage1());
        String time = contactListViewModels.get(position).getLastMsgTime();
        holder.time.setText(time);



        String url = baseCodeClass.BASE_URL + "User/DownloadFile?UserID=" + contactListViewModels.get(position).getObjectID() + "&fileNumber=" + 1;
        Glide.with(mcContext).load(url)
                .placeholder(R.drawable.ic_profile).into(holder.image);
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)


        Log.d(TAG, "onBindViewHolder: " + time);
        if (contactListViewModels.get(position).getCountNewMsg().equals("0")) {
            holder.newMessage.setVisibility(View.GONE);
        } else {
            holder.newMessage.setVisibility(View.VISIBLE);
        }
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mcContext, MessageActivity.class);
            // getter = Another user
            intent.putExtra("getter", contactListViewModels.get(position).getObjectID());
            // sender = Ourselves
            intent.putExtra("sender", sender);
            mcContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactListViewModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name, text, time;
        ImageView newMessage;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.senderMessage);
            text = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.messageTime);
            image = itemView.findViewById(R.id.ProfileImage);
            newMessage = itemView.findViewById(R.id.newMessage);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
