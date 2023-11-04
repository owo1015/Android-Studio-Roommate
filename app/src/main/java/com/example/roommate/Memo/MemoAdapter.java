package com.example.roommate.Memo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private ArrayList<Memo> arrayList;
    private Context context;

    public MemoAdapter(ArrayList<Memo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_memo_list, parent, false);
        MemoViewHolder holder = new MemoViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        holder.memo_content.setText(arrayList.get(position).getContent());
        holder.memo_date.setText(arrayList.get(position).getDate());
        holder.memo_time.setText(arrayList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public Memo getItem(int position) {
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

    public class MemoViewHolder extends RecyclerView.ViewHolder {
        TextView memo_content;
        TextView memo_date;
        TextView memo_time;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);

            this.memo_content = itemView.findViewById(R.id.txt_memo_content);
            this.memo_date = itemView.findViewById(R.id.txt_memo_date);
            this.memo_time = itemView.findViewById(R.id.txt_memo_time);

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
