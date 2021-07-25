package com.pharmacy.pharmacycare.ui.refill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.ui.refill.add.AddRefillActivity;
import com.pharmacy.pharmacycare.ui.refill.saved.SavedRefillsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefillActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.add)
    ImageView add;

    @BindView(R.id.saved)
    ImageView saved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);
        ButterKnife.bind(this);
        initToolbar();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RefillActivity.this,AddRefillActivity.class));
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RefillActivity.this,SavedRefillsActivity.class));
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
        title.setText(getString(R.string.refill));
    }

}
