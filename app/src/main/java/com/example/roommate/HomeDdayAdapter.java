package com.example.roommate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.dday.Dday;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeDdayAdapter extends RecyclerView.Adapter<HomeDdayAdapter.HomeDdayViewHolder> {

    private ArrayList<Dday> arrayList;
    private Context context;

    public HomeDdayAdapter(ArrayList<Dday> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeDdayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_dday_list, parent, false);
        HomeDdayViewHolder holder = new HomeDdayViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDdayViewHolder holder, int position) {
        holder.home_dday_title.setText(arrayList.get(position).getTitle());

        String date = arrayList.get(position).getDate();
        String date_array[] = date.split("/");

        Calendar dday_date = Calendar.getInstance();
        Calendar today_date = Calendar.getInstance();

        dday_date.set(Calendar.YEAR, Integer.parseInt(date_array[0]));
        dday_date.set(Calendar.MONTH, Integer.parseInt(date_array[1]) - 1);
        dday_date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date_array[2]));

        long count = (dday_date.getTimeInMillis() / 86400000) - (today_date.getTimeInMillis() / 86400000);

        if(count > 0)
            holder.home_dday_date.setText("D-" + count);
        else if(count == 0)
            holder.home_dday_date.setText("D-Day");
        else if(count < 0)
            holder.home_dday_date.setText("D+" + Math.abs(count));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public Dday getItem(int position) {
        return arrayList.get(position);
    }

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조 변수
    private OnItemClickListener mListener = null;

    // 리스너 객체 참조를 어댑터에 전달
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class HomeDdayViewHolder extends RecyclerView.ViewHolder {
        TextView home_dday_title;
        TextView home_dday_date;

        public HomeDdayViewHolder(@NonNull View itemView) {
            super(itemView);

            this.home_dday_title = itemView.findViewById(R.id.txt_home_dday_title);
            this.home_dday_date = itemView.findViewById(R.id.txt_home_dday_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, position);
                        }
                    }
                }
            });
        }
    }
}
