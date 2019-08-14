package com.nguyenoanh.chats.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenoanh.chats.Model.Chat;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private ArrayList<Chat> listChat;

    private String imageURL;

    public MessageAdapter(Context context, ArrayList<Chat> listChat, String imageURL) {
        this.context = context;
        this.listChat = listChat;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from (context);
        if(viewType == MSG_TYPE_RIGHT) {
            View view = inflater.inflate (R.layout.item_chat_right, null);
            return new MessageAdapter.ViewHolder (view);
        }else{
            View view = inflater.inflate (R.layout.item_chat_left, null);
            return new MessageAdapter.ViewHolder (view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int i) {

        Chat chat = listChat.get (i);

        switch (holder.getItemViewType ()){
            case 0:
                holder.showMess.setText (chat.getMessage ());
                if (imageURL.equals("default")){
                    holder.profileImage.setImageResource(R.drawable.anh);
                } else {
                    Glide.with(context).load(imageURL).into(holder.profileImage);
                }
                if (i == (listChat.size ()-1)){
                    if(chat.isIsseen ()){
                        holder.tvSeen.setText ("Seen");
                    }else {
                        holder.tvSeen.setText ("Not seen");
                    }
                }else {
                    holder.tvSeen.setVisibility (View.GONE);
                }
                break;
            case 1:
                holder.showMess.setText (chat.getMessage ());
                if (i == (listChat.size ()-1)){
                    if(chat.isIsseen ()){
                        holder.tvSeen.setText ("Seen");
                    }else {
                        holder.tvSeen.setText ("Not seen");
                    }
                }else {
                    holder.tvSeen.setVisibility (View.GONE);
                }
                break;
            default:
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profileImage;
        public TextView showMess, tvSeen;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            profileImage = (CircleImageView) itemView.findViewById (R.id.profileImage);
            showMess = (TextView) itemView.findViewById (R.id.show_mess);
            tvSeen = (TextView) itemView.findViewById (R.id.tv_time_seen);
        }
    }

    @Override
    public int getItemViewType(int i){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();
        if(listChat.get (i).getSender ().equals (firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return listChat.size ();
    }
}
