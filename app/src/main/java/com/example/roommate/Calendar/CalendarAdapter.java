package com.example.roommate.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roommate.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private LayoutInflater inflater;

    private ArrayList<DayInfo> dayList;
    private HashMap<String, String> scheduleList;

    public CalendarAdapter(Context context, int Resource, ArrayList<DayInfo> dayList, HashMap<String, String> scheduleList) {
        this.context = context;
        this.dayList = dayList;
        this.scheduleList = scheduleList;
        this.resource = Resource;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayInfo day = dayList.get(position);
        day.setSchedule(scheduleList.get(day.getDate()));
        DayViewHolder dayViewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(resource, null);
            dayViewHolder = new DayViewHolder();
            dayViewHolder.txtDay = (TextView) convertView.findViewById(R.id.calendar_item_day);
            dayViewHolder.txtSchedule = (TextView) convertView.findViewById(R.id.schedule2);
            convertView.setTag(dayViewHolder);
        }
        else {
            dayViewHolder = (DayViewHolder) convertView.getTag();
        }

        if(day != null) {
            dayViewHolder.txtDay.setText(day.getDay());

            if(day.isInMonth()) {
                // 일정바 출력
                if(day.getSchedule() != null) {
                    dayViewHolder.txtSchedule.setText(day.getSchedule());
                    dayViewHolder.txtSchedule.setVisibility(View.VISIBLE);
                }
                else
                    dayViewHolder.txtSchedule.setVisibility(View.INVISIBLE);

                // 요일별 색상 변경
                if(position % 7 == 0) {
                    dayViewHolder.txtDay.setTextColor(Color.RED);
                }
                else if(position % 7 == 6) {
                    dayViewHolder.txtDay.setTextColor(Color.BLUE);
                }
                else {
                    dayViewHolder.txtDay.setTextColor(Color.BLACK);
                }
            }
            else {
                dayViewHolder.txtDay.setVisibility(View.INVISIBLE);
                dayViewHolder.txtSchedule.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    public class DayViewHolder {
        public TextView txtDay;
        public TextView txtSchedule;
    }
}