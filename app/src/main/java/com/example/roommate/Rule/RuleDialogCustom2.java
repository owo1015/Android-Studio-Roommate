package com.example.roommate.Rule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.roommate.R;

public class RuleDialogCustom2 extends Dialog implements View.OnClickListener {

    private Context context;
    private String roomCode;
    private String contents1, contents2;

    private EditText rule_content;
    private EditText rule_detail;

    private Button delete;
    private Button save;

    private DialogListener dialogListener;

    public RuleDialogCustom2(Context context, String roomCode, String contents1, String contents2) {
        super(context);
        this.context = context;
        this.roomCode = roomCode;
        this.contents1 = contents1;
        this.contents2 = contents2;
    }

    public interface DialogListener {
        public void onPositiveClicked(String roomCode, String rule, String detail);
        public void onNegativeClicked();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_dialog_custom2);

        rule_content = (EditText) findViewById(R.id.edt_rule);
        rule_detail = (EditText) findViewById(R.id.edt_ruleDetail);

        delete = findViewById(R.id.btn_delete);
        save = findViewById(R.id.btn_save);

        rule_content.setText(contents1);
        rule_detail.setText(contents2);

        delete.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String rule = rule_content.getText().toString();
        String detail = rule_detail.getText().toString();

        switch(v.getId()) {
            case R.id.btn_delete:
                dialogListener.onNegativeClicked();
                dismiss();
                break;
            case R.id.btn_save:
                dialogListener.onPositiveClicked(roomCode, rule, detail);
                dismiss();
                break;
        }
    }
}