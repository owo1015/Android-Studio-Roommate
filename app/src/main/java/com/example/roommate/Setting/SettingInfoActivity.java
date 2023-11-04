package com.example.roommate.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class SettingInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);

        EditText email = (EditText) findViewById(R.id.edt_setting_email);
        EditText name = (EditText) findViewById(R.id.edt_setting_name);
        EditText phone = (EditText) findViewById(R.id.editTextPhone);
        EditText roomCode = (EditText) findViewById(R.id.editTextRoomCode);
        EditText startDate = (EditText) findViewById(R.id.editTextStartDate);

        findViewById(R.id.btn_back).setOnClickListener(onClickListener);
        findViewById(R.id.btn_setting_info_save).setOnClickListener(onClickListener);

        // 사용자 데이터 출력
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userData = snapshot.getValue(User.class);
                    if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        email.setText(userData.getEmail());
                        name.setText(userData.getName());
                        phone.setText(userData.getPhone());
                        roomCode.setText(userData.getRoomCode());
                        startDate.setText(userData.getStartDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.btn_setting_info_save:
                    String name = ((EditText) findViewById(R.id.edt_setting_name)).getText().toString();
                    String phone = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
                    String roomCode = ((EditText) findViewById(R.id.editTextRoomCode)).getText().toString();
                    String startDate = ((EditText)findViewById(R.id.editTextStartDate)).getText().toString();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference rootRef = database.getReference();
                    DatabaseReference dataRef = rootRef.child("user");

                    Query query = dataRef.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                dataSnapshot.getRef().child("name").setValue(name);
                                dataSnapshot.getRef().child("phone").setValue(phone);
                                dataSnapshot.getRef().child("roomCode").setValue(roomCode);
                                dataSnapshot.getRef().child("startDate").setValue(startDate);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    finish();
                    break;
            }
        }
    };
}