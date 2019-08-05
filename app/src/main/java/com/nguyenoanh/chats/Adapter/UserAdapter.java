package com.nguyenoanh.chats.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> listUser;

    public UserAdapter(Context context, ArrayList<User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from (context);

        View view = inflater.inflate (R.layout.item_user, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        User user = listUser.get (i);

        holder.tvUser.setText (user.getUserName ());
        holder.imvAvatar.setImageDrawable (context.getResources ().getDrawable (user.getInmageURL ()));
        if(Objects.equals (user.getInmageURL (), "default"))
            holder.imvAvatar.setImageResource (R.drawable.anh1);
        else{
            Glide.with(context).load (user.getInmageURL ());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imvAvatar;
        public TextView tvUser;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            imvAvatar = (ImageView) itemView.findViewById (R.id.imv_avatar);
            tvUser = (TextView) itemView.findViewById (R.id.tv_name);
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size ();
    }
}
