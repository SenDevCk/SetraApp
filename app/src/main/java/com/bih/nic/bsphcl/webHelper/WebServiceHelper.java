package com.bih.nic.bsphcl.webHelper;


import android.util.Log;

import com.bih.nic.bsphcl.model.UserInformation;
import com.bih.nic.bsphcl.model.Versioninfo;

import org.json.JSONObject;

/**
 * Created by Chandan Singh on 12-06-2018.
 */
public class WebServiceHelper {

    public static Versioninfo CheckVersion(String res) {
        Versioninfo versioninfo = null;
        try {
            JSONObject jsonObject = new JSONObject(res);
            versioninfo = new Versioninfo();
            versioninfo.setAdminMsg(jsonObject.getString("adminMsg"));
            versioninfo.setAdminTitle(jsonObject.getString("adminTitle"));
            versioninfo.setUpdateMsg(jsonObject.getString("updateMsg"));
            versioninfo.setUpdateTile(jsonObject.getString("updateTitle"));
            versioninfo.setAppUrl(jsonObject.getString("appUrl"));
            versioninfo.setRole(jsonObject.getString("role"));
            versioninfo.setAppversion(jsonObject.getString("version"));
            versioninfo.setPriority(jsonObject.getString("priority"));
            versioninfo.setVerUpdated(jsonObject.getBoolean("isUpdated"));
            versioninfo.setValidDevice(jsonObject.getBoolean("isValidDev"));
        } catch (Exception e) {
            e.printStackTrace();
            //Utiilties.writeIntoLog(Log.getStackTraceString(e));
            return null;
        }
        return versioninfo;
    }

    public static UserInformation loginParser(String res) {
        UserInformation userInfo2 = null;
        try {

            JSONObject jsonObject = new JSONObject(res);
            userInfo2 = new UserInformation();
            if (jsonObject.has("msgStr"))
                userInfo2.setMessageString(jsonObject.getString("msgStr"));
            if (jsonObject.has("userId")) userInfo2.setUserID(jsonObject.getString("userId"));
            if (jsonObject.has("userName")) userInfo2.setUserName(jsonObject.getString("userName"));
            if (jsonObject.has("isAuthenticated"))
                userInfo2.setAuthenticated(jsonObject.getBoolean("isAuthenticated"));
            if (jsonObject.has("imei"))userInfo2.setImeiNo(jsonObject.getString("imei"));
            if (jsonObject.has("role"))userInfo2.setRole(jsonObject.getString("role"));
            if (jsonObject.has("subDivision"))userInfo2.setSubDivision(jsonObject.getString("subDivision"));
            if (jsonObject.has("contactNo"))userInfo2.setContactNo(jsonObject.getString("contactNo"));
            if (jsonObject.has("email"))userInfo2.setEmail(jsonObject.getString("email"));
            if (jsonObject.has("division"))userInfo2.setDivision(jsonObject.getString("division"));
        } catch (Exception e) {
            e.printStackTrace();
            //Utiilties.writeIntoLog(Log.getStackTraceString(e));
            return null;
        }
        return userInfo2;
    }


}
