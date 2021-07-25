package com.pharmacy.pharmacycare.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.model.DataViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mariam on 3/24/2018.
 */

public class FeaturesAdapter extends LinearLayout {

    protected DataViewModel dataViewModel;
    protected LayoutInflater mInflater;
    private GoToFeaturesDetails goToDetails;
    private Context mContext;
    private int mPosition;

    @BindView(R.id.main_image)
    RoundedImageView main_image;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.part_of_content)
    TextView item_details;

    @BindView(R.id.read_article)
    TextView read_article;

    public FeaturesAdapter(Context c, DataViewModel m,int position) {
        super(c);

        mContext = c;
        dataViewModel = m;
        mPosition = position;

        try {
            goToDetails = (GoToFeaturesDetails) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString()
                    + " must implement goToDetails");
        }

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.news_card_list_item, this, true);

        ButterKnife.bind(this);
        setData();
    }

    private void setUpText(TextView txt, String s) {
        txt.setText(s);
    }

    void setData() {

        setUpText(this.title, dataViewModel.getTitle());
        setUpText(this.item_details, dataViewModel.getFieldSubTitle());

        if (dataViewModel.getFieldPhoto() != null && !dataViewModel.getFieldPhoto().isEmpty()) {
            Picasso.with(mContext).load(dataViewModel.getFieldPhoto()).fit().
                    placeholder(R.drawable.navanpharmacylogo).into(this.main_image);
        } else
            this.main_image.setImageResource(R.drawable.navanpharmacylogo);

        this.read_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetails.goToFeaturesDetails(mPosition);
            }
        });


    }

}



