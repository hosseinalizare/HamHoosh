package com.example.koohestantest1.adapter;

import android.content.Context;
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
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContactList extends RecyclerView.Adapter<AdapterContactList.ContactViewHolder> {
    private Context mcContext;
    private List<ContactListViewModel> contactListViewModels;
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    private String sender;
    private OnItemClickContactList onItemClickContactList;

    public AdapterContactList(Context mcContext, List<ContactListViewModel> contactListViewModels, String sender,OnItemClickContactList onItemClickContactList) {
        this.mcContext = mcContext;
        this.contactListViewModels = contactListViewModels;
        this.sender = sender;
        this.onItemClickContactList = onItemClickContactList;
    }

    @NonNull
    @NotNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcContext).inflate(R.layout.layout_item_contact_list,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContactViewHolder holder, int position) {
        ContactListViewModel contactListViewModel = contactListViewModels.get(position);

        holder.txtContactName.setText(contactListViewModel.getObjectName());

        String url = baseCodeClass.BASE_URL + "User/DownloadFile?UserID=" + contactListViewModel.getObjectID() + "&fileNumber=" + 1;
        Glide.with(mcContext).load(url)
                .placeholder(R.drawable.ic_profile).into(holder.imgProfile);

        holder.root.setOnClickListener(v -> {
            onItemClickContactList.onContactClicked(holder.imgChecked,contactListViewModel.getObjectName(),contactListViewModel.getObjectID());
        });

    }

    @Override
    public int getItemCount() {
        return contactListViewModels.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgProfile;
        TextView txtContactName;
        ImageView imgChecked;
        CardView root;
        public ContactViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.ProfileImage);
            txtContactName = itemView.findViewById(R.id.senderMessage);
            imgChecked = itemView.findViewById(R.id.img_selected);
            root = itemView.findViewById(R.id.cardview_root);
        }
    }


    public interface OnItemClickContactList{
        void onContactClicked(ImageView imageView,String contactName,String contactId);
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
    public void updateData(List<ContactListViewModel> contactListViewModels) {
        this.contactListViewModels = contactListViewModels;
        notifyDataSetChanged();
    }

}
