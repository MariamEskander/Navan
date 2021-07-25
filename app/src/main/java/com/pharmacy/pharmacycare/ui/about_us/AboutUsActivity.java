package com.pharmacy.pharmacycare.ui.about_us;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pharmacy.pharmacycare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.day1)
    TextView day1;

    @BindView(R.id.day1_hours)
    TextView day1_hours;


    @BindView(R.id.day2)
    TextView day2;

    @BindView(R.id.day2_hours)
    TextView day2_hours;


    @BindView(R.id.day3)
    TextView day3;

    @BindView(R.id.day3_hours)
    TextView day3_hours;


    @BindView(R.id.day4)
    TextView day4;

    @BindView(R.id.day4_hours)
    TextView day4_hours;


    @BindView(R.id.day5)
    TextView day5;

    @BindView(R.id.day5_hours)
    TextView day5_hours;


    @BindView(R.id.day6)
    TextView day6;

    @BindView(R.id.day6_hours)
    TextView day6_hours;


    @BindView(R.id.day7)
    TextView day7;

    @BindView(R.id.day7_hours)
    TextView day7_hours;


    @BindView(R.id.call)
    RelativeLayout call;

    @BindView(R.id.location)
    RelativeLayout location;

    @BindView(R.id.opening_hours)
    RelativeLayout opening_hours;

    @BindView(R.id.bottom_sheet)
    RelativeLayout bottom_sheet;

    private String phoneToCall;
    private BottomSheetBehavior mBottomSheetBehavior;


    private ArrayList<String> daysOfWeek = new ArrayList<>();
    private ArrayList<String> hoursOfWeek = new ArrayList<>();


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);//Important to add
        initToolbar();
        handleClick();
        setDaysTimes();

    }

    private void setDaysTimes() {
        daysOfWeek.add(0, "Saturday");
        daysOfWeek.add(1, "Sunday");
        daysOfWeek.add(2, "Monday");
        daysOfWeek.add(3, "Tuesday");
        daysOfWeek.add(4, "Wednesday");
        daysOfWeek.add(5, "Thursday");
        daysOfWeek.add(6, "Friday");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        daysOfWeek = setDaysNames(dateFormatter.format(date));
        day1.setText(daysOfWeek.get(0));
        day1_hours.setText(hoursOfWeek.get(0));

        day2.setText(daysOfWeek.get(1));
        day2_hours.setText(hoursOfWeek.get(1));

        day3.setText(daysOfWeek.get(2));
        day3_hours.setText(hoursOfWeek.get(2));

        day4.setText(daysOfWeek.get(3));
        day4_hours.setText(hoursOfWeek.get(3));

        day5.setText(daysOfWeek.get(4));
        day5_hours.setText(hoursOfWeek.get(4));

        day6.setText(daysOfWeek.get(5));
        day6_hours.setText(hoursOfWeek.get(5));

        day7.setText(daysOfWeek.get(6));
        day7_hours.setText(hoursOfWeek.get(6));
    }

    private ArrayList<String> setDaysNames(String startDay) {
        ArrayList<String> todaysList = new ArrayList<>();
        int indx = daysOfWeek.indexOf(startDay);
        for (int i = indx; i < 7; i++) {
            todaysList.add(daysOfWeek.get(i));
            if (daysOfWeek.get(i).equals("Saturday"))
                hoursOfWeek.add("10:00 AM - 2:00 PM");
            else  if (daysOfWeek.get(i).equals("Sunday"))
            hoursOfWeek.add("Closed");
            else
                hoursOfWeek.add("9:00 AM - 8:00 PM");

        }
        for (int j = 0; j < indx; j++) {
            todaysList.add(daysOfWeek.get(j));
            if (daysOfWeek.get(j).equals("Saturday"))
                hoursOfWeek.add("10:00 AM - 2:00 PM");
            else  if (daysOfWeek.get(j).equals("Sunday"))
                hoursOfWeek.add("Closed");
            else
                hoursOfWeek.add("9:00 AM - 8:00 PM");
        }
        return todaysList;
    }


    private void handleClick() {
        opening_hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    bottom_sheet.setVisibility(View.VISIBLE);
                else bottom_sheet.setVisibility(View.GONE);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        phoneToCall = "613 518 7444";
                                        callNumber(phoneToCall);
                                    }
                                }
        );

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir//Navan+Medical+Pharmacy+(Navan+Pharmacy)/" +
                                "@45.4277892,-75.5745437,12z/data=!4m8!4m7!1m0!1m5!1m1!1s0x4cce0d8e40ac76c3:0xb650a8dd561d2a8c!2m2!1d-75.5045039!2d45.4278106"));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void callNumber(String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        if (isPermissionGranted()) {
            startActivity(callIntent);
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callNumber(phoneToCall);
                }
                return;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
