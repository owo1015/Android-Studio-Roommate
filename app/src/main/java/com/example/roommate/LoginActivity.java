package com.example.roommate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);  //layout 아래 있는 activity_main을 view로 연결한다-실행시 제일 먼저 뜨는 화면
        findViewById(R.id.btn_login).setOnClickListener(onClickListener);
        findViewById(R.id.btn_register).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // reload();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_login:
                    Log.e("클릭", "클릭");
                    login();
                    //gotoLogin();
                    break;
                case R.id.btn_register:
                    Log.e("클릭", "클릭");
                    gotoRegister();


            }

        }
    };

    private void login(){
        String email = ((EditText)findViewById(R.id.login_id)).getText().toString();
        String password=((EditText)findViewById(R.id.login_pwd)).getText().toString();

        if(email.length()>0 && password.length()>0){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공하였습니다.");
                                startMainActivity();
                            } else {
                                if(task.getException() != null){
                                    //startToast(task.getException().toString());
                                    startToast("이메일이나 비밀번호가 틀렸습니다.");
                                }


                            }
                        }

                        private void updateUI(FirebaseUser user) {
                        }
                    });

        }else{
            startToast("이메일과 비밀번호를 입력해주세요.");
        }

    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 뒤로가기 눌렀을 때 앱 종료
        startActivity(intent);
    }

    private void gotoRegister(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }



}