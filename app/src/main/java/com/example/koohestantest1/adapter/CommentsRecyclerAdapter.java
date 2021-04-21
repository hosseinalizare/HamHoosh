package com.example.koohestantest1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.model.GetComments;

import java.util.Date;
import java.util.List;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.CommentViewHolder> {
    private Context context;
    private List<GetComments> commentsList;
    private DeleteCommentClickListener listener;
    BaseCodeClass baseCodeClass;


    public CommentsRecyclerAdapter(Context context, List<GetComments> commentsList, DeleteCommentClickListener listener) {
        this.context = context;
        this.commentsList = commentsList;
        this.listener = listener;
        baseCodeClass = new BaseCodeClass();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        GetComments comment = commentsList.get(position);
        holder.txtSenderComment.setText(comment.getSenderName());
        holder.txtCommentContent.setText(comment.getData());
        String url = baseCodeClass.BASE_URL + "User/DownloadFile?UserID=" + comment.getSenderId() + "&fileNumber=" + 1;
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_profile).into(holder.imgUser);

        String strLastSeen = comment.getDate();
        if (strLastSeen != null) {
            Date lastSeenDate = TimeUtils.convertStrToDate(strLastSeen);
            Date now = new Date(System.currentTimeMillis());
            String durationTime = TimeUtils.getDurationExpression(lastSeenDate, now);
            holder.txtCommentDate.setText(durationTime);
        }

        if (comment.getEditAble()) {
            holder.imgDeleteComment.setVisibility(View.VISIBLE);
        }else {
            holder.imgDeleteComment.setVisibility(View.GONE);
        }

        holder.imgDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImgDeleteCommentListener(String.valueOf(comment.getCommentID()), comment.getSenderId(), comment.getData(),position);


            }
        });


        if (!comment.getPublised() && comment.getEmployee()){
            holder.imgShareComment.setVisibility(View.VISIBLE);
            holder.parent.setCardBackgroundColor(Color.rgb(243,233,227));
        }else {
            holder.imgShareComment.setVisibility(View.GONE);
            holder.parent.setCardBackgroundColor(Color.WHITE);
        }

        holder.imgShareComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImgShareCommentListener(String.valueOf(comment.getCommentID()),baseCodeClass.getUserID(),baseCodeClass.getToken(),null,holder.imgShareComment,holder.parent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgUser;
        ImageView imgDeleteComment,imgShareComment;
        TextView txtSenderComment, txtCommentDate, txtCommentContent;
        CardView parent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_rowItemComment_profile);
            imgDeleteComment = itemView.findViewById(R.id.img_deleteComment);
            imgShareComment = itemView.findViewById(R.id.img_shareComment);

            txtSenderComment = itemView.findViewById(R.id.txt_rowItemComment_senderComment);
            txtCommentDate = itemView.findViewById(R.id.tv_commentTime);
            txtCommentContent = itemView.findViewById(R.id.tv_comment_content);
            parent = itemView.findViewById(R.id.card_parentRowItemComment);

        }
    }

    public interface DeleteCommentClickListener {
        void onImgDeleteCommentListener(String objectId, String userId, String data,int position);
        void onImgShareCommentListener(String objectId,String userId,String token,String data,ImageView imageView,CardView  cardView);
    }


}
