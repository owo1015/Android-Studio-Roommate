package com.example.roommate.Memo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roommate.R;
import com.google.firebase.auth.FirebaseAuth;

public class MemoDialog2 extends Dialog implements View.OnClickListener {

    private Context context;
    private String email, name, roomCode;
    private String contents;

    private EditText memo_content;
    private TextView memo_uploader;

    private Button delete;
    private Button save;

    private DialogListener dialogListener;

    public MemoDialog2(Context context, String email, String name, String roomCode, String contents) {
        super(context);
        this.context = context;
        this.email = email;
        this.name = name;
        this.roomCode = roomCode;
        this.contents = contents;
    }

    public interface DialogListener {
        public void onPositiveClicked(String email, String name, String roomCode, String content);
        public void onNegativeClicked();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_dialog2);

        memo_content = (EditText) findViewById(R.id.edt_content);
        memo_uploader = (TextView) findViewById(R.id.txt_memo_uploader);

        delete = findViewById(R.id.btn_delete);
        save = findViewById(R.id.btn_save);

        memo_content.setText(contents);
        memo_uploader.setText(name);

        delete.setOnClickListener(this);
        save.setOnClickListener(this);

        // 작성자인 사용자에게만 삭제, 저장 버튼 활성화
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email)) {
            delete.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            memo_uploader.setVisibility(View.INVISIBLE);
        }
        else {
            memo_content.setEnabled(false);
            delete.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
            memo_uploader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        String content = memo_content.getText().toString();

        switch(v.getId()) {
            case R.id.btn_delete:
                dialogListener.onNegativeClicked();
                dismiss();
                break;
            case R.id.btn_save:
                dialogListener.onPositiveClicked(email, name, roomCode, content);
                dismiss();
                break;
        }
    }
}