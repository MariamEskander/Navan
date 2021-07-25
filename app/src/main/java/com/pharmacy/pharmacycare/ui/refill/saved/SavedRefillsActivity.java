package com.pharmacy.pharmacycare.ui.refill.saved;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.data.DataBaseUtil;
import com.pharmacy.pharmacycare.data.PharmacyDbHelper;
import com.pharmacy.pharmacycare.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRefillsActivity extends AppCompatActivity {

    @BindView(R.id.txt)
    TextView txt;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_refills);
        ButterKnife.bind(this);

        initToolbar();

        PharmacyDbHelper dbHelper = PharmacyDbHelper.GetFor(SavedRefillsActivity.this);
        txt.setText("Saved Refills:  "+(DataBaseUtil.getRefills(dbHelper) == null ? 0 :
                DataBaseUtil.getRefills(dbHelper).size() ));
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
}
