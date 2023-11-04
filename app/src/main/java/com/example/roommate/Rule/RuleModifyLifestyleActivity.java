package com.example.roommate.Rule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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

public class RuleModifyLifestyleActivity extends AppCompatActivity {

    String[] items1 = {"가능", "불가능", "사전 연락"};

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_modify_lifestyle);

        CheckBox checkBox1 = findViewById(R.id.rule1);
        CheckBox checkBox2 = findViewById(R.id.rule2);
        CheckBox checkBox3 = findViewById(R.id.rule3);
        CheckBox checkBox4 = findViewById(R.id.rule4);
        CheckBox checkBox5 = findViewById(R.id.rule5);
        CheckBox checkBox6 = findViewById(R.id.rule6);

        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner3 = findViewById(R.id.spinner3);
        Spinner spinner4 = findViewById(R.id.spinner4);
        Spinner spinner5 = findViewById(R.id.spinner5);

        EditText editText2 = findViewById(R.id.edt2);
        EditText editText6 = findViewById(R.id.edt6);

        // 뒤로 가기
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 스피너 배열 받아오기
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items1
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // 스피너에 표시
        spinner1.setAdapter(adapter1);
        spinner3.setAdapter(adapter1);
        spinner4.setAdapter(adapter1);
        spinner5.setAdapter(adapter1);

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
                                    if(rule.getType().equals("생활")) {
                                        switch (rule.getRule()) {
                                            case "친구 데려오기":
                                                checkBox1.setChecked(true);
                                                spinner1.setSelection(Arrays.binarySearch(items1, rule.getRuleDetail()));
                                                break;
                                            case "공유 가능 물품":
                                                checkBox2.setChecked(true);
                                                editText2.setText(rule.getRuleDetail());
                                                break;
                                            case "실내 취식":
                                                checkBox3.setChecked(true);
                                                spinner3.setSelection(Arrays.binarySearch(items1, rule.getRuleDetail()));
                                                break;
                                            case "실내 음주":
                                                checkBox4.setChecked(true);
                                                spinner4.setSelection(Arrays.binarySearch(items1, rule.getRuleDetail()));
                                                break;
                                            case "실내 흡연":
                                                checkBox5.setChecked(true);
                                                spinner5.setSelection(Arrays.binarySearch(items1, rule.getRuleDetail()));
                                                break;
                                            case "소등 시간":
                                                checkBox6.setChecked(true);
                                                editText6.setText(rule.getRuleDetail());
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
                                            if (rule.getType().equals("생활"))
                                                snapshot2.getRef().removeValue();
                                        }

                                        // 새로운 데이터 추가
                                        if (checkBox1.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox1.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner1.getSelectedItem().toString());
                                            itemRef.child("type").setValue("생활");
                                        }
                                        if (checkBox2.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox2.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText2.getText().toString());
                                            itemRef.child("type").setValue("생활");
                                        }
                                        if (checkBox3.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox3.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner3.getSelectedItem().toString());
                                            itemRef.child("type").setValue("생활");
                                        }
                                        if (checkBox4.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox4.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner4.getSelectedItem().toString());
                                            itemRef.child("type").setValue("생활");
                                        }
                                        if (checkBox5.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox5.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner5.getSelectedItem().toString());
                                            itemRef.child("type").setValue("생활");
                                        }
                                        if (checkBox6.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox6.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText6.getText().toString());
                                            itemRef.child("type").setValue("생활");
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