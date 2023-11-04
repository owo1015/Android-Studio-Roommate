package com.example.roommate.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roommate.R;
import com.google.firebase.auth.FirebaseAuth;

public class CalendarDialog2 extends Dialog implements View.OnClickListener {

    private Context context;
    private String email, name, roomCode;
    private String contents1, contents2, contents3, contents4;

    private EditText schedule_title;
    private EditText schedule_date;
    private EditText schedule_time;
    private EditText schedule_memo;
    private TextView schedule_uploader;

    private Button delete;
    private Button save;

    private DialogListener dialogListener;

    public CalendarDialog2(Context context, String email, String name, String roomCode, String contents1, String contents2, String contents3, String contents4) {
        super(context);
        this.context = context;
        this.email = email;
        this.name = name;
        this.roomCode = roomCode;
        this.contents1 = contents1;
        this.contents2 = contents2;
        this.contents3 = contents3;
        this.contents4 = contents4;
    }

    public interface DialogListener {
        public void onPositiveClicked(String email, String name, String roomCode, String title, String date, String time, String memo);
        public void onNegativeClicked();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_dialog2);

        schedule_title = (EditText) findViewById(R.id.edt_title);
        schedule_date = (EditText) findViewById(R.id.edt_date);
        schedule_time = (EditText) findViewById(R.id.edt_time);
        schedule_memo = (EditText) findViewById(R.id.edt_memo);
        schedule_uploader = (TextView) findViewById(R.id.txt_schedule_uploader);

        delete = findViewById(R.id.btn_delete);
        save = findViewById(R.id.btn_save);

        schedule_title.setText(contents1);
        schedule_date.setText(contents2);
        schedule_time.setText(contents3);
        schedule_memo.setText(contents4);
        schedule_uploader.setText(name);

        delete.setOnClickListener(this);
        save.setOnClickListener(this);

        // 작성자인 사용자에게만 삭제, 저장 버튼 활성화
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email)) {
            delete.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            schedule_uploader.setVisibility(View.INVISIBLE);
        }
        else {
            schedule_title.setEnabled(false);
            schedule_date.setEnabled(false);
            schedule_time.setEnabled(false);
            schedule_memo.setEnabled(false);
            delete.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
            schedule_uploader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        String title = schedule_title.getText().toString();
        String date = schedule_date.getText().toString();
        String time = schedule_time.getText().toString();
        String memo = schedule_memo.getText().toString();

        switch(v.getId()) {
            case R.id.btn_delete:
                dialogListener.onNegativeClicked();
                dismiss();
                break;
            case R.id.btn_save:
                dialogListener.onPositiveClicked(email, name, roomCode, title, date, time, memo);
                dismiss();
                break;
        }
    }
}
