package com.nguyenoanh.chats.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                holder.profileImage.setImageResource (R.drawable.anh);
                break;
            case 1:
                holder.showMess.setText (chat.getMessage ());
                break;
            default:
                break;

        }
//        holder.showMess.setText (chat.getMessage ());

//        if(user.getInmageURL ().equals ("default"))
//        holder.profileImage.setImageResource (R.drawable.anh1);
//        else{
//            Glide.with(context).load (user.getInmageURL ()).into (holder.profileImage);
//        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profileImage;
        public TextView showMess;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            profileImage = (CircleImageView) itemView.findViewById (R.id.profileImage);
            showMess = (TextView) itemView.findViewById (R.id.show_mess);
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
