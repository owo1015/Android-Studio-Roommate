package com.example.roommate.Memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

public class MemoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MemoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Memo> arrayList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        // 메모 데이터 출력
        recyclerView = findViewById(R.id.memo_list);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        readMemoData();

        // 어댑터 연결
        adapter = new MemoAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        // 뒤로 가기
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 메모 추가
        Button btn_add = findViewById(R.id.btn_memo_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemoDialog memoDialog = new MemoDialog(MemoActivity.this);
                memoDialog.setDialogListener(new MemoDialog.DialogListener() {
                    @Override
                    public void onPositiveClicked(String email, String name, String roomCode, String content, String date, String time) {
                        // 메모 데이터 삽입
                        if (content.length() != 0) {
                            DatabaseReference rootRef = database.getReference();
                            DatabaseReference dataRef = rootRef.child("memo");
                            DatabaseReference itemRef = dataRef.push();
                            itemRef.child("email").setValue(email);
                            itemRef.child("name").setValue(name);
                            itemRef.child("roomCode").setValue(roomCode);
                            itemRef.child("content").setValue(content);
                            itemRef.child("date").setValue(date);
                            itemRef.child("time").setValue(time);

                            arrayList.add(new Memo(email, name, roomCode, content, date, time));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                memoDialog.show();
            }
        });

        // 메모 수정, 삭제
        adapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Memo item = adapter.getItem(position);
                MemoDialog2 memoDialog2 = new MemoDialog2(MemoActivity.this, item.getEmail(), item.getName(), item.getRoomCode(), item.getContent());
                memoDialog2.setDialogListener(new MemoDialog2.DialogListener() {
                    @Override
                    public void onPositiveClicked(String email, String name, String roomCode, String content) {
                        // 메모 데이터 수정
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("memo");
                        Query query = dataRef.orderByChild("content").equalTo(item.getContent());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().child("content").setValue(content);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        item.setContent(content);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onNegativeClicked() {
                        // 메모 데이터 삭제
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("memo");
                        Query query = dataRef.orderByChild("content").equalTo(item.getContent());
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
                memoDialog2.show();
            }
        });

        // 당겨서 새로 고침
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readMemoData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    protected void readMemoData() {
        databaseReference = database.getReference("user"); // 데이터베이스 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    User userData = snapshot1.getValue(User.class);
                    if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        DatabaseReference rootRef = database.getReference();
                        DatabaseReference dataRef = rootRef.child("memo");

                        Query query = dataRef.orderByChild("roomCode").equalTo(userData.getRoomCode());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                arrayList.clear();
                                for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                    Memo memo = snapshot2.getValue(Memo.class);
                                    arrayList.add(0, memo); // 데이터를 배열리스트에 넣고 리사이클러뷰로 보낼 준비
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