package com.bih.nic.bsphcl.utilitties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bih.nic.bsphcl.model.UserInformation;


/**
 * Created by CKS on 15/06/2018.
 */
public class CommonPref {

	static Context context;

	CommonPref() {

	}

	CommonPref(Context context) {
		CommonPref.context = context;
	}


	public static void setUserDetails(Context context, UserInformation UserInfo2) {

		String key = "_USER_DETAILS";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("MessageString", UserInfo2.getUserName());
		editor.putString("UserID", UserInfo2.getUserID());
		editor.putString("UserName", UserInfo2.getUserName());
		editor.putString("Password", UserInfo2.getPassword());
		editor.putBoolean("Authenticated", UserInfo2.getAuthenticated());
		editor.putString("ImeiNo", UserInfo2.getImeiNo());
		editor.putString("Role", UserInfo2.getRole());
		editor.putString("SubDivision", UserInfo2.getSubDivision());
		editor.putString("ContactNo", UserInfo2.getContactNo());
		editor.putString("Email", UserInfo2.getEmail());
		editor.putString("Division", UserInfo2.getDivision());
		editor.commit();
	}

	public static UserInformation getUserDetails(Context context) {
		String key = "_USER_DETAILS";
		UserInformation UserInfo2 = new UserInformation();
		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		UserInfo2.setMessageString(prefs.getString("MessageString", ""));
		UserInfo2.setUserID(prefs.getString("UserID", ""));
		UserInfo2.setUserName(prefs.getString("UserName", ""));
		UserInfo2.setAuthenticated(prefs.getBoolean("Authenticated", false));
		UserInfo2.setImeiNo(prefs.getString("ImeiNo", ""));
		UserInfo2.setPassword(prefs.getString("Password", ""));
		UserInfo2.setRole(prefs.getString("Role", ""));
		UserInfo2.setSubDivision(prefs.getString("SubDivision", ""));
		UserInfo2.setContactNo(prefs.getString("ContactNo", ""));
		UserInfo2.setEmail(prefs.getString("Email", ""));
		UserInfo2.setDivision(prefs.getString("Division", ""));
		return UserInfo2;
	}

	public static void setCheckUpdate(Context context, long dateTime) {

		String key = "_CheckUpdate";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();

		dateTime = dateTime + 1 * 3600000;
		editor.putLong("LastVisitedDate", dateTime);

		editor.commit();

	}
	public static void setPrinterMacAddress(Context context, String address) {

		String key = "_MAC_ADDRESS";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();

		editor.putString("MacAddress", address);
		editor.commit();

	}

	public static String getPrinterMacAddress(Context context) {

		String key = "_MAC_ADDRESS";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		String macAddress = prefs.getString("MacAddress", "");
		return macAddress;
	}

	public static void setPrinterType(Context context, String address) {

		String key = "P_Type";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();

		editor.putString("PType", address);
		editor.commit();

	}

	public static String getPrinterType(Context context) {

		String key = "P_Type";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		String Ptype = prefs.getString("PType", "");
		return Ptype;
	}
}
