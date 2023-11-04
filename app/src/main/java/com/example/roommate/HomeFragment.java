package com.example.roommate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roommate.Memo.MemoActivity;
import com.example.roommate.dday.Dday;
import com.example.roommate.dday.DdayActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    private Calendar todayDate = Calendar.getInstance();
    private Calendar startDate = Calendar.getInstance();
    private long count;

    private RecyclerView recyclerView;
    private HomeDdayAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Dday> arrayList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // 시작 날짜 디데이 출력
        TextView dayCount = v.findViewById(R.id.txt_day_count);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("user");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userData = snapshot.getValue(User.class);
                    if (currentUser != null) {
                        if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            // 시작 날짜 디데이 계산
                            String[] date = userData.getStartDate().split("/");
                            startDate.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
                            count = (todayDate.getTimeInMillis() / 86400000) - (startDate.getTimeInMillis() / 86400000) + 1;
                            dayCount.setText(count + "일");

                            // 마스코트 성장, 30일 단위로 4단계 진행
                            ImageView mascot = v.findViewById(R.id.img_mascot);
                            if(count >= 1 && count < 30)
                                mascot.setImageResource(R.drawable.mascot1);
                            else if(count >= 30 && count < 60)
                                mascot.setImageResource(R.drawable.mascot2);
                            else if(count >= 60 && count < 90)
                                mascot.setImageResource(R.drawable.mascot3);
                            else if(count >= 90)
                                mascot.setImageResource(R.drawable.mascot4);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 발생 시
            }
        });

        // 사용자 디데이 출력
        recyclerView = v.findViewById(R.id.home_dday_list);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        readDdayData();

        // 어댑터 연결
        adapter = new HomeDdayAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);

        // 디데이 페이지 이동
        dayCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DdayActivity.class);
                startActivity(intent);
            }
        });

        // 메모 페이지 이동
        ImageButton memo = v.findViewById(R.id.btn_memo);
        memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemoActivity.class);
                startActivity(intent);
            }
        });

        // 당겨서 새로 고침
        SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDdayData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

    protected void readDdayData() {
        databaseReference = database.getReference("user"); // 데이터베이스 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    User userData = snapshot1.getValue(User.class);
                    if (currentUser != null) {
                        if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            DatabaseReference rootRef = database.getReference();
                            DatabaseReference dataRef = rootRef.child("dday");

                            Query query = dataRef.orderByChild("roomCode").equalTo(userData.getRoomCode());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    arrayList.clear();
                                    for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                        Dday dday = snapshot2.getValue(Dday.class);
                                        if (arrayList.size() < 4) {
                                            arrayList.add(dday); // 데이터를 배열리스트에 넣고 리사이클러뷰로 보낼 준비
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 발생 시
            }
        });
    }
}