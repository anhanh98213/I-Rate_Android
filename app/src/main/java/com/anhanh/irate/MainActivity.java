package com.anhanh.irate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final Context context = this;
    final Calendar calendar = Calendar.getInstance();

    private RatingBar foodRating, cleanRating, serviceRating;
    private EditText edt_reporter, edt_rtName, edt_rtType, edt_dVisit, edt_price, edt_notes;
    private Button btn_addFeedback;
    private Toolbar toolbar;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        btn_addFeedback = findViewById(R.id.add_feedback);
        edt_dVisit = findViewById(R.id.time_visit);

        edt_reporter = findViewById(R.id.reporter);
        edt_rtName = findViewById(R.id.restaurant_Name);
        edt_rtType = findViewById(R.id.restaurant_type);
        edt_price = findViewById(R.id.price);
        edt_notes = findViewById(R.id.notes);

        foodRating = findViewById(R.id.food_quality_rating);
        cleanRating = findViewById(R.id.cleanliness_rating);
        serviceRating = findViewById(R.id.service_rating);

        btn_addFeedback.setOnClickListener(this);
        edt_dVisit.setOnClickListener(this);

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edt_dVisit.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onClick(View v) {
        int food_quality_rating = (int) this.foodRating.getRating();
        int cleanliness_rating = (int) this.cleanRating.getRating();
        int service_rating = (int) this.serviceRating.getRating();
        String edt_reporter= this.edt_reporter.getText().toString();
        String edt_rtName = this.edt_rtName.getText().toString();
        String edt_rtType = this.edt_rtType.getText().toString();
        String edt_dVisit = this.edt_dVisit.getText().toString();
        String edt_price = this.edt_price.getText().toString();
        String edt_notes = this.edt_notes.getText().toString();
        boolean validation = validation();
        switch (v.getId()){

            case R.id.add_feedback:
                if (edt_reporter.isEmpty() || edt_rtName.isEmpty() || edt_rtType.isEmpty() || edt_dVisit.isEmpty() || edt_price.isEmpty() || validation() == false){
                    customDialog(edt_reporter, edt_rtName, edt_rtType, edt_dVisit, edt_price);
                }else {
                    showFeedback(edt_reporter,edt_rtName, edt_rtType, edt_dVisit, edt_price,edt_notes, food_quality_rating, cleanliness_rating, service_rating);
                }
                break;
            case R.id.time_visit:
                new DatePickerDialog(MainActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

        }

    }

    private void customDialog(String edt_reporter, String edt_rtName, String edt_rtType, String edt_dVisit, String edt_price){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        setTextDialog(dialog, edt_reporter, edt_rtName, edt_rtType, edt_dVisit, edt_price);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void showFeedback(String edt_reporter, String edt_rtName, String edt_rtType, String edt_dVisit,
                              String edt_price,String edt_notes, int food_quality_rating, int cleanliness_rating, int service_rating){

        final Dialog feedback = new Dialog(context);
        feedback.setContentView(R.layout.your_feedback);

        setDataFb(feedback, edt_reporter, edt_rtName, edt_rtType, edt_dVisit,
                edt_price, edt_notes, food_quality_rating, cleanliness_rating, service_rating);
        Button btnfeedbackCancel = (Button) feedback.findViewById(R.id.feedbackCancel);
        btnfeedbackCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback.dismiss();
            }
        });
        Button btnfeedbackSend = (Button) feedback.findViewById(R.id.feedbackSend);
        btnfeedbackSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                feedback.dismiss();
            }
        });
        feedback.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private void setTextDialog(Dialog dialog, String edt_reporter, String edt_rtName, String edt_rtType, String edt_dVisit, String edt_price){
        TextView dg_reporter, dgrt_name, dgrt_type, dgd_visit, dg_price;

        dg_reporter = dialog.findViewById(R.id.dialog_reporter);
        dgrt_name  = dialog.findViewById(R.id.dialog_restaurant_name);
        dgrt_type  = dialog.findViewById(R.id.dialog_restaurant_type);
        dgd_visit  = dialog.findViewById(R.id.dialog_time_visit);
        dg_price   = dialog.findViewById(R.id.dialog_price);

        if(edt_reporter.isEmpty()){
            dg_reporter.setText("Reporter name");
        }
        if(edt_rtName.isEmpty() ){
            dgrt_name.setText("Restaurant name");
        }
        if(edt_rtType.isEmpty() ){
            dgrt_type.setText("Restaurant type");
        }
        if(edt_dVisit.isEmpty() ){
            dgd_visit.setText("Date and time of the visit");
        }
        if(edt_price.isEmpty() ){
            dg_price.setText("Price");
        }
    }
    private void setDataFb(Dialog feedback, String edt_reporter, String edt_rtName, String edt_rtType, String edt_dVisit,
                           String edt_price,String edt_notes, int food_quality_rating, int cleanliness_rating, int service_rating){
        TextView fb_reporter, fbrt_name, fbrt_type, fbd_visit, fb_price, fb_notes;
        RatingBar fb_food_quality_rating, fb_cleanliness_rating, fb_service_rating;

        fb_reporter =  feedback.findViewById(R.id.feedback_reporter);
        fbrt_name   =  feedback.findViewById(R.id.feedback_restaurant_name);
        fbrt_type   =  feedback.findViewById(R.id.feedback_restaurant_type);
        fbd_visit   =  feedback.findViewById(R.id.feedback_time_visit);
        fb_price    =  feedback.findViewById(R.id.feedback_price);
        fb_notes    =  feedback.findViewById(R.id.feedback_notes);
        fb_food_quality_rating = feedback.findViewById(R.id.feedback_food_quality_rating);
        fb_cleanliness_rating  = feedback.findViewById(R.id.feedback_cleanliness_rating);
        fb_service_rating      = feedback.findViewById(R.id.feedback_service_rating);

        fb_reporter.setText(edt_reporter);
        fbrt_name.setText(edt_rtName);
        fbrt_type.setText(edt_rtType);
        fbd_visit.setText(edt_dVisit);
        fb_price.setText(edt_price);
        fb_notes.setText(edt_notes);
        fb_food_quality_rating.setRating(food_quality_rating);
        fb_cleanliness_rating.setRating(cleanliness_rating);
        fb_service_rating.setRating(service_rating);
    }
    private void clearData(){
        edt_dVisit.getText().clear();
        edt_reporter.getText().clear();
        edt_rtName.getText().clear();
        edt_rtType.getText().clear();
        edt_price.getText().clear();
        edt_notes.getText().clear();
    }
    private boolean validation(){
        awesomeValidation.addValidation(this, R.id.reporter, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.reporter);
        awesomeValidation.addValidation(this, R.id.restaurant_Name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.restaurant_name);
        awesomeValidation.addValidation(this, R.id.restaurant_type, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.restaurant_Type);
        awesomeValidation.addValidation(this, R.id.price, "^[0-9]{1,}[\\.]{0,1}[0-9]{0,}$", R.string.price);
        if (awesomeValidation.validate()) {
            return true;
        }else return false;
    }

}