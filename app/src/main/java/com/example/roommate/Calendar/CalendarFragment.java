package com.example.roommate.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.roommate.R;
import com.example.roommate.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CalendarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    private TextView txtHeader;
    private GridView gridView;
    private ArrayList<DayInfo> dayList;
    private HashMap<String, String> scheduleList;
    private CalendarAdapter calendarAdapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    private DayInfo day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        ImageButton back = (ImageButton) v.findViewById(R.id.calendar_back);
        ImageButton forward = (ImageButton) v.findViewById(R.id.calendar_forward);

        txtHeader = (TextView) v.findViewById(R.id.calendar_header);
        gridView = (GridView) v.findViewById(R.id.calendar_gridview);

        back.setOnClickListener(this);
        forward.setOnClickListener(this);
        gridView.setOnItemClickListener(this);

        dayList = new ArrayList<DayInfo>();
        scheduleList = new HashMap<String, String>();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }

    private void getCalendar(Calendar calendar) {

        txtHeader.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        dayList.clear();

        int dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, -1);

        int lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);

        lastMonthStartDay -= (dayOfMonth - 1) - 1;

        for(int i = 0; i < dayOfMonth - 1; i++) {
            day = new DayInfo();
            day.setInMonth(false);
            dayList.add(day);
        }

        // 일정 데이터 저장
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("user"); // 데이터베이스 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    User userData = snapshot1.getValue(User.class);
                    if (currentUser != null) {
                        if (userData.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            DatabaseReference rootRef = database.getReference();
                            DatabaseReference dataRef = rootRef.child("schedule");

                            Query query = dataRef.orderByChild("roomCode").equalTo(userData.getRoomCode());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    scheduleList.clear();
                                    for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                        Schedule schedule = snapshot2.getValue(Schedule.class);
                                        scheduleList.put(schedule.getDate(), schedule.getTitle());
                                    }
                                    calendarAdapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 발생 시
            }
        });

        // 해당 월에 해당하는 날짜 정보 입력
        for(int i = 1; i <= lastDay; i++) {
            String getYear = String.valueOf(mThisMonthCalendar.get(Calendar.YEAR));
            String getMonth = String.valueOf((mThisMonthCalendar.get(Calendar.MONTH) + 1));
            String getDay = Integer.toString(i);
            String getDate = getYear + "/" + getMonth + "/" + getDay;

            day = new DayInfo();
            day.setDay(getDay);
            day.setDate(getDate);
            day.setInMonth(true);
            dayList.add(day);
        }

        for(int i = 1; i < 42 - (lastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setInMonth(false);
            dayList.add(day);
        }

        initCalendarAdapter();
    }

    private Calendar getLastMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        txtHeader.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        txtHeader.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        return calendar;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long l) {
        if(dayList.get(position).getDay() != null) {
            Intent intent = new Intent(getActivity(), CalendarScheduleActivity.class);
            intent.putExtra("year", String.valueOf(mThisMonthCalendar.get(Calendar.YEAR)));
            intent.putExtra("month", String.valueOf((mThisMonthCalendar.get(Calendar.MONTH) + 1)));
            intent.putExtra("day", String.valueOf(dayList.get(position).getDay()));
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.calendar_back:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            case R.id.calendar_forward:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }

    private void initCalendarAdapter() {
        calendarAdapter = new CalendarAdapter(getActivity(), R.layout.activity_calendar_item, dayList, scheduleList);
        gridView.setAdapter(calendarAdapter);
    }
}