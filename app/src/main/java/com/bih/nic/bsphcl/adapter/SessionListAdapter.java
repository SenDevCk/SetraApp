package com.bih.nic.bsphcl.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.bih.nic.bsphcl.model.SessionEntity;
import com.bih.nic.bsphcl.setraapp.LoginActivity;
import com.bih.nic.bsphcl.setraapp.R;

import java.util.ArrayList;

public class SessionListAdapter extends BaseAdapter{
    AppCompatActivity activity;
    ArrayList<SessionEntity> sessionEntities;
    LayoutInflater layoutInflater;
    public SessionListAdapter(AppCompatActivity activity, ArrayList<SessionEntity> sessionEntities){
        this.activity=activity;
        this.sessionEntities=sessionEntities;
        layoutInflater=activity.getLayoutInflater();
    }
    @Override
    public int getCount() {
        return sessionEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return sessionEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=layoutInflater.inflate(R.layout.session_list_item,null,false);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}

