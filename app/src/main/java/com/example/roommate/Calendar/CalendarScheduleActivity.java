package com.example.roommate.Calendar;

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

public class CalendarScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CalendarScheduleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Schedule> arrayList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String year;
    private String month;
    private String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule);

        // 날짜 데이터 가져오기
        Intent intent = getIntent();
        year = intent.getStringExtra("year");
        month = intent.getStringExtra("month");
        day = intent.getStringExtra("day");

        // 선택 날짜 표시
        TextView date = (TextView) findViewById(R.id.txt_date);
        date.setText(month + "월 " + day + "일");

        // 스케쥴 데이터 출력
        recyclerView = findViewById(R.id.calendar_schedulelist);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        readScheduleData();

        // 어댑터 연결
        adapter = new CalendarScheduleAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        // 뒤로 가기
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 스케쥴 추가
        Button schedule_add = findViewById(R.id.btn_schedule_add);
        schedule_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDialog calendarDialog = new CalendarDialog(CalendarScheduleActivity.this, year + "/" + month + "/" + day);
                calendarDialog.setDialogListener(new CalendarDialog.DialogListener() {
                    @Override
                    public void onPositiveClicked(String email, String name, String roomCode, String title, String date, String time, String memo) {
                        // 스케쥴 데이터 삽입
                        if (title.length() != 0 && date.length() != 0) {
                            DatabaseReference rootRef = database.getReference();
                            DatabaseReference dataRef = rootRef.child("schedule");
                            DatabaseReference itemRef = dataRef.push();
                            itemRef.child("email").setValue(email);
                            itemRef.child("name").setValue(name);
                            itemRef.child("roomCode").setValue(roomCode);
                            itemRef.child("title").setValue(title);
                            itemRef.child("date").setValue(date);
                            itemRef.child("time").setValue(time);
                            itemRef.child("memo").setValue(memo);

                            arrayList.add(new Schedule(email, name, roomCode, title, date, time, memo));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                calendarDialog.show();
            }
        });

        // 스케쥴 수정, 삭제
        adapter.setOnItemClickListener(new CalendarScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Schedule item = adapter.getItem(position);
                CalendarDialog2 calendarDialog2 = new CalendarDialog2(CalendarScheduleActivity.this, item.getEmail(), item.getName(), item.getRoomCode(), item.getTitle(), item.getDate(), item.getTime(), item.getMemo());
                calendarDialog2.setDialogListener(new CalendarDialog2.DialogListener() {
                    @Override
                    public void onPositiveClicked(String email, String name, String roomCode, String title, String date, String time, String memo) {
                        // 스케쥴 데이터 수정
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("schedule");
                        Query query = dataRef.orderByChild("title").equalTo(item.getTitle());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().child("title").setValue(title);
                                    dataSnapshot.getRef().child("date").setValue(date);
                                    dataSnapshot.getRef().child("time").setValue(time);
                                    dataSnapshot.getRef().child("memo").setValue(memo);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        item.setTitle(title);
                        item.setDate(date);
                        item.setTime(time);
                        item.setMemo(memo);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onNegativeClicked() {
                        // 스케쥴 데이터 삭제
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("schedule");
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
                calendarDialog2.show();
            }
        });

        // 당겨서 새로 고침
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readScheduleData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    protected void readScheduleData() {
        databaseReference = database.getReference("user"); // 데이터베이스 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    User userData = snapshot1.getValue(User.class);
                    if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("schedule");

                        Query query = dataRef.orderByChild("roomCode").equalTo(userData.getRoomCode());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                arrayList.clear();
                                for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                    Schedule schedule = snapshot2.getValue(Schedule.class);
                                    if(schedule.getDate().equals(year + "/" + month + "/" + day)) {
                                        arrayList.add(schedule); // 데이터를 배열리스트에 넣고 리사이클러뷰로 보낼 준비
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