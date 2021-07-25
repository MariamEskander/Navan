package com.pharmacy.pharmacycare.ui.refill.add;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.model.RefillModel;
import com.pharmacy.pharmacycare.model.RxModelView;
import com.pharmacy.pharmacycare.model.UserModel;
import com.pharmacy.pharmacycare.ui.refill.fill_rx.FillRxActivity;
import com.pharmacy.pharmacycare.ui.refill.refill_action.RefillActionActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRefillActivity extends AppCompatActivity implements AddRefillView,  ScanItem {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private static final int SEND_EMAIL = 2;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.tet_fname)
    EditText tet_fname;

    @BindView(R.id.tet_lname)
    EditText tet_lname;

    @BindView(R.id.tet_phone)
    EditText tet_phone;

//
//    @BindView(R.id.address)
//    TextView address;


    @BindView(R.id.continue_refill)
    TextView continue_refill;

    @BindView(R.id.rx_number)
    EditText rx_number;

    @BindView(R.id.scan)
    LinearLayout scan;

    @BindView(R.id.rx_scan)
    LinearLayout rx_scan;

    @BindView(R.id.medication_name)
    EditText medication_name;

    @BindView(R.id.rx_rv)
    RecyclerView rx_rv;

    @BindView(R.id.add_rx)
    TextView add_rx;

    @BindView(R.id.deliveryRb)
    RadioButton deliverRb;

    @BindView(R.id.pickupRb)
    RadioButton pickupRb;

    @BindView(R.id.notes)
    EditText notes;


    private AddRefillPresenter addRefillPresenter;

    private String place = "Pickup";
    private RxAdapter rxAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<RxModelView> rxModelViews;
    private ViewGroup.LayoutParams params;
    private float scale;
    private int position = -1;
    private RefillModel refillModel;
    private Dialog pd;

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_refill);
        ButterKnife.bind(this);
        initToolbar();

        pd =new Dialog(this, R.style.DialogCustomTheme);
        pd.setContentView(R.layout.view_progress);
        pd.setCancelable(false);

        addRefillPresenter = new AddRefillPresenterImpl(this);


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setupParent(v);
                return true;
            }
        });

        pickupRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if(b) place = "Pickup";
              else place =  "Delivery";
            }
        });

        deliverRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) place =  "Delivery";
                else place = "Pickup";
            }
        });


        rxAdapter = new RxAdapter(AddRefillActivity.this, new ArrayList<RxModelView>());
        rx_rv.setAdapter(rxAdapter);
        layoutManager = new LinearLayoutManager(AddRefillActivity.this); // (Context context, int spanCount)
        rx_rv.setLayoutManager(layoutManager);
        rx_rv.setItemAnimator(new DefaultItemAnimator());

        tet_phone.addTextChangedListener(new AutoAddTextWatcher(tet_phone,
                "-", 3, 6));

        handleClicks();


    }

    private void handleClicks() {

        continue_refill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRefillPresenter.continueRefill(tet_fname.getText().toString().trim(), tet_lname.getText().toString().trim()
                        , tet_phone.getText().toString().trim(), place, rx_number.getText().toString()
                        , medication_name.getText().toString(), rxAdapter.getItems(), notes.getText().toString());
            }
        });

//        pick_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                    .build(AddRefillActivity.this);
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    Toast.makeText(AddRefillActivity.this, "Not available, try again later", Toast.LENGTH_LONG).show();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    Toast.makeText(AddRefillActivity.this, "Not available, try again later", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        add_rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rxAdapter.getItemCount() == 0) {
                    rx_rv.setVisibility(View.VISIBLE);
                    scale = getResources().getDisplayMetrics().density;
                    params = rx_rv.getLayoutParams();
                    params.height = (int) (150 * scale);
                    rx_rv.setLayoutParams(params);
                    rxModelViews = new ArrayList<>();
                    rxModelViews.add(new RxModelView());
                    rxAdapter = new RxAdapter(AddRefillActivity.this, rxModelViews);
                    rx_rv.setAdapter(rxAdapter);

                } else {
                    params.height = rx_rv.getHeight() + ((int) (150 * scale));
                    rx_rv.setLayoutParams(params);
                    rxAdapter.add(new RxModelView());
                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    position = -1;
                    scanBar(-1);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                place = PlaceAutocomplete.getPlace(this, data);
//                address.setText(place.getAddress());
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                place = null;
//                address.setText("");
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                Toast.makeText(AddRefillActivity.this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
//            } else if (resultCode == RESULT_CANCELED) {
//                place = null;
//                address.setText("");
//            }
        } else if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                if (position == -1)
                    rx_number.setText(contents);
                else rxAdapter.resetItem(position, contents);
            }
        }else if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                Toast.makeText(AddRefillActivity.this, "Your Request has been Sent Successfully", Toast.LENGTH_LONG).show();
//                Intent intent =new Intent(TransferRxActivity.this, RefillActionActivity.class);
//                intent.putExtra("data",new Gson().toJson(refillModel));
//                startActivity(intent);
                finish();
            }else {
                Toast.makeText(AddRefillActivity.this, "Sending Cancelled!", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void setupParent(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    return false;
                }
            });
        }
        //If a layout container, iterate over children
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView);
            }
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title.setText(getString(R.string.fill_new));
    }


    private String formatDate(int year, int month, int dayOfMonth) {
        StringBuilder s = new StringBuilder();
        if (dayOfMonth < 10) s.append("0").append(dayOfMonth).append("/");
        else s.append(dayOfMonth).append("/");
        if (month < 10) s.append("0").append(month).append("/");
        else s.append(month).append("/");
        s.append(year);

        return s.toString();
    }

    @Override
    public void invalidFName() {
        tet_fname.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidLName() {
        tet_lname.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidPhone() {
        tet_phone.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.edit_txt_error_bg));
    }



//    @Override
//    public void invalidPlace() {
//        pick_up.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.button_error_bg));
//    }

    @Override
    public void invalidRx() {
        rx_scan.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void validFName() {
        tet_fname.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validLName() {
        tet_lname.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validPhone() {
        tet_phone.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.edit_txt_bg));
    }


//    @Override
//    public void validPlace() {
//        pick_up.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.pick_up_bg));
//    }

    @Override
    public void validRx() {
        rx_scan.setBackground(ContextCompat.getDrawable(AddRefillActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void sendRefill(final String fName, final String lName, final String phone, final String place,
                           ArrayList<RxModelView> rxModelViews, final String notes) {
        pd.show();

        refillModel = new RefillModel("",new UserModel(-1,fName,lName,phone,""),
                rxModelViews);
        final StringBuilder medications= new StringBuilder();
        for (int i = 0 ; i<rxModelViews.size();i++){
            medications.append("Prescription #").append(i+1).append(" : ");
            if (!rxModelViews.get(i).getRx().equals(""))
            medications.append("RX Number : ").append(rxModelViews.get(i).getRx()).append("   ");
            if (!rxModelViews.get(i).getMedicationName().equals(""))
                medications.append("Medication Name : ").append(rxModelViews.get(i).getMedicationName());
            medications.append("\n");
        }

        new Thread(new Runnable() {
            public void run() {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "mail.navanmedicalcentre.com");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.ssl.enable", "false");

                    Session session = Session.getInstance(props, null);

                    sendImageEmail(session,"pharmacy@navanmedicalcentre.com","Refill RX - "+ Calendar.getInstance().getTimeInMillis()
                            ,"Name : " + fName +"  " + lName +"\n\n" +
                                    "Phone : " + phone + "\n\n" +
                                    "I want to receive my medication by : " + place + "\n\n" +

                                    (notes.equals("")? "" : "Notes : " + notes+"\n\n") +
                                    medications);
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AddRefillActivity.this, "Sending Cancelled!", Toast.LENGTH_LONG).show();
                        }
                    });
//                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                }

            }

        }).start();

    }
//   "Location : " + "https://maps.google.com?q="+place.getLatLng().latitude+","+place.getLatLng().longitude + "\n\n" +

    @Override
    public void scan(int position) {
            scanBar(position);
    }

    //product barcode mode

    public void scanBar(int position) {
        this.position = position;
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(AddRefillActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }


    //product qr code mode
    public void scanQR() {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(AddRefillActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }
    //alert dialog for downloadDialog

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }


    public void sendImageEmail(Session session, String toEmail, String subject, String body){
        try{
            //"pharmacyapp@navanmedicalcentre.com"
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom("pharmacyapp@navanmedicalcentre.com");
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(body);

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);


            //Set the multipart message to the email message
            msg.setContent(multipart);

            // Send message
            Transport.send(msg,"pharmacyapp@navanmedicalcentre.com",
                    "JesusisGod3571!");

            runOnUiThread(new Runnable() {
                public void run() {
                    pd.dismiss();
                    Toast.makeText(AddRefillActivity.this, "Your Request has been Sent Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }catch (MessagingException e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    pd.dismiss();
                    Toast.makeText(AddRefillActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}
