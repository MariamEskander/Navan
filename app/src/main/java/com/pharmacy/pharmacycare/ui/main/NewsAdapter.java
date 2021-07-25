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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mariam on 3/24/2018.
 */

public class NewsAdapter extends LinearLayout{

    protected DataViewModel dataViewModel;
    protected LayoutInflater mInflater;
    private GoToNewsDetails goToDetails;
    private Context mContext;
    private int mPosition;

    @BindView(R.id.main_image)
    RoundedImageView main_image;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.part_of_content)
    TextView item_details;

    @BindView(R.id.date)
    TextView date;


    @BindView(R.id.read_article)
    TextView read_article;

    public NewsAdapter(Context c, DataViewModel m,int position) {
        super(c);

        mContext = c;
        dataViewModel = m;
        mPosition = position;

        try {
            goToDetails = (GoToNewsDetails) mContext;
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

            setUpText(this.date, dateFormat(dataViewModel.getCreated()));

            if (dataViewModel.getFieldPhoto() != null && !dataViewModel.getFieldPhoto().isEmpty()) {
                Picasso.with(mContext).load(dataViewModel.getFieldPhoto()).fit().
                        placeholder(R.drawable.navanpharmacylogo).into(this.main_image);
            } else
                this.main_image.setImageResource(R.drawable.navanpharmacylogo);


            read_article.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetails.goToNewsDetails(mPosition);
                }
            });


        }

        private String dateFormat(String input) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'");
            Date convertedDate;
            try {
                convertedDate = dateFormat.parse(input);
            } catch (ParseException e) {
                convertedDate = Calendar.getInstance().getTime();
            }
            SimpleDateFormat df = new SimpleDateFormat("MMMM MM, yyyy");
            return df.format(convertedDate);
        }

}
