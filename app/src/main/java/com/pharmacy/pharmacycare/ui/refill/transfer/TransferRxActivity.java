package com.pharmacy.pharmacycare.ui.refill.transfer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.ui.refill.add.AutoAddTextWatcher;
import com.pharmacy.pharmacycare.ui.refill.fill_rx.FillRxActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

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

public class TransferRxActivity extends AppCompatActivity implements TransferRxView, DatePickerDialog.OnDateSetListener {

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

    @BindView(R.id.txt_date_of_birh)
    TextView txt_date_of_birh;


    @BindView(R.id.tet_address)
    EditText tet_address;

    @BindView(R.id.tet_pharmacy_name)
    EditText tet_pharmacy_name;

    @BindView(R.id.tet_pharmacy_code)
    EditText tet_pharmacy_code;

    @BindView(R.id.tet_pharmacy_phone)
    EditText tet_pharmacy_phone;

    @BindView(R.id.tet_pharmacy_address)
    EditText tet_pharmacy_address;



    @BindView(R.id.continue_refill)
    TextView continue_refill;

    @BindView(R.id.notes)
    EditText notes;


    private TransferRxPresenter addRefillPresenter;
    private int startYear = 1930, startMonth = 1, startDay = 1;
    private DatePickerDialog datePickerDialog;
    private Dialog pd;


    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_rx);
        ButterKnife.bind(this);
        initToolbar();


        pd =new Dialog(this, R.style.DialogCustomTheme);
        pd.setContentView(R.layout.view_progress);
        pd.setCancelable(false);


        addRefillPresenter = new TransferRxPresenterImpl(this);


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setupParent(v);
                return true;
            }
        });


        Calendar calendar = Calendar.getInstance();
        startMonth = calendar.get(Calendar.MONTH) + 1;
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startYear = calendar.get(Calendar.YEAR);


        datePickerDialog = new DatePickerDialog(
                this, TransferRxActivity.this, startYear, startMonth-1, startDay);

        tet_phone.addTextChangedListener(new AutoAddTextWatcher(tet_phone,
                "-", 3, 6));
        tet_pharmacy_phone.addTextChangedListener(new AutoAddTextWatcher(tet_pharmacy_phone,
                "-", 3, 6));
        handleClicks();


    }

    private void handleClicks() {
        txt_date_of_birh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        continue_refill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRefillPresenter.continueRefill(tet_fname.getText().toString().trim(), tet_lname.getText().toString().trim()
                        , tet_phone.getText().toString().trim(), txt_date_of_birh.getText().toString().trim(),
                        tet_address.getText().toString().trim(), tet_pharmacy_name.getText().toString().trim()
                        , tet_pharmacy_code.getText().toString().trim(), tet_pharmacy_phone.getText().toString().trim(),
                    tet_pharmacy_address.getText().toString().trim(), notes.getText().toString());
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                Toast.makeText(TransferRxActivity.this, "Your Request has been Sent Successfully", Toast.LENGTH_LONG).show();
                finish();
            }else {
                Toast.makeText(TransferRxActivity.this, "Sending Cancelled!", Toast.LENGTH_LONG).show();
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
        title.setText(getString(R.string.rx_transfer));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        startYear = year;
        startMonth = month+1;
        startDay = dayOfMonth;
        txt_date_of_birh.setText(formatDate(startYear, startMonth, startDay));
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
        tet_fname.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidLName() {
        tet_lname.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidPhone() {
        tet_phone.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidDate() {
        txt_date_of_birh.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidPharmacyName() {
        tet_pharmacy_name.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidPharmacyPhone() {
        tet_pharmacy_phone.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidPharmacyCode() {
        tet_pharmacy_code.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_error_bg));
    }


    @Override
    public void validFName() {
        tet_fname.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validLName() {
        tet_lname.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validPhone() {
        tet_phone.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validDate() {
        txt_date_of_birh.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validPharmacyName() {
        tet_pharmacy_name.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_bg));
    }
    @Override
    public void validPharmacyPhone() {
        tet_pharmacy_phone.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_bg));
    }
    @Override
    public void validPharmacyCode() {
        tet_pharmacy_code.setBackground(ContextCompat.getDrawable(TransferRxActivity.this, R.drawable.edit_txt_bg));
    }


    @Override
    public void sendRefill(final String fName, final String lName, final String phone, final String bithday,
                           final String address , final String pharmacyName,
                           final String pharmacyCode , final String pharmacyPhone, final String pharmacyAddress, final String notes) {

        pd.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "mail.navanmedicalcentre.com");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.ssl.enable", "false");

                    Session session = Session.getInstance(props, null);

                    sendImageEmail(session,"pharmacy@navanmedicalcentre.com", "Transfer RX - "+ Calendar.getInstance().getTimeInMillis(),
                            "Name : " + fName +"  " + lName +"\n\n" +
                            "Phone : " + phone + "\n\n" +
                            "Date of Birth : " + bithday + "\n\n" +
                            (address.equals("")? "" : "Address : " + address+"\n\n") +
                            "Name of the pharmacy you would like to transfer your file from : " + pharmacyName + "\n\n" +
                            "Pharmacy Phone Number : " + pharmacyCode +"  " + pharmacyPhone +"\n\n" +
                            (pharmacyAddress.equals("")? "" : "Pharmacy Address : " + pharmacyAddress+"\n\n") +
                            (notes.equals("")? "" : "Notes : " + notes+"\n\n"));
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TransferRxActivity.this, "Sending Cancelled!", Toast.LENGTH_LONG).show();
                        }
                    });
//                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                }

            }

        }).start();

//        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setType("plain/text");
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Transfer RX - "+ Calendar.getInstance().getTimeInMillis());
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pharmacy@navanmedicalcentre.com"});
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Name : " + fName +"  " + lName +"\n\n" +
//                "Phone : " + phone + "\n\n" +
//                "Date of Birth : " + bithday + "\n\n" +
//                (address.equals("")? "" : "Address : " + address+"\n\n") +
//                "Name of the pharmacy you would like to transfer your file from : " + pharmacyName + "\n\n" +
//                "Pharmacy Phone Number : " + pharmacyCode +"  " + pharmacyPhone +"\n\n" +
//                (pharmacyAddress.equals("")? "" : "Pharmacy Address : " + pharmacyAddress+"\n\n") +
//                (notes.equals("")? "" : "Notes : " + notes+"\n\n")
//        );
//        startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."),SEND_EMAIL);
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
                    Toast.makeText(TransferRxActivity.this, "Your Request has been Sent Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }catch (MessagingException e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    pd.dismiss();
                    Toast.makeText(TransferRxActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}

