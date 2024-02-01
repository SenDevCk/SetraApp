package com.bih.nic.bsphcl.setraapp;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bih.nic.bsphcl.databaseHandler.DataBaseHelper;
import com.bih.nic.bsphcl.model.Versioninfo;
import com.bih.nic.bsphcl.utilitties.CommonPref;
import com.bih.nic.bsphcl.utilitties.MarshmallowPermission;
import com.bih.nic.bsphcl.utilitties.Urls_this_pro;
import com.bih.nic.bsphcl.utilitties.Utiilties;
import com.bih.nic.bsphcl.utilitties.WebHandler;
import com.bih.nic.bsphcl.webHelper.WebServiceHelper;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import pl.tajchert.waitingdots.DotsTextView;
public class SplashActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    String version;
    TelephonyManager tm;
    private static String imei;
    MarshmallowPermission MARSHMALLOW_PERMISSION;
    DotsTextView dotsTextView;
    private boolean initse = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dotsTextView=(DotsTextView)findViewById(R.id.dots);
        dotsTextView.setText("Please Wait ...");
        dotsTextView.setTextSize(15);
        dotsTextView.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Call some material design APIs here
            if (checkAndRequestPermissions()) {
                init2();
            }

        } else {
            // Implement this feature without material design
            init2();
        }

    }
    private void init2(){
        initse=true;
        if(Utiilties.isEmulator() ||  Utiilties.isDebugConnected()){
            Toast.makeText(this, "Virtual Device Not Allowed !", Toast.LENGTH_SHORT).show();
            dotsTextView.setText("Virtual Device Not Allowed !");
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                finish();
            },600);
        }else {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            },1000);
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Call some material design APIs here
            if (checkAndRequestPermissions()) {
                init();
            }

        } else {
            // Implement this feature without material design
            init();
        }*/
    }

    @SuppressLint("MissingPermission")
    private void init() {
        DataBaseHelper db = new DataBaseHelper(this);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;

        }

        try {
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null)
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
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
            //imei = "861878037876718";
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            //Log.e("App Version : ", "" + version + " ( " + imei + " )");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        checkOnline();
    }



    private boolean checkAndRequestPermissions() {
        int read_phone_state = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int read_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int receve_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int write_ex_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_ex_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (read_phone_state != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (write_ex_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read_ex_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (read_sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (receve_sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!initse) init2();
                } else {
                    if (!initse) init2();
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    private class CheckUpdate extends AsyncTask<String, Void, Versioninfo> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Versioninfo doInBackground(String... Params) {
            MARSHMALLOW_PERMISSION = new MarshmallowPermission(SplashActivity.this, android.Manifest.permission.READ_PHONE_STATE);
            if (MARSHMALLOW_PERMISSION.result == -1 || MARSHMALLOW_PERMISSION.result == 0) {
                try {
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    imei = tm.getDeviceId();
                    version = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    imei = tm.getDeviceId();
                    version = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String res=null;

            try{
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("userId",""+ CommonPref.getUserDetails(SplashActivity.this).getUserID());
                jsonObject.accumulate("passWord",""+version);
                jsonObject.accumulate("imeiNo",""+CommonPref.getUserDetails(SplashActivity.this).getImeiNo());
                jsonObject.accumulate("query","NSC");
                res= WebHandler.callByPostwithoutparameter(Urls_this_pro.NSC_OUIRY_VERSION+ URLEncoder.encode(String.valueOf(imei+"|"+version), "UTF-8"));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            Versioninfo versioninfo = WebServiceHelper.CheckVersion(res);
            return versioninfo;
        }

        @Override
        protected void onPostExecute(final Versioninfo versioninfo) {

            final AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
            ab.setCancelable(false);
            if (versioninfo != null) {
                CommonPref.setCheckUpdate(getApplicationContext(), System.currentTimeMillis());
                if (versioninfo.isVerUpdated() == false) {
                    ab.setTitle(versioninfo.getAdminTitle());
                    ab.setMessage(Html.fromHtml(versioninfo.getAdminMsg()));
                    ab.setPositiveButton("OK",
                            (dialog, whichButton) -> {
                                dialog.dismiss();
                                showDailog(ab, versioninfo);

                            });
                    ab.setNegativeButton("Ignore", (dialog, which) -> {
                        dialog.dismiss();
                        start();
                    });

                    ab.show();
                } else if (versioninfo.isValidDevice() == false) {
                    showDailog(ab, versioninfo);
                } else {
                    showDailog(ab, versioninfo);
                }
            }

        }
    }

    protected void checkOnline() {
        // TODO Auto-generated method stub
        super.onResume();

        if (Utiilties.isOnline(SplashActivity.this)) {
            new CheckUpdate().execute();
        } else {

            AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
            ab.setMessage("Internet Connection is not avaliable. Please Turn ON Network Connection OR Continue With Off-line Mode.");
            ab.setPositiveButton("Turn On Network Connection", (dialog, whichButton) -> {
                Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(I);
                //new CheckUpdate().execute();
            });

            ab.setNegativeButton("Continue Offline", (dialog, whichButton) -> start());

            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
            ab.show();
        }
    }
    private void start(){
        if(Utiilties.isEmulator()) {
            Toast.makeText(this, "You Are Using Emulator !", Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(getApplicationContext(), LoginActivity2.class);
            startActivity(i);
            SplashActivity.this.finish();
        }
    }

    private void showDailog(AlertDialog.Builder ab,
                            final Versioninfo versioninfo) {

        if (!versioninfo.isVerUpdated()) {

            if (versioninfo.getPriority().equals("0")) {

                start();
            } else if (versioninfo.getPriority().endsWith("1")) {

                ab.setTitle(versioninfo.getUpdateTile());
                ab.setMessage(versioninfo.getUpdateMsg());

                ab.setPositiveButton("Update",
                        (dialog, whichButton) -> {
                            Intent myWebLink = new Intent(
                                    Intent.ACTION_VIEW);
                            myWebLink.setData(Uri.parse(versioninfo.getAppUrl()));
                            startActivity(myWebLink);
                            dialog.dismiss();
                        });
                ab.setNegativeButton("Ignore",
                        (dialog, whichButton) -> {

                            dialog.dismiss();

                            start();
                        });

                ab.show();

            } else if (versioninfo.getPriority().equals("2")) {
                ab.setTitle(versioninfo.getUpdateTile());
                ab.setMessage(versioninfo.getUpdateMsg());
                ab.setPositiveButton("Update",
                        (dialog, whichButton) -> {
                            Intent myWebLink = new Intent(Intent.ACTION_VIEW);
                            myWebLink.setData(Uri.parse(versioninfo.getAppUrl()));
                            startActivity(myWebLink);
                            dialog.dismiss();

                        });

                ab.show();
            }
        } else {
            start();
        }
    }
    @Override
    protected void onDestroy() {
        dotsTextView.hideAndStop();
        super.onDestroy();

    }
}