package com.pharmacy.pharmacycare.ui.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.NewsViewHolder> {

    protected ArrayList<DataViewModel> mItems;
    protected LayoutInflater mInflater;
    private GoToTopNewsDetails goToDetails;
    private Context mContext;


    public TopNewsAdapter(Context context, ArrayList<DataViewModel> data) {
        mInflater = LayoutInflater.from(context);
        this.mItems = data;
        this.mContext = context;

        try {
            goToDetails = (GoToTopNewsDetails) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString()
                    + " must implement goToDetails");
        }

    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = mInflater.inflate(R.layout.top_news_card_list_item, viewGroup, false);
        final NewsViewHolder holder = new NewsViewHolder(view);
        return holder;

    }

    public void setItems(ArrayList<DataViewModel> items) {
        mItems = items;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder viewHolder, int position) {
        final DataViewModel dataViewModel = mItems.get(position);
        viewHolder.setData(dataViewModel, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void add(DataViewModel item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }


    private void setUpText(TextView txt, String s) {
        txt.setText(s);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_image)
        RoundedImageView main_image;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.part_of_content)
        TextView item_details;

        @BindView(R.id.read_article)
        TextView read_article;

        int mPosition;

        NewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        void setData(DataViewModel data, final int position) {
            this.mPosition = position;
            setUpText(this.title, data.getTitle());
            setUpText(this.item_details, data.getFieldSubTitle());

            if (data.getFieldPhoto() != null && !data.getFieldPhoto().isEmpty()) {
                Picasso.with(mContext).load(data.getFieldPhoto()).fit().
                        placeholder(R.drawable.navanpharmacylogo).into(this.main_image);
            } else
                this.main_image.setImageResource(R.drawable.navanpharmacylogo);

            this.read_article.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetails.goToDetails(position);
                }
            });


        }

    }
}



