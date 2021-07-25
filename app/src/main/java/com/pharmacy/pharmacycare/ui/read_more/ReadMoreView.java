package com.pharmacy.pharmacycare.ui.read_more;

import com.pharmacy.pharmacycare.model.DataViewModel;
import com.pharmacy.pharmacycare.util.BaseView;

import java.util.ArrayList;

/**
 * Created by Mariam.Narouz on 1/22/2018.
 */

public interface ReadMoreView extends BaseView{
    void showPDialog();
    void hidePDialog();
    void hideSwipe();
    void resetView();
    void setData(ArrayList<DataViewModel> dataViewModels);
    void offset(int offset);
    void setLoadMore(boolean b);
    void setFirstOpen(boolean b);
    int getPage();

}
