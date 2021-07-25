package com.pharmacy.pharmacycare.ui.refill.add;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.model.DataViewModel;
import com.pharmacy.pharmacycare.model.RxModelView;
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

public class RxAdapter extends RecyclerView.Adapter<RxAdapter.ViewHolder> {

    protected ArrayList<RxModelView> mItems;
    protected LayoutInflater mInflater;
    private Context mContext;
    private ScanItem scanItem;

    public RxAdapter(Context context, ArrayList<RxModelView> data) {
        mInflater = LayoutInflater.from(context);
        this.mItems = data;
        this.mContext = context;

        try {
            scanItem = (ScanItem) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString()
                    + " must implement ScanItem");
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = mInflater.inflate(R.layout.rx_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    public void setItems(ArrayList<RxModelView> items) {
        mItems = items;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final RxModelView dataViewModel = mItems.get(position);
        viewHolder.setData(dataViewModel, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void add(RxModelView item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<RxModelView> arrayList) {
        for (RxModelView dataViewModel : arrayList) {
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

    public void resetItem(int position, String contents) {
        this.mItems.get(position).setRx(contents);
        notifyDataSetChanged();
    }

    public ArrayList<RxModelView> getItems() {
        return this.mItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.p_number)
        TextView p_number;

        @BindView(R.id.rx_number)
        EditText rx_number;

        @BindView(R.id.scan)
        LinearLayout scan;

        @BindView(R.id.medication_name)
        EditText medication_name;


        int mPosition;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setData(final RxModelView data, final int position) {
            this.mPosition = position;
            this.p_number.setText("Prescription #"+(position+2));
            if (data.getRx().equals("")){}
            else {
                rx_number.setText(data.getRx());
            }
            this.medication_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    data.setMedicationName(String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            this.rx_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    data.setRx(String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            this.scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanItem.scan(position);
                }
            });


        }
    }
}
