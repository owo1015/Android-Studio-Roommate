package com.example.roommate.Rule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.roommate.Memo.Memo;
import com.example.roommate.Memo.MemoActivity;
import com.example.roommate.Memo.MemoDialog;
import com.example.roommate.R;
import com.example.roommate.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RuleDetailCustomActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RuleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Rule> arrayList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_detail_custom);

        recyclerView = findViewById(R.id.rule_list);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        // 규칙 데이터 출력
        database = FirebaseDatabase.getInstance();
        readRuleData();

        // 어댑터 연결
        adapter = new RuleAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        // 뒤로 가기
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 규칙 추가
        Button rule_add = findViewById(R.id.btn_rule_add);
        rule_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RuleDialogCustom ruleDialogCustom = new RuleDialogCustom(RuleDetailCustomActivity.this);
                ruleDialogCustom.setDialogListener(new RuleDialogCustom.DialogListener() {
                    @Override
                    public void onPositiveClicked(String roomCode, String rule, String detail, String type) {
                        // 규칙 데이터 삽입
                        if (rule.length() != 0) {
                            DatabaseReference rootRef = database.getReference();
                            DatabaseReference dataRef = rootRef.child("rule");
                            DatabaseReference itemRef = dataRef.push();
                            itemRef.child("roomCode").setValue(roomCode);
                            itemRef.child("rule").setValue(rule);
                            itemRef.child("ruleDetail").setValue(detail);
                            itemRef.child("type").setValue(type);

                            arrayList.add(new Rule(roomCode, rule, detail, type));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                ruleDialogCustom.show();
            }
        });

        // 규칙 수정, 삭제
        adapter.setOnItemClickListener(new RuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Rule item = adapter.getItem(position);
                RuleDialogCustom2 ruleDialogCustom2 = new RuleDialogCustom2(RuleDetailCustomActivity.this, item.getRoomCode(), item.getRule(), item.getRuleDetail());
                ruleDialogCustom2.setDialogListener(new RuleDialogCustom2.DialogListener() {
                    @Override
                    public void onPositiveClicked(String roomCode, String rule, String detail) {
                        // 규칙 데이터 수정
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("rule");
                        Query query = dataRef.orderByChild("rule").equalTo(item.getRule());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().child("rule").setValue(rule);
                                    dataSnapshot.getRef().child("ruleDetail").setValue(detail);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        item.setRule(rule);
                        item.setRuleDetail(detail);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegativeClicked() {
                        // 규칙 데이터 삭제
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("rule");
                        Query query = dataRef.orderByChild("rule").equalTo(item.getRule());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        arrayList.remove(item);
                        adapter.notifyDataSetChanged();
                    }
                });
                ruleDialogCustom2.show();
            }
        });

        // 당겨서 새로 고침
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readRuleData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    protected void readRuleData() {
        databaseReference = database.getReference("user"); // 데이터베이스 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    User userData = snapshot1.getValue(User.class);
                    if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("rule");

                        Query query = dataRef.orderByChild("roomCode").equalTo(userData.getRoomCode());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                arrayList.clear();
                                for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                    Rule rule = snapshot2.getValue(Rule.class);
                                    if(rule.getType().equals("사용자 지정")) {
                                        arrayList.add(rule); // 데이터를 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                                        Collections.sort(arrayList);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 발생 시
            }
        });

    }
}