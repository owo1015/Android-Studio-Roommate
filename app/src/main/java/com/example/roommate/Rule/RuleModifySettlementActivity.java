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

public class RuleModifySettlementActivity extends AppCompatActivity {

    String[] items1 = {"매일", "월초", "월말", "연초", "연말"};

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_modify_settlement);

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

        EditText editText1 = findViewById(R.id.edt1);
        EditText editText2 = findViewById(R.id.edt2);
        EditText editText3 = findViewById(R.id.edt3);
        EditText editText4 = findViewById(R.id.edt4);
        EditText editText5 = findViewById(R.id.edt5);
        EditText editText6 = findViewById(R.id.edt6);
        EditText editText7 = findViewById(R.id.edt7);
        EditText editText8 = findViewById(R.id.edt8);
        EditText editText9 = findViewById(R.id.edt9);

        Spinner spinner10 = findViewById(R.id.spinner10);

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

        // 배열을 스피너에 표시
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
                                    if(rule.getType().equals("정산")) {
                                        switch (rule.getRule()) {
                                            case "보증금":
                                                checkBox1.setChecked(true);
                                                editText1.setText(rule.getRuleDetail());
                                                break;
                                            case "전세금":
                                                checkBox2.setChecked(true);
                                                editText2.setText(rule.getRuleDetail());
                                                break;
                                            case "월세":
                                                checkBox3.setChecked(true);
                                                editText3.setText(rule.getRuleDetail());
                                                break;
                                            case "식재료":
                                                checkBox4.setChecked(true);
                                                editText4.setText(rule.getRuleDetail());
                                                break;
                                            case "생필품":
                                                checkBox5.setChecked(true);
                                                editText5.setText(rule.getRuleDetail());
                                                break;
                                            case "관리비":
                                                checkBox6.setChecked(true);
                                                editText6.setText(rule.getRuleDetail());
                                                break;
                                            case "가스비":
                                                checkBox7.setChecked(true);
                                                editText7.setText(rule.getRuleDetail());
                                                break;
                                            case "전기세":
                                                checkBox8.setChecked(true);
                                                editText8.setText(rule.getRuleDetail());
                                                break;
                                            case "수도세":
                                                checkBox9.setChecked(true);
                                                editText9.setText(rule.getRuleDetail());
                                                break;
                                            case "정산 시기":
                                                checkBox10.setChecked(true);
                                                spinner10.setSelection(Arrays.binarySearch(items1, rule.getRuleDetail()));
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
                // 데이터 저장하기 전에 싹 다 삭제하고

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference rootRef = database.getReference();
                DatabaseReference dataRef = rootRef.child("rule");

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
                                            if (rule.getType().equals("정산"))
                                                snapshot2.getRef().removeValue();
                                        }

                                        // 새로운 데이터 추가
                                        if(checkBox1.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox1.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText1.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox2.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox2.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText2.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox3.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox3.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText3.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox4.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox4.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText4.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox5.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox5.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText5.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox6.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox6.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText6.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox7.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox7.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText7.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox8.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox8.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText8.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox9.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox9.getText().toString());
                                            itemRef.child("ruleDetail").setValue(editText9.getText().toString());
                                            itemRef.child("type").setValue("정산");
                                        }
                                        if(checkBox10.isChecked()) {
                                            DatabaseReference itemRef = dataRef.push();
                                            itemRef.child("roomCode").setValue(userData.getRoomCode());
                                            itemRef.child("rule").setValue(checkBox10.getText().toString());
                                            itemRef.child("ruleDetail").setValue(spinner10.getSelectedItem().toString());
                                            itemRef.child("type").setValue("정산");
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