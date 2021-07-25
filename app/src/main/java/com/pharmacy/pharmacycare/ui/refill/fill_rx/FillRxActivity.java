package com.pharmacy.pharmacycare.ui.refill.fill_rx;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.ui.refill.add.AutoAddTextWatcher;
import com.sun.mail.smtp.SMTPMessage;

import java.io.File;
import java.io.UnsupportedEncodingException;
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

import static butterknife.internal.Utils.arrayOf;

public class FillRxActivity extends AppCompatActivity implements FillRxView, DatePickerDialog.OnDateSetListener {

    private static final int STORAGE_PERMISSION = 1;
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


//    @BindView(R.id.address)
//    TextView address;
//

    @BindView(R.id.continue_refill)
    TextView continue_refill;

    @BindView(R.id.deliveryRb)
    RadioButton deliverRb;

    @BindView(R.id.pickupRb)
    RadioButton pickupRb;

    @BindView(R.id.notes)
    EditText notes;

    @BindView(R.id.upload)
    ImageView upload;

    @BindView(R.id.file)
    TextView file;


    @BindView(R.id.txt_allergies)
    EditText txt_allergies;

    private Dialog pd;
    private FillRxPresenter addRefillPresenter;
    private int startYear = 1930, startMonth = 1, startDay = 1;
    private DatePickerDialog datePickerDialog;
    private String place = "Pickup";
    private Uri URI;

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        ButterKnife.bind(this);
        initToolbar();

        pd =new Dialog(this, R.style.DialogCustomTheme);
        pd.setContentView(R.layout.view_progress);
        pd.setCancelable(false);

        addRefillPresenter = new FillRxPresenterImpl(this);


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
                this, FillRxActivity.this, startYear, startMonth - 1, startDay);

        tet_phone.addTextChangedListener(new AutoAddTextWatcher(tet_phone,
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
                        , tet_phone.getText().toString().trim(), txt_date_of_birh.getText().toString().trim(), place,
                        txt_allergies.getText().toString(), file.getText().toString(), notes.getText().toString());
            }
        });

        pickupRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) place = "Pickup";
                else place = "Delivery";
            }
        });

        deliverRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) place = "Delivery";
                else place = "Pickup";
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(FillRxActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FillRxActivity.this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION);
                } else {
                    pickImage();
                }
                //    showFileChooser();
            }
        });
    }


    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), STORAGE_PERMISSION);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STORAGE_PERMISSION) {
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String attachmentFile = cursor.getString(columnIndex);
                File f = new File(attachmentFile);
//                URI = Uri.parse("file://" + attachmentFile);
                file.setText(attachmentFile);
                URI = FileProvider.getUriForFile(this, "com.pharmacy.pharmacycare.provider", f);

                cursor.close();

            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(FillRxActivity.this, "Your Request has been Sent Successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(FillRxActivity.this, "Sending Cancelled!", Toast.LENGTH_LONG).show();
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
        title.setText(getString(R.string.refill_rx));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        startYear = year;
        startMonth = month + 1;
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
        tet_fname.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidLName() {
        tet_lname.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidPhone() {
        tet_phone.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.edit_txt_error_bg));
    }

    @Override
    public void invalidDate() {
        txt_date_of_birh.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.edit_txt_error_bg));
    }
//
//    @Override
//    public void invalidPlace() {
//        pick_up.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.button_error_bg));
//    }

    @Override
    public void invalidFile() {
        Toast.makeText(FillRxActivity.this, "Upload your prescription", Toast.LENGTH_LONG).show();
    }


    @Override
    public void validFName() {
        tet_fname.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validLName() {
        tet_lname.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validPhone() {
        tet_phone.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.edit_txt_bg));
    }

    @Override
    public void validDate() {
        txt_date_of_birh.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.edit_txt_bg));
    }
//
//    @Override
//    public void validPlace() {
//        pick_up.setBackground(ContextCompat.getDrawable(FillRxActivity.this, R.drawable.pick_up_bg));
//    }

    @Override
    public void validFile() {

    }


    @Override
    public void sendRefill(final String fName, final String lName, final String phone, final String bithday, final String place,
                           final String allergies, final String file, final String notes) {

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

                    sendImageEmail(session,"pharmacy@navanmedicalcentre.com","Fill RX - " + Calendar.getInstance().getTimeInMillis()
                    ,"Name : " + fName + "  " + lName + "\n\n" +
                                    "Phone : " + phone + "\n\n" +
                                    "Date of Birth : " + bithday + "\n\n" +
                                    (allergies.equals("") ? "" : "Allergies : " + allergies + "\n\n") +
                                    "I want to receive my medication by : " + place + "\n\n" +
                                    (notes.equals("") ? "" : "Notes : " + notes + "\n\n"),file);
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(FillRxActivity.this, "Sending Cancelled!", Toast.LENGTH_LONG).show();
                        }
                    });
//                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                }

            }

        }).start();

    }



    public void sendImageEmail(Session session, String toEmail, String subject, String body,String filename){
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

            // Second part is image attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            //Trick is to add the content-id header here
            messageBodyPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(messageBodyPart);

            //third part for displaying image in the email body
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<h1>Attached Image</h1>" +
                    "<img src='cid:image_id'>", "text/html");
            multipart.addBodyPart(messageBodyPart);

            //Set the multipart message to the email message
            msg.setContent(multipart);

            // Send message
            Transport.send(msg,"pharmacyapp@navanmedicalcentre.com",
                    "JesusisGod3571!");

           runOnUiThread(new Runnable() {
                public void run() {
                    pd.dismiss();
                    Toast.makeText(FillRxActivity.this, "Your Request has been Sent Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }catch (MessagingException e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    pd.dismiss();
                    Toast.makeText(FillRxActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
//        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setType("file/*");
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fill RX - " + Calendar.getInstance().getTimeInMillis());
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pharmacy@navanmedicalcentre.com"});
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Name : " + fName + "  " + lName + "\n\n" +
//                "Phone : " + phone + "\n\n" +
//                "Date of Birth : " + bithday + "\n\n" +
//                (allergies.equals("") ? "" : "Allergies : " + allergies + "\n\n") +
//                "I want to receive my medication by : " + place + "\n\n" +
//                (notes.equals("") ? "" : "Notes : " + notes + "\n\n")
//
//        );



