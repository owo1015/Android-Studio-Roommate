package com.example.roommate.Rule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.RuleViewHolder> {

    private ArrayList<Rule> arrayList;
    private Context context;

    public RuleAdapter(ArrayList<Rule> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rule_list, parent, false);
        RuleViewHolder holder = new RuleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RuleViewHolder holder, int position) {
        holder.rule_content.setText(arrayList.get(position).getRule());
        holder.rule_detail.setText(arrayList.get(position).getRuleDetail());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public Rule getItem(int position) {
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

    public class RuleViewHolder extends RecyclerView.ViewHolder {
        TextView rule_content;
        TextView rule_detail;

        public RuleViewHolder(@NonNull View itemView) {
            super(itemView);

            this.rule_content = itemView.findViewById(R.id.txt_rule_content);
            this.rule_detail = itemView.findViewById(R.id.txt_rule_detail);

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
