package com.example.roommate.Rule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
import com.example.roommate.dday.Dday;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RuleModifyCleaningActivity extends AppCompatActivity {

    String[] items1 = {"1주일", "2주일", "3주일", "한 달", "두 달"};
    String[] items2 = {"식전", "식후"};
    String[] items3 = {"같이", "각자"};

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_modify_cleaning);

        CheckBox checkBox1 = findViewById(R.id.rule1);
        CheckBox checkBox2 = findViewById(R.id.rule2);
        CheckBox checkBox3 = findViewById(R.id.rule3);
        CheckBox checkBox4 = findViewById(R.id.rule4);
        CheckBox checkBox5 = findViewById(R.id.rule5);
        CheckBox checkBox6 = findViewById(R.id.rule6);
        CheckBox checkBox7 = findViewById(R.id.rule7);
        CheckBox checkBox8 = findViewById(R.id.rule8);
        CheckBox checkBox9 = findViewById(R.id.rule9);
        CheckBox checkBox10 = findViewById(R.id.rule10);
        CheckBox checkBox11 = findViewById(R.id.rule11);

        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner6 = findViewById(R.id.spinner6);
        Spinner spinner7 = findViewById(R.id.spinner7);
        Spinner spinner9 = findViewById(R.id.spinner9);
        Spinner spinner10 = findViewById(R.id.spinner10);

        EditText editText2 = findViewById(R.id.edt2);
        EditText editText3 = findViewById(R.id.edt3);
        EditText editText4 = findViewById(R.id.edt4);
        EditText editText5 = findViewById(R.id.edt5);
        EditText editText8 = findViewById(R.id.edt8);
        EditText editText11 = findViewById(R.id.edt11);

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

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items2
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items3
        );
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // 배열을 스피너에 표시
        spinner1.setAdapter(adapter1);
        spinner6.setAdapter(adapter2);
        spinner7.setAdapter(adapter1);
        spinner9.setAdapter(adapter3);
        spinner10.setAdapter(adapter1);

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
                                    if(rule.getType().equals("청소")) {
                                        switch (rule.getRule()) {
                                            case "대청소 주기":
                                                checkBox1.setChecked(true);
                                                spinner1.setSelection(Arrays.binarySearch(items1, rule.getRuleDetail()));
                                                break;
                                            case "청소기 당번":
                                                checkBox2.setChecked(true);
                                                editText2.setText(rule.getRuleDetail());
                                                break;
                                            case "물걸레 당번":
                                                checkBox3.setChecked(true);
                                                editText3.setText(rule.getRuleDetail());
                                                break;
                                            case "분리수거 당번":
                                                checkBox4.setChecked(true);
                                                editText4.setText(rule.getRuleDetail());
                                                break;
                                            case "설거지 당번":
                                                checkBox5.setChecked(true);
                                                editText5.setText(rule.getRuleDetail());
                                                break;
                                            case "설거지 선호 유형":
                                                checkBox6.setChecked(true);
                                                spinner6.setSelection(Arrays.binarySearch(items2, rule.getRuleDetail()));
                                                break;
                                            case "빨래 주기":
                                                checkBox7.setChecked(true);
                                                spinner7.setSelection(Arrays.binarySearch(items1, rule.getRuleDetail()));
                                                break;
                                            case "빨래 당번":
                                                checkBox8.setChecked(true);
                                                editText8.setText(rule.getRuleDetail());
                                                break;
                                            case "빨래 선호 유형":
                                                checkBox9.setChecked(true);
                                                spinner9.setSelection(Arrays.binarySearch(items3, rule.getRuleDetail()));
                                                break;
                                            case "화장실 청소 주기":
                                                checkBox10.setChecked(true);
                                                spinner10.setSelection(Arrays.binarySearch(items1, rule.getRuleDetail()));
                                                break;
                                            case "화장실 청소 당번":
                                                checkBox11.setChecked(true);
                                                editText11.setText(rule.getRuleDetail());
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

                            if(userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                DatabaseReference rootRef = database.getReference();
                                DatabaseReference dataRef = rootRef.child("rule");

                                // 기존 데이터 삭제
                                Query query = dataRef.orderByChild("roomCode").equalTo(userData.getRoomCode());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                        for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                            Rule rule = snapshot2.getValue(Rule.class);
                                            if (rule.getType().equals("청소"))
                                                snapshot2.getRef().removeValue();
                                        }

                                        // 새로운 데이터 추가
                                        if(checkBox1.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox1.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner1.getSelectedItem().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox2.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox2.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText2.getText().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox3.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox3.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText3.getText().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox4.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox4.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText4.getText().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox5.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox5.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText5.getText().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox6.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox6.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner6.getSelectedItem().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox7.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox7.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner7.getSelectedItem().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox8.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox8.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText8.getText().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox9.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox9.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner9.getSelectedItem().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox10.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox10.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner10.getSelectedItem().toString());
                                            itemRef.child("type").setValue("청소");
                                        }
                                        if(checkBox11.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox11.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText11.getText().toString());
                                            itemRef.child("type").setValue("청소");
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