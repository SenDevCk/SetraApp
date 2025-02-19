package com.bih.nic.bsphcl.setraapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.bih.nic.bsphcl.model.UserInformation;
import com.bih.nic.bsphcl.utilitties.CommonPref;
import com.bih.nic.bsphcl.utilitties.Urls_this_pro;
import com.bih.nic.bsphcl.utilitties.Utiilties;
import com.bih.nic.bsphcl.utilitties.WebHandler;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private BottomSheetBehavior mBottomSheetBehavior1;
    RelativeLayout mButton1, mButton2,mbtnCouneter, button_3,upload_fasal_chhati;
    TextView text_username, text_add_detail, text_grih_chatti_count,text_fashal_chatti_count;
    ImageView logot;
    TextView text_email,text_mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bgLayout);
        NestedScrollView bottomSheet2 = (NestedScrollView) coordinatorLayout.findViewById(R.id.bottom_sheet2);
        logot = (ImageView) coordinatorLayout.findViewById(R.id.logot);
        text_username = (TextView) coordinatorLayout.findViewById(R.id.text_username);
        text_add_detail = (TextView) coordinatorLayout.findViewById(R.id.text_add_detail);
        text_email = (TextView) coordinatorLayout.findViewById(R.id.text_email);
        text_mb = (TextView) coordinatorLayout.findViewById(R.id.text_mb);
        text_grih_chatti_count = (TextView) coordinatorLayout.findViewById(R.id.text_grih_chatti_count);
        text_fashal_chatti_count = (TextView) coordinatorLayout.findViewById(R.id.text_fashal_chatti_count);
        UserInformation userInfo2=CommonPref.getUserDetails(Main2Activity.this);
        text_username.setText(""+userInfo2.getUserName()+" ( "+userInfo2.getRole()+" )");
        text_add_detail.setText(""+ userInfo2.getSubDivision()+", "+userInfo2.getDivision());
        text_email.setText(""+ userInfo2.getEmail());
        if(Build.VERSION.SDK_INT >= 29){
            text_mb.setText("******" + userInfo2.getContactNo().substring(userInfo2.getContactNo().length()-4) + "\n UUID : **************" + userInfo2.getImeiNo().substring(userInfo2.getImeiNo().length()-2));
        }
        else {
            text_mb.setText("******" + userInfo2.getContactNo().substring(userInfo2.getContactNo().length()-4) + "\n IMEI : **************" + userInfo2.getImeiNo().substring(userInfo2.getImeiNo().length()-2));
        }
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet2);
        mBottomSheetBehavior1.setHideable(false);
        mBottomSheetBehavior1.setPeekHeight(350);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

        mButton1 =  findViewById(R.id.button_1);
        mButton2 =  findViewById(R.id.button_2);
        mbtnCouneter =  findViewById(R.id.button_new);

        if(userInfo2.getUserID().equalsIgnoreCase("AITM_HQRP") ||
                userInfo2.getUserID().equalsIgnoreCase("AITM_SB") ||
                userInfo2.getUserID().equalsIgnoreCase("ADMIN_SB") ||
                userInfo2.getUserID().equalsIgnoreCase("AITM_HQ_NIC") ||
                userInfo2.getUserID().equalsIgnoreCase("ITM_HQ_NIC")||
                userInfo2.getUserID().equalsIgnoreCase("AITM_RAJ")){
            mbtnCouneter.setVisibility(View.VISIBLE);
        }else{
            mbtnCouneter.setVisibility(View.GONE);
        }

        button_3 =  findViewById(R.id.button_3);
        //upload_fasal_chhati =  findViewById(R.id.upload_fasal_chhati);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mbtnCouneter.setOnClickListener(this);
        button_3.setOnClickListener(this);
        logot.setOnClickListener(this);

    }
    /*   mButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                        //mButton1.setText(R.string.collapse_button1);
                    }
                    else {
                        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                       // mButton1.setText(R.string.button1);
                    }
                }
            });*/
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(Main2Activity.this);
            input.setHint("Enter Transaction ID");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);
            builder.setMessage("Would you like to get Transaction OTP ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        if (input.getText().toString().trim().equals("")||input.getText().toString().trim().length()<3) {
                            Toast.makeText(Main2Activity.this, "Enter valid Transaction Id !", Toast.LENGTH_SHORT).show();
                        }else if (!Utiilties.isOnline(Main2Activity.this)){
                            Toast.makeText(Main2Activity.this, "Please go online !", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            UserInformation userInfo2=CommonPref.getUserDetails(Main2Activity.this);
                            new RequestTransactionOTPLoader().execute(userInfo2.getUserID()+"|"+userInfo2.getPassword()+"|"+userInfo2.getImeiNo()+"|"+"TP"+"|"+input.getText().toString());
                        }
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Transaction OTP");
            alert.show();
            //Toast.makeText(getApplicationContext(),"It will work after monday or tuesday..!",Toast.LENGTH_LONG).show();
        }else   if (view.getId() == R.id.button_new) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(Main2Activity.this);
            input.setHint("Enter Counter Activation Otp");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);
            builder.setMessage("Would you like to get Counter Activation OTP ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        if (input.getText().toString().trim().equals("")||input.getText().toString().trim().length()<3) {
                            Toast.makeText(Main2Activity.this, "Enter valid Counter Activtion Otp !", Toast.LENGTH_SHORT).show();
                        }else if (!Utiilties.isOnline(Main2Activity.this)){
                            Toast.makeText(Main2Activity.this, "Please go online !", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            UserInformation userInfo2=CommonPref.getUserDetails(Main2Activity.this);
                            new RequestTransactionOTPLoader().execute(userInfo2.getUserID()+"|"+userInfo2.getPassword()+"|"+userInfo2.getImeiNo()+"|"+"CP"+"|"+input.getText().toString());
                        }
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Counter Activation OTP");
            alert.show();
            //Toast.makeText(getApplicationContext(),"It will work after monday or tuesday..!",Toast.LENGTH_LONG).show();
        }  else if (view.getId() == R.id.button_2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Would you like to get Login OTP ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        UserInformation userInfo2=CommonPref.getUserDetails(Main2Activity.this);
                       if (!Utiilties.isOnline(Main2Activity.this)){
                            Toast.makeText(Main2Activity.this, "Please go online !", Toast.LENGTH_SHORT).show();
                        }else {
                           new RequestLoginOTPLoader().execute(userInfo2.getUserID() + "|" + userInfo2.getPassword() + "|" + userInfo2.getImeiNo() + "|" + "LP");
                       }
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.setTitle("Login OTP");
            alert.show();
        }
        else if (view.getId() == R.id.logot) {
            logout();
        }
        else if (view.getId() == R.id.button_3) {
            //logout();
        }else {
            Toast.makeText(getApplicationContext(),"Comming Soon..!",Toast.LENGTH_LONG).show();
        }

    }

    private void logout() {


        /* SplashActivity.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = SplashActivity.prefs.edit();
        editor.putBoolean("username", false);
        editor.putBoolean("password", false);
        editor.commit();*/

        Intent intent = new Intent(Main2Activity.this, LoginActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    class RequestTransactionOTPLoader extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog1 = new ProgressDialog(Main2Activity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(Main2Activity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog1.setCanceledOnTouchOutside(false);
            this.dialog1.setMessage("Processing...");
            this.dialog1.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = null;
            if (Utiilties.isOnline(Main2Activity.this)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        result = WebHandler.callByPostwithoutparameter(Urls_this_pro.TRAN_OTP_IN_URL + reqString(strings[0]));

                }else{
                    alertDialog.setMessage("Android version must kitkat(19) or above !");
                    alertDialog.show();
                }
            }else{
                Toast.makeText(Main2Activity.this, "No internet Connection !", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.dialog1.isShowing()) {
                this.dialog1.cancel();
            }
            if (result!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("pin")) {
                        alertDialog.setMessage("Your Transaction PIN : " + jsonObject.getString("pin"));
                    } else {
                        alertDialog.setMessage("Transaction PIN not Generated !");
                    }
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                alertDialog.setMessage("Server not Responding !");
                alertDialog.setButton( DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        }
    }



    class RequestLoginOTPLoader extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog1 = new ProgressDialog(Main2Activity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(Main2Activity.this).create();

        @Override
        protected void onPreExecute() {
            this.dialog1.setCanceledOnTouchOutside(false);
            this.dialog1.setMessage("Processing...");
            this.dialog1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            if (Utiilties.isOnline(Main2Activity.this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        result = WebHandler.callByPostwithoutparameter(Urls_this_pro.LOGIN_OTP_IN_URL + reqString(strings[0]));
                }else{
                    alertDialog.setMessage("Android version must KITKAT (19) or above !");
                    alertDialog.show();
                }

            }else{
                Toast.makeText(Main2Activity.this, "No internet Connection !", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.dialog1.isShowing()) {
                this.dialog1.cancel();
            }
            if (result!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("pin")) {
                        alertDialog.setMessage("Your Login PIN : " + jsonObject.getString("pin"));
                    } else {
                        alertDialog.setMessage("Login PIN not Generated !");
                    }
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                alertDialog.setMessage("Server not Responding !");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", (dialog, which) -> dialog.cancel());
                alertDialog.show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String reqString(String req_string) {
        byte[] chipperdata = Utiilties.rsaEncrypt(req_string.getBytes(), Main2Activity.this);
        Log.e("chiperdata", new String(chipperdata));
        String encString = android.util.Base64.encodeToString(chipperdata, Base64.NO_WRAP);//.getEncoder().encodeToString(chipperdata);
        encString = encString.replaceAll("\\/", "SSLASH").replaceAll("\\=", "EEQUAL").replaceAll("\\+", "PPLUS");
        return encString;
    }

}
