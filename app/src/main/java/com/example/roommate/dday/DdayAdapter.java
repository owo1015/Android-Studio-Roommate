package com.example.roommate.dday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;

public class DdayAdapter extends RecyclerView.Adapter<DdayAdapter.DdayViewHolder> {

    private ArrayList<Dday> arrayList;
    private Context context;

    public DdayAdapter(ArrayList<Dday> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DdayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_dday_list, parent, false);
        DdayViewHolder holder = new DdayViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DdayViewHolder holder, int position) {
        holder.dday_title.setText(arrayList.get(position).getTitle());
        holder.dday_date.setText(arrayList.get(position).getDate());
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

    public class DdayViewHolder extends RecyclerView.ViewHolder {
        TextView dday_title;
        TextView dday_date;

        public DdayViewHolder(@NonNull View itemView) {
            super(itemView);

            this.dday_title = itemView.findViewById(R.id.txt_dday_title);
            this.dday_date = itemView.findViewById(R.id.txt_dday_date);

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
