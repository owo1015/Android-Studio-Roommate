package com.example.roommate.Memo;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.roommate.R;
import com.example.roommate.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;

public class MemoDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private EditText memo_content;

    private Button cancel;
    private Button add;

    private DialogListener dialogListener;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public MemoDialog(Context context) {
        super(context);
        this.context = context;
    }

    public interface DialogListener {
        public void onPositiveClicked(String email, String name, String roomCode, String content, String date, String time);
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_dialog);

        memo_content = (EditText) findViewById(R.id.edt_content);

        cancel = findViewById(R.id.btn_cancel);
        add = findViewById(R.id.btn_add);

        cancel.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                String name = userData.getName();
                                String roomCode = userData.getRoomCode();
                                String content = memo_content.getText().toString();
                                LocalDateTime now = LocalDateTime.now();
                                String date = now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth();
                                String time = now.getHour() + ":" + now.getMinute();
                                dialogListener.onPositiveClicked(email, name, roomCode, content, date, time);
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