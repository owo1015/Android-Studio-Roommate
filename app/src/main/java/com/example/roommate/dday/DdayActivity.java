package com.example.roommate.dday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.roommate.HomeFragment;
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
import java.util.Calendar;
import java.util.Collections;

public class DdayActivity extends AppCompatActivity {

    private Calendar todayDate = Calendar.getInstance();
    private Calendar startDate = Calendar.getInstance();
    private long count;

    private RecyclerView recyclerView;
    private DdayAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Dday> arrayList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dday);

        // 시작 날짜 디데이 출력
        TextView dayCount = findViewById(R.id.txt_day_count);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("user");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userData = snapshot.getValue(User.class);
                    if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        // 시작 날짜 디데이 계산
                        String[] date = userData.getStartDate().split("/");
                        startDate.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
                        count = (todayDate.getTimeInMillis() / 86400000) - (startDate.getTimeInMillis() / 86400000) + 1;
                        dayCount.setText(count + "일 째 함께 사는 중");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 발생 시
            }
        });

        // 디데이 데이터 출력
        recyclerView = findViewById(R.id.dday_list);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        readDdayData();

        // 어댑터 연결
        adapter = new DdayAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        // 뒤로 가기
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 디데이 추가
        Button dday_add = findViewById(R.id.btn_dday_add);
        dday_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DdayDialog ddayDialog = new DdayDialog(DdayActivity.this);
                ddayDialog.setDialogListener(new DdayDialog.DialogListener() {
                    @Override
                    public void onPositiveClicked(String email, String name, String roomCode, String title, String date) {
                        // 디데이 데이터 삽입
                        if (title.length() != 0 && date.length() != 0) {
                            DatabaseReference rootRef = database.getReference();
                            DatabaseReference dataRef = rootRef.child("dday");
                            DatabaseReference itemRef = dataRef.push();
                            itemRef.child("email").setValue(email);
                            itemRef.child("name").setValue(name);
                            itemRef.child("roomCode").setValue(roomCode);
                            itemRef.child("title").setValue(title);
                            itemRef.child("date").setValue(date);

                            arrayList.add(new Dday(email, name, roomCode, title, date));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                ddayDialog.show();
            }
        });

        // 디데이 수정, 삭제
        adapter.setOnItemClickListener(new DdayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Dday item = adapter.getItem(position);
                DdayDialog2 ddayDialog2 = new DdayDialog2(DdayActivity.this, item.getEmail(), item.getName(), item.getRoomCode(), item.getTitle(), item.getDate());
                ddayDialog2.setDialogListener(new DdayDialog2.DialogListener() {
                    @Override
                    public void onPositiveClicked(String email, String name, String roomCode, String title, String date) {
                        // 디데이 데이터 수정
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("dday");
                        Query query = dataRef.orderByChild("title").equalTo(item.getTitle());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().child("title").setValue(title);
                                    dataSnapshot.getRef().child("date").setValue(date);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        item.setTitle(title);
                        item.setDate(date);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onNegativeClicked() {
                        // 디데이 데이터 삭제
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("dday");
                        Query query = dataRef.orderByChild("title").equalTo(item.getTitle());
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
                ddayDialog2.show();
            }
        });

        // 당겨서 새로 고침
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDdayData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    protected void readDdayData() {
        databaseReference = database.getReference("user"); // 데이터베이스 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    User userData = snapshot1.getValue(User.class);
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
                                    arrayList.add(dday); // 데이터를 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                                    Collections.sort(arrayList);
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