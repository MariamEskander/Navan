package com.pharmacy.pharmacycare.ui.notifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.model.DataViewModel;
import com.pharmacy.pharmacycare.ui.read_more.GoToDetails;
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

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    protected ArrayList<String> mItems;
    protected LayoutInflater mInflater;
    private Context mContext;

    public NotificationsAdapter(Context context, ArrayList<String> data) {
        mInflater = LayoutInflater.from(context);
        this.mItems = data;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = mInflater.inflate(R.layout.notification_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    public void setItems(ArrayList<String> items) {
        mItems = items;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final String dataViewModel = mItems.get(position);
        viewHolder.setData(dataViewModel, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void add(String item) {
        mItems.add(item);
      /// notifyItemInserted(mItems.size() - 1);
    }

    public void addAll(ArrayList<String> arrayList) {
        for (String dataViewModel : arrayList) {
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

        @BindView(R.id.txt)
        TextView txt;

        int mPosition;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setData(String data, final int position) {
            this.mPosition = position;
            setUpText(this.txt, data);

        }
    }
}
