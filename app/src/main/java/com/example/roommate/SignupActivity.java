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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private String email;
    private String password;
    private String passwordCheck;
    private String roomCode;
    private String startDate;
    private String name;
    private String phone;

    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_up);  //layout 아래 있는 activity_main을 view로 연결한다-실행시 제일 먼저 뜨는 화면
        findViewById(R.id.signupButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           // reload();
        }
    }

    View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.signupButton:
                    Log.e("클릭", "클릭");
                    signup();
                    break;

            }

        }
    };

    private void signup(){
        email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        passwordCheck = ((EditText) findViewById(R.id.passwordCheckEditText)).getText().toString();
        roomCode = ((EditText) findViewById(R.id.roomCodeEditText)).getText().toString();
        startDate = ((EditText) findViewById(R.id.startDateEditText)).getText().toString();
        name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if(roomCode.length() > 0 && startDate.length() > 0 && name.length() > 0 && phone.length() > 0) {
                if (password.equals(passwordCheck)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        /*updateUI(user);*/
                                        startToast("회원가입 성공");
                                        userSave();
                                        startLoginActivity();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        //실패했을 때
                                        if (task.getException() != null) {
                                            startToast(task.getException().toString());
                                        }
                                    }
                                }
                            });
                } else {
                    startToast("비밀번호가 일치하지 않습니다.");
                }
            } else {
                startToast("정보를 모두 입력해주세요.");
            }
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void startLoginActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void userSave(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();

        DatabaseReference dataRef=rootRef.child("user");
        DatabaseReference itemRef=dataRef.push();
        itemRef.child("email").setValue(email);
        itemRef.child("roomCode").setValue(roomCode);
        itemRef.child("startDate").setValue(startDate);
        itemRef.child("name").setValue(name);
        itemRef.child("phone").setValue(phone);
    }
}
