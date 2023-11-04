package com.example.roommate.Album;

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
import android.widget.Button;
import android.widget.TextView;

import com.example.roommate.R;
import com.example.roommate.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {

    private View v;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    private RecyclerView recyclerView;
    private AlbumAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Photo> arrayList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_album, container, false);

        recyclerView = v.findViewById(R.id.album_list);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        // 앨범 데이터 출력
        database = FirebaseDatabase.getInstance();
        readAlbumData();

        // 어댑터 연결
        adapter = new AlbumAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);

        // 사진 추가 페이지 이동
        Button photo_add = v.findViewById(R.id.btn_photo_add);
        photo_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AlbumUploadActivity.class);
                startActivity(intent);
            }
        });

        // 사진 상세 페이지 이동 + 선택한 사진 데이터 전송
        adapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Photo item = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), AlbumViewActivity.class);
                intent.putExtra("email", item.getEmail());
                intent.putExtra("name", item.getName());
                intent.putExtra("roomCode", item.getRoomCode());
                intent.putExtra("imageUrl", item.getImageUrl());
                intent.putExtra("comment", item.getComment());
                startActivity(intent);
            }
        });

        // 당겨서 새로 고침
        SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readAlbumData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

    protected void readAlbumData() {
        databaseReference = database.getReference("user"); // 데이터베이스 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    User userData = snapshot1.getValue(User.class);
                    if (currentUser != null) {
                        if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            DatabaseReference rootRef = database.getReference();
                            DatabaseReference dataRef = rootRef.child("photo");

                            Query query = dataRef.orderByChild("roomCode").equalTo(userData.getRoomCode());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    arrayList.clear();
                                    for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                        Photo photo = snapshot2.getValue(Photo.class);
                                        arrayList.add(0, photo); // 데이터를 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                                    }
                                    adapter.notifyDataSetChanged();

                                    // 사진이 없으면 텍스트 출력
                                    TextView text = v.findViewById(R.id.album_text);
                                    if(arrayList.size() > 0)
                                        text.setVisibility(View.INVISIBLE);
                                    else
                                        text.setVisibility(View.VISIBLE);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 발생 시
            }
        });
    }
}