package com.example.roommate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.roommate.Calendar.CalendarFragment;
import com.example.roommate.Rule.RuleFragment;
import com.example.roommate.Setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import com.example.roommate.Album.AlbumFragment;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    BottomNavigationView navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //layout 아래 있는 activity_main을 view로 연결한다-실행시 제일 먼저 뜨는 화면

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startLoginActivity();

        }
        navigation_view = findViewById(R.id.navigation_view);

        // 홈 화면 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new HomeFragment()).commit();

        // 하단 내비게이션 바 터치 시 화면 전환
        navigation_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                        break;
                    case R.id.menu_rule:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RuleFragment()).commit();
                        break;
                    case R.id.menu_calendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
                        break;
                    case R.id.menu_album:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlbumFragment()).commit();
                        break;

                    case R.id.menu_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }
    private void startLoginActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}