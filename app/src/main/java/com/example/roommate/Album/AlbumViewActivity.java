package com.example.roommate.Album;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roommate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AlbumViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);

        // 선택한 사진 데이터 가져오기
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        String roomCode = intent.getStringExtra("roomCode");
        String imageUrl = intent.getStringExtra("imageUrl");
        String comment = intent.getStringExtra("comment");

        // 선택한 사진 데이터 표시
        TextView photo_uploader = (TextView) findViewById(R.id.txt_photo_uploader);
        ImageView photo_image = (ImageView) findViewById(R.id.img_photo);
        TextView photo_comment = (TextView) findViewById(R.id.txt_photo_comment);

        photo_uploader.setText(name);
        photo_comment.setText(comment);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        storageReference.child(roomCode + "/" + imageUrl).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //이미지 로드 성공
                        Glide.with(AlbumViewActivity.this)
                                .load(uri)
                                .into(photo_image);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //이미지 로드 실패
                        Toast.makeText(AlbumViewActivity.this, "이미지 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });

        // 뒤로 가기
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        // 사진 삭제
        Button btn_delete = findViewById(R.id.btn_photo_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference rootRef = database.getReference();
                DatabaseReference dataRef = rootRef.child("photo");
                Query query = dataRef.orderByChild("imageUrl").equalTo(imageUrl);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Realtime Database에서 데이터 삭제
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            dataSnapshot.getRef().removeValue();
                        }

                        // Storage에서 데이터 삭제
                        StorageReference storageReference = storage.getReference();
                        String[] uriArray = imageUrl.split("/");
                        StorageReference imageReference = storageReference.child(roomCode + "/" + uriArray[uriArray.length - 1]);
                        imageReference.delete();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                finish();
            }
        });

        // 사진 업로더인 사용자에게만 삭제 버튼 활성화
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email)) {
            btn_delete.setVisibility(View.VISIBLE);
        }
    }
}