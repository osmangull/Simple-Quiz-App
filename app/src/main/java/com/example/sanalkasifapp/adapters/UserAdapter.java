package com.example.sanalkasifapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanalkasifapp.R;
import com.example.sanalkasifapp.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private ArrayList<User> userList;
    public UserAdapter(ArrayList<User> userList){
        this.userList = userList;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getMail());
    }

    @Override
    public int getItemCount() {
        if (userList == null)
            return 0;
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
        }
    }

    public void refreshAdapter(ArrayList<User> old){
        this.userList = old;
        this.notifyDataSetChanged();
    }
}