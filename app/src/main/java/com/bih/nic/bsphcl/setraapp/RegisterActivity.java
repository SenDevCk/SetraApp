package com.bih.nic.bsphcl.setraapp;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bih.nic.bsphcl.utilitties.Urls_this_pro;
import com.bih.nic.bsphcl.utilitties.Utiilties;
import com.bih.nic.bsphcl.utilitties.WebHandler;



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar_mpin;
    Button button_change_mpin;
    EditText edit_user_id,edit_pass,edit_reg_mobile,edit_email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String req_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar_mpin=(Toolbar)findViewById(R.id.toolbar_mpin);
        toolbar_mpin.setTitle("Register");
        setSupportActionBar(toolbar_mpin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init() {
        edit_user_id=(EditText) findViewById(R.id.edit_user_id);
        edit_pass=(EditText) findViewById(R.id.edit_pass);
        edit_reg_mobile=(EditText) findViewById(R.id.edit_reg_mobile);
        edit_email=(EditText) findViewById(R.id.edit_email);
        button_change_mpin=(Button)findViewById(R.id.button_reg);
        button_change_mpin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (edit_user_id.getText().toString().trim().equals("")){
            edit_user_id.setError("Enter Valid User Id");
        }else  if (edit_pass.getText().toString().trim().equals("")){
            edit_pass.setError("Enter Valid Password");
        }else  if (edit_reg_mobile.getText().toString().trim().equals("") || edit_reg_mobile.getText().toString().trim().length()<10){
            edit_reg_mobile.setError("Enter Valid Mobile No");
        } else  if (edit_email.getText().toString().trim().equals("") || !edit_email.getText().toString().trim().matches(emailPattern)){
            edit_email.setError("Enter Valid email id");
        }else{
            new RequestOTP().execute(edit_user_id.getText().toString().trim()+"|"+edit_pass.getText().toString().trim()+"|"+edit_reg_mobile.getText().toString().trim()+"|"+edit_email.getText().toString().trim());
        }
    }

    public void AlertDialogForPrinter() {
        final Dialog dialog = new Dialog(RegisterActivity.this);
        /*	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before*/

        dialog.setContentView(R.layout.otp_dialog);
        // set the custom dialog components - text, image and button
        //final EditText epson = (EditText) dialog.findViewById(R.id.enter_otp);
        final EditText otp_view = (EditText) dialog.findViewById(R.id.otp_view);
        final Button button_submit = (Button) dialog.findViewById(R.id.button_submit);
        // if button is clicked, close the custom dialog
        button_submit.setOnClickListener(v -> {
            if (button_submit.getText().toString().trim().length()<6){
                Toast.makeText(RegisterActivity.this, "Enter Valid OTP !", Toast.LENGTH_SHORT).show();
            }else {
                dialog.dismiss();

                /* String userid = arr[0];
    String password = arr[1];
    String mobileNo = arr[2];
    String email = arr[3];
    String otp = arr[4];
    String imei = arr[5];
    String mobileDetails = model no company; */
                try{
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (tm != null) ;
                    if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    new RegiterLoader().execute(edit_user_id.getText().toString().trim()+"|"+edit_pass.getText().toString().trim()+"|"+edit_reg_mobile.getText().toString().trim()+"|"+edit_email.getText().toString().trim()
                            +"|"+otp_view.getText().toString().trim()+"|"+tm.getDeviceId()+"|"+ Build.MODEL+" "+ Build.BRAND);
                }catch (Exception e){}

            }

        });
        //Button Ok = (Button) dialog.findViewById(R.id.btn_OK);
        // if button is clicked, close the custom dialog
        dialog.show();
    }

    class RequestOTP extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog1 = new ProgressDialog(RegisterActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog1.setCanceledOnTouchOutside(false);
            this.dialog1.setMessage("Processing...");
            this.dialog1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            req_string=strings[0];
            String result = null;
            if (Utiilties.isOnline(RegisterActivity.this)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        result = WebHandler.callByPostwithoutparameter(Urls_this_pro.OTP_IN_URL + reqString(strings[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    alertDialog.setMessage("Android version must KITKAT (19) or above !");
                    alertDialog.show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "No internet Connection !", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.dialog1.isShowing()) this.dialog1.cancel();
            if (result!=null){
                if (result.contains("Success")) {
                    AlertDialogForPrinter();
                }else{
                    alertDialog.setMessage(result);
                    alertDialog.show();
                }
            }else {
                alertDialog.setMessage("Server not Responding !");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", (dialog, which) -> dialog.cancel());
                alertDialog.show();
            }
        }
    }
    class RegiterLoader extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog1 = new ProgressDialog(RegisterActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog1.setCanceledOnTouchOutside(false);
            this.dialog1.setMessage("Processing...");
            this.dialog1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            req_string=strings[0];
            String result = null;
            if (Utiilties.isOnline(RegisterActivity.this)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        result = WebHandler.callByPostwithoutparameter(Urls_this_pro.REGISTER_IN_URL + reqString(strings[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    alertDialog.setMessage("Android version must KITKAT (19) or above !");
                    alertDialog.show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "No internet Connection !", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.dialog1.isShowing()) this.dialog1.cancel();
            if (result!=null){
               alertDialog.setMessage(result);
               alertDialog.show();
               edit_user_id.setText("");
               edit_pass.setText("");
               edit_reg_mobile.setText("");
               edit_email.setText("");
            }else {
                alertDialog.setMessage("Server not Responding !");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", (dialog, which) -> dialog.cancel());
                alertDialog.show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String reqString(String req_string) {
        byte[] chipperdata = Utiilties.rsaEncrypt(req_string.getBytes(), RegisterActivity.this);
        Log.e("chiperdata", new String(chipperdata));
        String encString = android.util.Base64.encodeToString(chipperdata, Base64.NO_WRAP);//.getEncoder().encodeToString(chipperdata);
        encString = encString.replaceAll("\\/", "SSLASH").replaceAll("\\=", "EEQUAL").replaceAll("\\+", "PPLUS");
        return encString;
    }
}
