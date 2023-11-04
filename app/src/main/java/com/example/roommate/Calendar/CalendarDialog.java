package com.example.roommate.Calendar;

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

public class CalendarDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String contents;

    private EditText schedule_title;
    private EditText schedule_date;
    private EditText schedule_time;
    private EditText schedule_memo;

    private Button cancel;
    private Button add;

    private DialogListener dialogListener;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public CalendarDialog(Context context, String contents) {
        super(context);
        this.context = context;
        this.contents = contents;
    }

    public interface DialogListener {
        public void onPositiveClicked(String email, String name, String roomCode, String title, String date, String time, String memo);
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_dialog);

        schedule_title = (EditText) findViewById(R.id.edt_title);
        schedule_date = (EditText) findViewById(R.id.edt_date);
        schedule_time = (EditText) findViewById(R.id.edt_time);
        schedule_memo = (EditText) findViewById(R.id.edt_memo);

        cancel = findViewById(R.id.btn_cancel);
        add = findViewById(R.id.btn_add);

        schedule_date.setText(contents);

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
                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                String name = userData.getName();
                                String roomCode = userData.getRoomCode();
                                String title = schedule_title.getText().toString();
                                String date = schedule_date.getText().toString();
                                String time = schedule_time.getText().toString();
                                String memo = schedule_memo.getText().toString();
                                dialogListener.onPositiveClicked(email, name, roomCode, title, date, time, memo);
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
