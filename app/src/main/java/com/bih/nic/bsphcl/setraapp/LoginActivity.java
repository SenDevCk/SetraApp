package com.bih.nic.bsphcl.setraapp;



import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);
        /* viewPager = (ViewPager) findViewById(R.id.pager);
        //setupViewPager(viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
       getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MpinFragment())
                .commit();*/

    }



    /*private class Varifier extends AsyncTask<String,Void,String> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Verifying...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
                res= WebHandler.callByPostwithoutparameter("", Urls_this_pro.LOG_IN_URL);
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())progressDialog.dismiss();
            if (s!=null){

            }
        }
    }*/

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
//        adapter.addFrag(new LoginFragment(), "Using User ID");
//        //adapter.addFrag(new MpinFragment(), "Using MPIN");
//        viewPager.setAdapter(adapter);
//    }
}
