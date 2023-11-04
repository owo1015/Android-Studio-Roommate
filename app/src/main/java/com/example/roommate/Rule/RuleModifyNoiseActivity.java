package com.example.roommate.Rule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.example.roommate.R;
import com.example.roommate.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class RuleModifyNoiseActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_modify_noise);

        CheckBox checkBox1 = findViewById(R.id.rule1);
        CheckBox checkBox2 = findViewById(R.id.rule2);
        CheckBox checkBox3 = findViewById(R.id.rule3);
        CheckBox checkBox4 = findViewById(R.id.rule4);
        CheckBox checkBox5 = findViewById(R.id.rule5);

        EditText editText1 = findViewById(R.id.edt1);

        ToggleButton toggleButton2 = findViewById(R.id.toggleButton2);
        ToggleButton toggleButton3 = findViewById(R.id.toggleButton3);
        ToggleButton toggleButton4 = findViewById(R.id.toggleButton4);
        ToggleButton toggleButton5 = findViewById(R.id.toggleButton5);

        // 뒤로 가기
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 저장된 규칙 표시
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

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
                                for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                    Rule rule = snapshot2.getValue(Rule.class);
                                    if(rule.getType().equals("소음")) {
                                        switch (rule.getRule()) {
                                            case "소음 허용 시간":
                                                checkBox1.setChecked(true);
                                                editText1.setText(rule.getRuleDetail());
                                                break;
                                            case "취침 시 소음":
                                                checkBox2.setChecked(true);
                                                if(rule.getRuleDetail().equals(toggleButton2.getTextOn()))
                                                    toggleButton2.setChecked(true);
                                                break;
                                            case "컨텐츠 시청":
                                                checkBox3.setChecked(true);
                                                if(rule.getRuleDetail().equals(toggleButton3.getTextOn()))
                                                    toggleButton3.setChecked(true);
                                                break;
                                            case "통화 장소":
                                                checkBox4.setChecked(true);
                                                if(rule.getRuleDetail().equals(toggleButton4.getTextOn()))
                                                    toggleButton4.setChecked(true);
                                                break;
                                            case "드라이기 사용 장소":
                                                checkBox5.setChecked(true);
                                                if(rule.getRuleDetail().equals(toggleButton5.getTextOn()))
                                                    toggleButton5.setChecked(true);
                                                break;
                                        }
                                    }
                                }
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

        // 규칙 저장
        Button rule_save = findViewById(R.id.rule_save);
        rule_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 데이터 저장
                databaseReference = database.getReference("user");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User userData = snapshot.getValue(User.class);

                            if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                DatabaseReference rootRef = database.getReference();
                                DatabaseReference dataRef = rootRef.child("rule");

                                // 기존 데이터 삭제
                                Query query = dataRef.orderByChild("roomCode").equalTo(userData.getRoomCode());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                        for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                            Rule rule = snapshot2.getValue(Rule.class);
                                            if (rule.getType().equals("소음"))
                                                snapshot2.getRef().removeValue();
                                        }

                                        // 새로운 데이터 추가
                                        if(checkBox1.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox1.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText1.getText().toString());
                                            itemRef.child("type").setValue("소음");
                                        }
                                        if(checkBox2.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox2.getText().toString());
                                            itemRef.child("ruleDetail").setValue(toggleButton2.getText().toString());
                                            itemRef.child("type").setValue("소음");
                                        }
                                        if(checkBox3.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox3.getText().toString());
                                            itemRef.child("ruleDetail").setValue(toggleButton3.getText().toString());
                                            itemRef.child("type").setValue("소음");
                                        }
                                        if(checkBox4.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox4.getText().toString());
                                            itemRef.child("ruleDetail").setValue(toggleButton4.getText().toString());
                                            itemRef.child("type").setValue("소음");
                                        }
                                        if(checkBox5.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox5.getText().toString());
                                            itemRef.child("ruleDetail").setValue(toggleButton5.getText().toString());
                                            itemRef.child("type").setValue("소음");
                                        }

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
                    }
                });

                finish();
            }
        });
    }
}