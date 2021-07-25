package com.pharmacy.pharmacycare.ui.refill.refill_action;

import android.content.Context;
import android.database.DatabaseUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.data.DataBaseUtil;
import com.pharmacy.pharmacycare.data.PharmacyDbHelper;
import com.pharmacy.pharmacycare.model.RefillModel;
import com.pharmacy.pharmacycare.ui.refill.RefillActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefillActionActivity extends AppCompatActivity {

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

   @BindView(R.id.title)
    TextView title;

//    @BindView(R.id.spinner)
//    Spinner spinner;

    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.tet_refill_name)
    EditText tet_refill_name;

    @BindView(R.id.continue_no_save)
    TextView continue_no_save;


    private int alarm = 0;
    private RefillModel refillModel;

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_action);
        ButterKnife.bind(this);


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setupParent(v);
                return true;
            }
        });

        String refill = getIntent().getStringExtra("data");
        refillModel = new Gson().fromJson(refill,RefillModel.class);

        initToolbar();
     //   addItemsOnSpinner();
       // addListenerOnSpinnerItemSelection();
        handleClicks();


    }

    private void handleClicks() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tet_refill_name.getText().toString().trim().equals("")){
                    tet_refill_name.setBackground(ContextCompat.getDrawable(RefillActionActivity.this, R.drawable.edit_txt_error_bg));
                }else {
                    tet_refill_name.setBackground(ContextCompat.getDrawable(RefillActionActivity.this, R.drawable.edit_txt_bg));
                    PharmacyDbHelper dbHelper = PharmacyDbHelper.GetFor(RefillActionActivity.this);
                    DataBaseUtil.insertRefill(dbHelper, refillModel);
                    finish();
                }
            }
        });

        continue_no_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addListenerOnSpinnerItemSelection() {
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                alarm = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    public void addItemsOnSpinner() {
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.alarm_remainder));
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(dataAdapter);
    }


    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title.setText(getString(R.string.refill));
    }

    public void setupParent(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    return false;
                }
            });
        }
        //If a layout container, iterate over children
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView);
            }
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


//    public void clickToggleButtonRTC(View view) {
//        boolean isEnabled = ((ToggleButton)view).isEnabled();
//
//        if (isEnabled) {
//            NotificationHelper.scheduleRepeatingRTCNotification(mContext, hours.getText().toString(), minutes.getText().toString());
//            NotificationHelper.enableBootReceiver(mContext);
//        } else {
//            NotificationHelper.cancelAlarmRTC();
//            NotificationHelper.disableBootReceiver(mContext);
//        }
//    }
//
//    public void clickToggleButtonElapsed(View view) {
//        boolean isEnabled = ((ToggleButton)view).isEnabled();
//
//        if (isEnabled) {
//            NotificationHelper.scheduleRepeatingElapsedNotification(mContext);
//            NotificationHelper.enableBootReceiver(mContext);
//        } else {
//            NotificationHelper.cancelAlarmElapsed();
//            NotificationHelper.disableBootReceiver(mContext);
//        }
//    }
//
//    public void cancelAlarms(View view) {
//        NotificationHelper.cancelAlarmRTC();
//        NotificationHelper.cancelAlarmElapsed();
//        NotificationHelper.disableBootReceiver(mContext);
//    }

}
