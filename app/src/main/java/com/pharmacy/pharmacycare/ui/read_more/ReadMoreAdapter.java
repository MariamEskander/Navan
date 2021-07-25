package com.pharmacy.pharmacycare.ui.read_more;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.model.DataViewModel;
import com.pharmacy.pharmacycare.ui.main.GoToNewsDetails;
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

public class ReadMoreAdapter extends RecyclerView.Adapter<ReadMoreAdapter.ViewHolder> {

    protected ArrayList<DataViewModel> mItems;
    protected LayoutInflater mInflater;
    private GoToDetails goToDetails;
    private Context mContext;
    private String type;

    public ReadMoreAdapter(Context context, ArrayList<DataViewModel> data,String type) {
        mInflater = LayoutInflater.from(context);
        this.mItems = data;
        this.mContext = context;
        this.type = type;

        try {
            goToDetails = (GoToDetails) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString()
                    + " must implement goToDetails");
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = mInflater.inflate(R.layout.news_card_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    public void setItems(ArrayList<DataViewModel> items) {
        mItems = items;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
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

    public void addAll(ArrayList<DataViewModel> arrayList) {
        for (DataViewModel dataViewModel : arrayList) {
            add(dataViewModel);
        }
        notifyDataSetChanged();
    }

    void resetAdapter() {
        this.mItems = new ArrayList<>();

    }

    private void setUpText(TextView txt, String s) {
        txt.setText(s);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_image)
        RoundedImageView main_image;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.part_of_content)
        TextView item_details;

        @BindView(R.id.date)
        TextView date;


        int mPosition;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setData(DataViewModel data, final int position) {
            this.mPosition = position;
            setUpText(this.title, data.getTitle());
            setUpText(this.item_details, data.getFieldSubTitle());

            if (type.equals("news") || type.equals("topNews") )
            setUpText(this.date, dateFormat(data.getCreated()));
            else setUpText(this.date, "");

            if (data.getFieldPhoto() != null && !data.getFieldPhoto().isEmpty()) {
                Picasso.with(mContext).load(data.getFieldPhoto()).fit().
                        placeholder(R.drawable.navanpharmacylogo).into(this.main_image);
            } else
                this.main_image.setImageResource(R.drawable.navanpharmacylogo);


            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetails.goToDetails(position);
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
}
