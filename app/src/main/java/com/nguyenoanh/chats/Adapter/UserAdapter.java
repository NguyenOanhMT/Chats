package com.nguyenoanh.chats.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nguyenoanh.chats.Activity.Message;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> listUser;

    public UserAdapter(Context context, ArrayList<User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from (context);

        View view = inflater.inflate (R.layout.item_user, null);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final User user = listUser.get (i);

        holder.tvUser.setText (user.getUserName ());

        String url = user.getImageURL();
        if (url != null && url.equals("default")) {
            holder.profileImage.setImageResource (R.drawable.anh);
        }else{
            Glide.with(context).load (user.getImageURL ()).into (holder.profileImage);
        }

        // check event click item_user move activity message
        holder.itemView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Message.class);

                intent.putExtra ("userid", user.getId () );
                context.startActivity (intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profileImage;
        public TextView tvUser;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            profileImage = (CircleImageView) itemView.findViewById (R.id.imv_avatar);
            tvUser = (TextView) itemView.findViewById (R.id.tv_name);
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size ();
    }
}
