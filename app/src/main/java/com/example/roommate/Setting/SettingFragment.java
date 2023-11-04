package com.example.roommate.Setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.roommate.LoginActivity;
import com.example.roommate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {

    private FirebaseAuth mAuth;
    BottomNavigationView navigation_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        Button setting_user = v.findViewById(R.id.btn_setting_user);
        Button setting_roommate = v.findViewById(R.id.btn_setting_roommate);
        Button setting_sound = v.findViewById(R.id.btn_setting_sound);
        Button setting_notice = v.findViewById(R.id.btn_setting_notice);
        Button setting_logout = v.findViewById(R.id.btn_setting_logout);

        // 사용자 정보 버튼
        setting_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingInfoActivity.class);
                startActivity(intent);
            }
        });

        // 룸메이트 정보 버튼
        setting_roommate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingRoommateActivity.class);
                startActivity(intent);
            }
        });

        // 소리 및 진동 설정 버튼
        setting_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingSoundActivity.class);
                startActivity(intent);
            }
        });

        // 알림 설정 버튼
        setting_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingNoticeActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼
        setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}