package com.example.roommate.dday;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roommate.R;
import com.google.firebase.auth.FirebaseAuth;

public class DdayDialog2 extends Dialog implements View.OnClickListener {

    private Context context;
    private String email, name, roomCode;
    private String contents1, contents2;

    private EditText dday_title;
    private EditText dday_date;
    private TextView dday_uploader;

    private Button delete;
    private Button save;

    private DialogListener dialogListener;

    public DdayDialog2(Context context, String email, String name, String roomCode, String contents1, String contents2) {
        super(context);
        this.context = context;
        this.email = email;
        this.name = name;
        this.roomCode = roomCode;
        this.contents1 = contents1;
        this.contents2 = contents2;
    }

    public interface DialogListener {
        public void onPositiveClicked(String email, String name, String roomCode, String title, String date);
        public void onNegativeClicked();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dday_dialog2);

        dday_title = (EditText) findViewById(R.id.edt_dday_title);
        dday_date = (EditText) findViewById(R.id.edt_dday_date);
        dday_uploader = (TextView) findViewById(R.id.txt_dday_uploader);

        delete = findViewById(R.id.btn_delete);
        save = findViewById(R.id.btn_save);

        dday_title.setText(contents1);
        dday_date.setText(contents2);
        dday_uploader.setText(name);

        delete.setOnClickListener(this);
        save.setOnClickListener(this);

        // 작성자인 사용자에게만 삭제, 저장 버튼 활성화
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email)) {
            delete.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            dday_uploader.setVisibility(View.INVISIBLE);
        }
        else {
            dday_title.setEnabled(false);
            dday_date.setEnabled(false);
            delete.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
            dday_uploader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        String title = dday_title.getText().toString();
        String date = dday_date.getText().toString();

        switch(v.getId()) {
            case R.id.btn_delete:
                dialogListener.onNegativeClicked();
                dismiss();
                break;
            case R.id.btn_save:
                dialogListener.onPositiveClicked(email, name, roomCode, title, date);
                dismiss();
                break;
        }
    }
}