package com.bih.nic.bsphcl.setraapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.bih.nic.bsphcl.adapter.SessionListAdapter;
import com.bih.nic.bsphcl.model.SessionEntity;

import java.util.ArrayList;
import java.util.List;

public class SessionList extends AppCompatActivity {
ListView list_session;
ArrayList<SessionEntity> sessionEntities=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);
        list_session=(ListView)findViewById(R.id.list_session);
        sessionEntities.add(new SessionEntity());
        sessionEntities.add(new SessionEntity());
        sessionEntities.add(new SessionEntity());
        sessionEntities.add(new SessionEntity());
        list_session.setAdapter(new SessionListAdapter(SessionList.this,sessionEntities));
    }
}
