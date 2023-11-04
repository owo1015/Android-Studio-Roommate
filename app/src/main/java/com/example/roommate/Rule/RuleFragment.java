package com.example.roommate.Rule;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.roommate.R;

public class RuleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_rule, container, false);

        Button category1 = v.findViewById(R.id.rule_category1);
        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RuleDetailCleaningActivity.class);
                startActivity(intent);
            }
        });

        Button category2 = v.findViewById(R.id.rule_category2);
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RuleDetailLifestyleActivity.class);
                startActivity(intent);
            }
        });

        Button category3 = v.findViewById(R.id.rule_category3);
        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RuleDetailNoiseActivity.class);
                startActivity(intent);
            }
        });

        Button category4 = v.findViewById(R.id.rule_category4);
        category4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RuleDetailSettlementActivity.class);
                startActivity(intent);
            }
        });

        Button category5 = v.findViewById(R.id.rule_category5);
        category5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RuleDetailCustomActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}