package com.bih.nic.bsphcl.fragments;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bih.nic.bsphcl.model.UserInformation;
import com.bih.nic.bsphcl.setraapp.MainActivity;
import com.bih.nic.bsphcl.setraapp.R;
import com.bih.nic.bsphcl.utilitties.CommonPref;
import com.bih.nic.bsphcl.utilitties.GlobalVariables;
import com.bih.nic.bsphcl.utilitties.MarshmallowPermission;
import com.bih.nic.bsphcl.utilitties.Urls_this_pro;
import com.bih.nic.bsphcl.utilitties.Utiilties;
import com.bih.nic.bsphcl.utilitties.WebHandler;
import com.bih.nic.bsphcl.webHelper.WebServiceHelper;

import org.json.JSONObject;

/**
 * Created by Chandan Singh on 01-07-2018.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    EditText edit_user_name, edit_pass;
    Button button_login;
    ToggleButton toggleButton;
    TextView text_ver, text_forgot_password;
    MarshmallowPermission MARSHMALLOW_PERMISSION;
    String version;
    TelephonyManager tm;
    private static String imei = "";
    String uuid = "";
    String serial_id = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        readPhoneState();
    }

    private void init() {
        text_ver = (TextView) getActivity().findViewById(R.id.text_ver);
        text_forgot_password = (TextView) getActivity().findViewById(R.id.text_forget_pass);
        edit_user_name = (EditText) getActivity().findViewById(R.id.edit_user_name);
        edit_pass = (EditText) getActivity().findViewById(R.id.edit_pass);
        button_login = (Button) getActivity().findViewById(R.id.button_login);
        button_login.setOnClickListener(this);
        text_forgot_password.setOnClickListener(this);
        toggleButton = (ToggleButton) getActivity().findViewById(R.id.watch_pass);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edit_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    public void readPhoneState() {

        try {
            tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) ;
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            imei = tm.getDeviceId();
            uuid = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            serial_id = Build.getSerial();
        } else {
            serial_id = Build.SERIAL;
        }
        try {
            version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            Log.e("App Version : ", "" + version + " ( " + imei + "/" + serial_id + " )");
            text_ver.setText("App Version" + version + " ( " + imei + " )");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_forget_pass) {
            if (edit_user_name.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Enter User Name", Toast.LENGTH_SHORT).show();
            } else {
                new Requestforgetpassword().execute(edit_user_name.getText().toString() + "|" + imei );

            }
        } else if (v.getId() == R.id.button_login) {
            if (edit_user_name.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Enter User Name", Toast.LENGTH_SHORT).show();
            } else if (edit_pass.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
            } else {
                new LoginLoader().execute(edit_user_name.getText().toString() + "|" + edit_pass.getText().toString() + "|" + imei + "|" + Utiilties.getDateString("dd-MM-yyyy hh:mm:ss"));
            }
        }

    }

    class LoginLoader extends AsyncTask<String, Void, UserInformation> {
        private final ProgressDialog dialog1 = new ProgressDialog(getActivity());
        private final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        @Override
        protected void onPreExecute() {
            this.dialog1.setCanceledOnTouchOutside(false);
            this.dialog1.setMessage("Logging...");
            this.dialog1.show();
        }

        @Override
        protected UserInformation doInBackground(String... strings) {
            UserInformation userInfo2 = null;
            String res = "";
            if (Utiilties.isOnline(getActivity())) {
                String result = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    result = WebHandler.callByPostwithoutparameter(Urls_this_pro.LOG_IN_URL);
                }
                if (result != null) {
                    userInfo2 = WebServiceHelper.loginParser(result);
                }
                return userInfo2;
            } else {
                userInfo2 = CommonPref.getUserDetails(getActivity());
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
                if (result == null || !result.getAuthenticated()) {
                    alertDialog.setTitle("Failed");
                    alertDialog.setMessage("No result found ! try Again !!");
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            edit_user_name.setFocusable(true);
                        }
                    });
                    alertDialog.show();
                } else {
                    Intent cPannel = new Intent(getActivity(), MainActivity.class);
                    if (Utiilties.isOnline(getActivity())) {
                        if (result != null) {
                            if (imei.equalsIgnoreCase(result.getImeiNo())) {
                                try {
                                    result.setPassword(edit_pass.getText().toString());
                                    GlobalVariables.LoggedUser = result;
                                    CommonPref.setUserDetails(getActivity(), result);
                                    startActivity(cPannel);
                                    getActivity().finish();

                                } catch (Exception ex) {
                                    Toast.makeText(getActivity(), "Login failed due to Some Error !", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                alertDialog.setTitle("Device Not Registered");
                                alertDialog.setMessage("Sorry, your device is not registered!.\r\nPlease contact your Admin.");
                                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        edit_user_name.setFocusable(true);
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                    } else {
                        if (CommonPref.getUserDetails(getActivity()) != null) {
                            GlobalVariables.LoggedUser = result;
                            if (GlobalVariables.LoggedUser.getUserID().equalsIgnoreCase(edit_user_name.getText().toString().trim()) && GlobalVariables.LoggedUser.getPassword().equalsIgnoreCase(edit_pass.getText().toString().trim())) {
                                startActivity(cPannel);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "User name and password not matched !", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please enable internet connection for first time login.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String reqString(String req_string) {
        byte[] chipperdata = Utiilties.rsaEncrypt(req_string.getBytes(), (AppCompatActivity) getActivity());
        Log.e("chiperdata", new String(chipperdata));
        String encString = android.util.Base64.encodeToString(chipperdata, Base64.NO_WRAP);//.getEncoder().encodeToString(chipperdata);
        encString = encString.replaceAll("\\/", "SSLASH").replaceAll("\\=", "EEQUAL").replaceAll("\\+", "PPLUS");
        return encString;
    }
    class Requestforgetpassword extends AsyncTask<String,Void,String>{
        private final ProgressDialog dialog =new ProgressDialog(getActivity());
        private final AlertDialog alertdialog=new AlertDialog.Builder(getActivity()).create();

        @Override
        protected void onPreExecute() {
            this.dialog.setCancelable(false);
            this.dialog.setMessage("Processing....");
            this.dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result=null;
            if(Utiilties.isOnline(getActivity())){
                result=WebHandler.callByPost(strings[0],Urls_this_pro.FORGET_PASSWORD_URL);

            }else{
                Toast.makeText(getActivity(), "Please be Online for this service", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(this.dialog.isShowing()){
                this.dialog.cancel();

            }
            if(s!=null){
             alertdialog.setMessage(s.toString());
             alertdialog.show();
            }
        }
    }
}
