package com.example.roommate.Album;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.roommate.R;
import com.example.roommate.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AlbumUploadActivity extends AppCompatActivity {

    private Uri imageUri;

    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_upload);

        storage = FirebaseStorage.getInstance();

        ImageView imageView = (ImageView) findViewById(R.id.img_view);
        EditText comment = (EditText) findViewById(R.id.edt_photo_comment);

        // 뒤로 가기
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        // 사진 가져오기
        ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        imageView.setImageURI(imageUri);
                    }
                });

        // 이미지 클릭, 갤러리 열기
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                activityResult.launch(galleryIntent);
            }
        });

        // 업로드 버튼 클릭
        Button btn_upload = findViewById(R.id.btn_photo_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                    databaseReference = database.getReference("user"); // 데이터베이스 테이블 연결
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                User userData = snapshot1.getValue(User.class);
                                if(userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                    String[] uriArray = imageUri.toString().split("/");
                                    String imageUrl = uriArray[uriArray.length - 1];

                                    // Realtime Database에 데이터 저장
                                    DatabaseReference rootRef = database.getReference();
                                    DatabaseReference dataRef = rootRef.child("photo");
                                    DatabaseReference itemRef = dataRef.push();
                                    itemRef.child("email").setValue(userData.getEmail());
                                    itemRef.child("name").setValue(userData.getName());
                                    itemRef.child("roomCode").setValue(userData.getRoomCode());
                                    itemRef.child("imageUrl").setValue(imageUrl);
                                    itemRef.child("comment").setValue(comment.getText().toString());

                                    // Storage에 데이터 저장
                                    StorageReference storageReference = storage.getReference();
                                    StorageReference imageReference = storageReference.child(userData.getRoomCode() + "/" + imageUrl);
                                    imageReference.putFile(imageUri);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    finish();
                }
                else {
                    Toast.makeText(AlbumUploadActivity.this, "사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}