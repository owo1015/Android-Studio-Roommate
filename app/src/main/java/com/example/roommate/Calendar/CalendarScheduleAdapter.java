package com.example.roommate.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;

public class CalendarScheduleAdapter extends RecyclerView.Adapter<CalendarScheduleAdapter.CalendarScheduleViewHolder> {

    private ArrayList<Schedule> arrayList;
    private Context context;

    public CalendarScheduleAdapter(ArrayList<Schedule> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CalendarScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_calendar_schedulelist, parent, false);
        CalendarScheduleViewHolder holder = new CalendarScheduleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarScheduleViewHolder holder, int position) {
        holder.schedule_title.setText(arrayList.get(position).getTitle());
        holder.schedule_date.setText(arrayList.get(position).getDate());
        holder.schedule_time.setText(arrayList.get(position).getTime());
        holder.schedule_memo.setText(arrayList.get(position).getMemo());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public Schedule getItem(int position) {
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

    public class CalendarScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView schedule_title;
        TextView schedule_date;
        TextView schedule_time;
        TextView schedule_memo;

        public CalendarScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            this.schedule_title = itemView.findViewById(R.id.txt_schedule_title);
            this.schedule_date = itemView.findViewById(R.id.txt_schedule_date);
            this.schedule_time = itemView.findViewById(R.id.txt_schedule_time);
            this.schedule_memo = itemView.findViewById(R.id.txt_schedule_memo);

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
