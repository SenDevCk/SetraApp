package com.bih.nic.bsphcl.setraapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bih.nic.bsphcl.model.UserInformation;
import com.bih.nic.bsphcl.smsReceiver.SmsReceiver;
import com.bih.nic.bsphcl.utilitties.CommonPref;
import com.bih.nic.bsphcl.utilitties.GlobalVariables;
import com.bih.nic.bsphcl.utilitties.Urls_this_pro;
import com.bih.nic.bsphcl.utilitties.Utiilties;
import com.bih.nic.bsphcl.utilitties.WebHandler;
import com.bih.nic.bsphcl.webHelper.WebServiceHelper;



public class LoginActivity2 extends AppCompatActivity implements View.OnClickListener {
    EditText edit_user_name, edit_pass;
    Button button_login, signup;
    TextView text_ver, text_imei;
    String version;
    TextView text_forgot_password;
    TelephonyManager tm;
    private ProgressDialog dialog = null;
    private static String imei = "";
    SmsReceiver smsReceiver;
    IntentFilter filter;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        readPhoneState();
    }

    private void init() {
        text_imei = findViewById(R.id.txtimei);
        text_ver = findViewById(R.id.txtVersion);
        edit_user_name = findViewById(R.id.edit_username);
        edit_pass = findViewById(R.id.edit_pass);
        button_login = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        text_forgot_password = findViewById(R.id.text_forget_pass);
        text_forgot_password.setOnClickListener(this);
        button_login.setOnClickListener(this);
        signup.setOnClickListener(this);
        if (CommonPref.getUserDetails(LoginActivity2.this) != null) {
            if (!CommonPref.getUserDetails(LoginActivity2.this).getUserID().trim().isEmpty()) {
                edit_user_name.setText("" + CommonPref.getUserDetails(LoginActivity2.this).getUserID());
                //edit_user_name.setText("AITM_SB");
            }
        }
        dialog = new ProgressDialog(
                LoginActivity2.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Authenticating...");
    }

    public void readPhoneState() {
        try {
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) ;
            if (ActivityCompat.checkSelfPermission(LoginActivity2.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                imei = tm.getDeviceId();
            } else {
                imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
                //   imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            version = LoginActivity2.this.getPackageManager().getPackageInfo(LoginActivity2.this.getPackageName(), 0).versionName;
            //Log.e("App Version : ", "" + version + " ( " + imei + " )");
            text_ver.setText("App Version : " + version + "( M )");
            text_imei.setText("IMEI NO : " + imei);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signin) {
            if (Utiilties.isDebugConnected()) {
                Toast.makeText(this, "Debugging not allowed !", Toast.LENGTH_SHORT).show();
            } else if (edit_user_name.getText().toString().trim().equals("")) {
                Toast.makeText(LoginActivity2.this, "Enter User Name", Toast.LENGTH_SHORT).show();
            } else if (edit_pass.getText().toString().trim().equals("")) {
                Toast.makeText(LoginActivity2.this, "Enter Password", Toast.LENGTH_SHORT).show();
            } else if (!Utiilties.isOnline(LoginActivity2.this)) {
                Toast.makeText(LoginActivity2.this, "Please go online !", Toast.LENGTH_SHORT).show();
            } else {
                button_login.setEnabled(false);
                filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
                smsReceiver=new SmsReceiver();
                registerReceiver(smsReceiver, filter);
                SmsReceiver.bindListener(messageText -> {
                    // text_resend.setVisibility(View.GONE);
                    Log.d("activity",""+messageText);
                    if (countDownTimer!=null)countDownTimer.cancel();
                    // dialog1.dismiss();
                    //if (smsVerificationService!=null && smsVerificationService.getStatus()!=SmsVerificationService.Status.RUNNING) {
                    new SmsVerificationService(LoginActivity2.this,imei,imei).execute(messageText.split(" ")[0].trim());
                    //}
                });
                new LoginLoader().execute(edit_user_name.getText().toString().trim() + "|" + edit_pass.getText().toString().trim() + "|" + imei.trim() + "|" + version.trim());//
            }
        } else if (v.getId() == R.id.signup) {
            Intent intent = new Intent(LoginActivity2.this, RegisterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.text_forget_pass) {
            if (edit_user_name.getText().toString().trim().equals("")) {
                Toast.makeText(LoginActivity2.this, "Enter User Name", Toast.LENGTH_SHORT).show();
            } else {
                if (Utiilties.isOnline(LoginActivity2.this)) {
                    final AlertDialog alertDialog1 = new AlertDialog.Builder(LoginActivity2.this).create();
                    alertDialog1.setTitle("Forgot Password");
                    alertDialog1.setMessage("Are you sure to reset Password");
                    alertDialog1.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) ->
                            new Requestforgetpassword().execute(reqString(edit_user_name.getText().toString() + "|" + imei)));
                    alertDialog1.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
                        if (alertDialog1.isShowing()) {
                            alertDialog1.dismiss();
                        }
                    });
                    alertDialog1.show();
                } else {
                    Toast.makeText(this, "Please be online for this service", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    class LoginLoader extends AsyncTask<String, Integer, UserInformation> {

        private final ProgressDialog dialog1 = new ProgressDialog(LoginActivity2.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity2.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog1.setCanceledOnTouchOutside(false);
            this.dialog1.setMessage("Logging...");
            this.dialog1.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setMessage("Logging..."+values[0]+" %");
        }

        @Override
        protected UserInformation doInBackground(String... strings) {
            UserInformation userInfo2 = null;
            if (Utiilties.isOnline(LoginActivity2.this)) {
                String result = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        result = WebHandler.callByPostwithoutparameter(Urls_this_pro.LOG_IN_URL + reqString(strings[0]));
                        //System.out.println(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    alertDialog.setMessage("Android version must kitkat(19) or above !");
                    alertDialog.show();
                }
                if (result != null) {
                    userInfo2 = WebServiceHelper.loginParser(result);
                }
                return userInfo2;
            } else {
                userInfo2 = CommonPref.getUserDetails(LoginActivity2.this);
                if (userInfo2.getUserID().length() > 4) {
                    userInfo2.setAuthenticated(true);
                    return userInfo2;
                } else {
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(UserInformation result) {
            super.onPostExecute(result);
            if (this.dialog1.isShowing()) {
                this.dialog1.cancel();
            }
            if (Utiilties.isDebugConnected()) {
                Toast.makeText(LoginActivity2.this, "Debugging Enabled !", Toast.LENGTH_SHORT).show();
                LoginActivity2.this.finish();
            }
            if (result == null) {
                button_login.setEnabled(true);
                alertDialog.setTitle("Failed");
                alertDialog.setMessage("Something Went Wrong ! Try again after some time !");
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) -> edit_user_name.setFocusable(true));
                alertDialog.show();
            }
            else if (!result.getAuthenticated()) {
                if ((result.getMessageString().trim().contains("OTP SENT"))) {
                    countDownTimer = new CountDownTimer(30000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long seconds=millisUntilFinished / 1000;
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setMessage("Waiting For Otp for 30 seconds... remaining"+(seconds));
                            dialog.show();
                            //here you can have your logic to set text to edittext
                        }

                        public void onFinish() {
                            dialog.dismiss();
                            AlertDialogForOTP();
                        }

                    }.start();

                } else {
                    button_login.setEnabled(true);
                    String msgs = "IMEI MISMATCH";
                    if (result.getMessageString().equalsIgnoreCase(msgs)) {
                        text_imei.setVisibility(View.VISIBLE);
                        text_imei.setText("IMEI NO : " + imei);
                        result.getMessageString().concat("\nYour Previous IMEI was : "+imei);
                    }
                    alertDialog.setTitle("Failed");
                    alertDialog.setMessage("Authentication Failed" + result.getMessageString());
                    alertDialog.show();
                }
            } else {
                Intent cPannel = new Intent(LoginActivity2.this, Main2Activity.class);
                if (Utiilties.isOnline(LoginActivity2.this)) {
                    if (result != null) {
                        if (imei.equalsIgnoreCase(result.getImeiNo().trim())) {
                            try {
                                result.setPassword(edit_pass.getText().toString());
                                GlobalVariables.LoggedUser = result;
                                CommonPref.setUserDetails(LoginActivity2.this, result);
                                cPannel.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(cPannel);
                                LoginActivity2.this.finish();
                            } catch (Exception ex) {
                                Toast.makeText(LoginActivity2.this, "Login failed due to Some Error !", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            button_login.setEnabled(true);
                            alertDialog.setTitle("Device Not Registered");
                            alertDialog.setMessage("Sorry, your device is not registered!.\r\nPlease contact your Admin.");
                            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) -> edit_user_name.setFocusable(true));
                            alertDialog.show();
                        }
                    }
                } else {
                    if (CommonPref.getUserDetails(LoginActivity2.this) != null) {
                        GlobalVariables.LoggedUser = result;
                        if (GlobalVariables.LoggedUser.getUserID().equalsIgnoreCase(edit_user_name.getText().toString().trim()) && GlobalVariables.LoggedUser.getPassword().equalsIgnoreCase(edit_pass.getText().toString().trim())) {
                            cPannel.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(cPannel);
                            LoginActivity2.this.finish();
                        } else {
                            Toast.makeText(LoginActivity2.this, "User name and password not matched !", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity2.this, "Please enable internet connection for first time login.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String reqString(String req_string) {
        byte[] chipperdata = Utiilties.rsaEncrypt(req_string.getBytes(), LoginActivity2.this);
        Log.e("chiperdata", new String(chipperdata));
        String encString = android.util.Base64.encodeToString(chipperdata, Base64.NO_WRAP);//.getEncoder().encodeToString(chipperdata);
        encString = encString.replaceAll("\\/", "SSLASH").replaceAll("\\=", "EEQUAL").replaceAll("\\+", "PPLUS");
        return encString;
    }

    class Requestforgetpassword extends AsyncTask<String, Integer, String> {
        private final ProgressDialog dialog = new ProgressDialog(LoginActivity2.this);
        private final AlertDialog alertdialog = new AlertDialog.Builder(LoginActivity2.this).create();

        @Override
        protected void onPreExecute() {
            this.dialog.setCancelable(false);
            this.dialog.setMessage("Processing....");
            // this.dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            result = WebHandler.callByGet(Urls_this_pro.FORGET_PASSWORD_URL + strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.dialog.isShowing()) {
                this.dialog.cancel();
            }
            if (s != null) {
                alertdialog.setMessage(s.trim());
                alertdialog.show();
            }
        }
    }

    public class SmsVerificationService extends AsyncTask<String, Integer, String> {
        private Activity activity;
        private android.app.AlertDialog alertDialog;
        String imei, serial_id;

        public SmsVerificationService(Activity activity, String imei, String serial_id) {
            this.activity = activity;
            this.imei = imei;
            this.serial_id = serial_id;
            alertDialog = new android.app.AlertDialog.Builder(this.activity).create();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Verifying...");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setMessage("Verifying..."+values[0]+" %");
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            if (Utiilties.isOnline(activity)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Log.d("params", strings[0]);
                    //return WebServiceHelper.validateotp(userid, imei, strings[0]);
                    return WebHandler.callByGet(Urls_this_pro.GET_VALIDATE_OTP + reqString(edit_user_name.getText().toString() + "|" + edit_pass.getText().toString() + "|" + imei + "|" + strings[0].trim()));
                } else {
                    Log.e("error", "Your device must have atleast Kitkat or Above Version");
                }
            } else {
                Log.e("error", "No Internet Connection !");
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.e("res", "res varification :" + result);
                if (result.contains("SUCCESS")) {
                    if (dialog.isShowing()) dialog.dismiss();
                    new LoginLoader().execute(edit_user_name.getText().toString().trim() + "|" + edit_pass.getText().toString().trim() + "|" + imei.trim() + "|" + version.trim());//
                } else {
                    if (dialog.isShowing()) dialog.dismiss();
                    Toast.makeText(activity, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (dialog.isShowing()) dialog.dismiss();
                Toast.makeText(activity, "Server Problem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void AlertDialogForOTP() {
        final Dialog dialogOtp = new Dialog(LoginActivity2.this);
        /*	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before*/
        dialogOtp.setContentView(R.layout.otp_dialog);
        // set the custom dialogOtp components - text, image and button
        //final EditText otpEdit = (EditText) dialogOtp.findViewById(R.id.enter_otp);
        final EditText otp_view = (EditText) dialogOtp.findViewById(R.id.otp_view);
        final Button button_submit = (Button) dialogOtp.findViewById(R.id.button_submit);
        final TextView text_timer = (TextView) dialogOtp.findViewById(R.id.text_timer);
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secs= millisUntilFinished / 1000;
                text_timer.setVisibility(View.VISIBLE);
                text_timer.setText(Html.fromHtml("<b style=\"color:White;\">" + (secs) + "</b> "));
                text_timer.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                text_timer.setVisibility(View.GONE);
                dialogOtp.dismiss();
            }

        }.start();
        // if button is clicked, close the custom dialogOtp
        SmsReceiver.bindListener(messageText -> {
            //Log.d("activity",""+messageText);
            otp_view.setText(messageText.split(" ")[0].trim());
        });
        button_submit.setOnClickListener(v -> {
            if (otp_view.getText().length() < 6) {
                Toast.makeText(LoginActivity2.this, "Enter Valid OTP !", Toast.LENGTH_SHORT).show();
            } else {
                button_submit.setClickable(false);
                dialogOtp.dismiss();
                new SmsVerificationService(LoginActivity2.this, imei, imei).execute(otp_view.getText().toString().trim());
            }

        });
        dialogOtp.show();
    }

}