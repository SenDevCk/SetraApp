package com.bih.nic.bsphcl.setraapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bih.nic.bsphcl.utilitties.Urls_this_pro;
import com.bih.nic.bsphcl.utilitties.Utiilties;
import com.bih.nic.bsphcl.utilitties.WebHandler;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
LinearLayout ll_scan,ll_mob_session,ll_session_list,ll_login_log,ll_verify,ll_profile;
    //qr code scanner object
    private IntentIntegrator qrScan;
    String model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrScan = new IntentIntegrator(this);
        init();
    }

    private void init() {
        ll_scan=(LinearLayout)findViewById(R.id.ll_scan);
        ll_mob_session=(LinearLayout)findViewById(R.id.ll_mob_session);
        ll_session_list=(LinearLayout)findViewById(R.id.ll_session_list);
        ll_login_log=(LinearLayout)findViewById(R.id.ll_login_log);
        ll_verify=(LinearLayout)findViewById(R.id.ll_verify);
        ll_profile=(LinearLayout)findViewById(R.id.ll_profile);
        ll_scan.setOnClickListener(this);
        ll_mob_session.setOnClickListener(this);
        ll_session_list.setOnClickListener(this);
        ll_login_log.setOnClickListener(this);
        ll_verify.setOnClickListener(this);
        ll_profile.setOnClickListener(this);
        ll_scan.setOnClickListener(this);
        model = Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
                //+ " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
    }

    @Override
    public void onClick(View v) {
            if(v.getId()==R.id.ll_scan){
                qrScan.initiateScan();
            }
    }
    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                Toast.makeText(this, ""+result, Toast.LENGTH_LONG).show();
                if (Utiilties.isOnline(MainActivity.this)) {
                    //new Varifier().execute();
                }else{
                    Toast.makeText(this, "No Internet Connection ! ", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class Varifier extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res1=null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                res1 = WebHandler.callByPostwithoutparameter( Urls_this_pro.LOG_IN_URL);
            }
            return res1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String reqString(String req_string) {
        byte[] chipperdata = Utiilties.rsaEncrypt(req_string.getBytes(),MainActivity.this);
        Log.e("chiperdata", new String(chipperdata));
        String encString = android.util.Base64.encodeToString(chipperdata, Base64.NO_WRAP);//.getEncoder().encodeToString(chipperdata);
        encString=encString.replaceAll("\\/","SSLASH").replaceAll("\\=","EEQUAL").replaceAll("\\+","PPLUS");
        return encString;
    }
}
