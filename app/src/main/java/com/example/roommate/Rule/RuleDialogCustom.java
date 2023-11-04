package com.example.roommate.Rule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.roommate.R;
import com.example.roommate.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;

public class RuleDialogCustom extends Dialog implements View.OnClickListener {

    private Context context;

    private EditText rule_content;
    private EditText rule_detail;

    private Button cancel;
    private Button add;

    private DialogListener dialogListener;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public RuleDialogCustom(Context context) {
        super(context);
        this.context = context;
    }

    public interface DialogListener {
        public void onPositiveClicked(String roomCode, String rule, String detail, String type);
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_dialog_custom);

        rule_content = (EditText) findViewById(R.id.edt_rule);
        rule_detail = (EditText) findViewById(R.id.edt_ruleDetail);

        cancel = findViewById(R.id.btn_cancel);
        add = findViewById(R.id.btn_add);

        cancel.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_add:
                // 사용자 데이터 읽어오기
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("user");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User userData = snapshot.getValue(User.class);
                            if(userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                String roomCode = userData.getRoomCode();
                                String rule = rule_content.getText().toString();
                                String detail = rule_detail.getText().toString();
                                String type = "사용자 지정";
                                dialogListener.onPositiveClicked(roomCode, rule, detail, type);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                dismiss();
                break;
        }
    }
}