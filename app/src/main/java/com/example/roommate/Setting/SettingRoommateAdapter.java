package com.example.roommate.Setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;
import com.example.roommate.User;

import java.util.ArrayList;

public class SettingRoommateAdapter extends RecyclerView.Adapter<SettingRoommateAdapter.RoommateViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;

    public SettingRoommateAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoommateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_setting_roommate_list, parent, false);
        RoommateViewHolder holder = new RoommateViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoommateViewHolder holder, int position) {
        holder.roommate_name.setText(arrayList.get(position).getName());
        holder.roommate_email.setText(arrayList.get(position).getEmail());
        holder.roommate_phone.setText(arrayList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class RoommateViewHolder extends RecyclerView.ViewHolder {
        TextView roommate_name;
        TextView roommate_email;
        TextView roommate_phone;

        public RoommateViewHolder(@NonNull View itemView) {
            super(itemView);

            this.roommate_name = itemView.findViewById(R.id.txt_roommate_name);
            this.roommate_email = itemView.findViewById(R.id.txt_roommate_email);
            this.roommate_phone = itemView.findViewById(R.id.txt_roommate_phone);
        }
    }
}
