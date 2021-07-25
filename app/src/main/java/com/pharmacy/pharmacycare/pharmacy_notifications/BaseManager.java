package com.pharmacy.pharmacycare.pharmacy_notifications;

import com.pharmacy.pharmacycare.model.Result;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by Mariam on 8/15/2017.
 */

abstract class BaseManager extends Observable {

   void updateObservers(int event) {
       updateObservers(event, null);
   }

   void updateObservers(int event, Object data) {
       setChanged();

       Result result = new Result(event);
       if (data != null) {
           result = new Result(event, data);
       }
       notifyObservers(result);
   }

   public void register(Observer observer) {
       addObserver(observer);
   }

   public void unregister(Observer observer) {
       deleteObserver(observer);
   }



}
