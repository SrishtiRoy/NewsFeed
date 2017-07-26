
package com.and.newsfeed.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class Util {

	 private static final String TAG="accesibility_service";

    public static void runOnUiThread(Runnable runnable){
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler .post(runnable);
    } 
    
   
    
   
	public static boolean haveNetworkConnection(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

			if (activeNetwork != null
					&& activeNetwork.isConnectedOrConnecting()) {
				return true;
			}

			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (NetworkInfo ni : netInfo) {
				if (ni.getType() == ConnectivityManager.TYPE_WIFI
						&& ni.isConnected())
					return true;
				if (ni.getType() == ConnectivityManager.TYPE_MOBILE
						&& ni.isConnected())
					return true;
			}
		} catch (Exception e) {
			//LoggerUtils.logWarn("Freecharge", Log.getStackTraceString(e));
		}
		return false;
	}
	  public static String getContactName(Context context, String phoneNumber) {
	        ContentResolver cr = context.getContentResolver();
	        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
	        Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
	        if (cursor == null) {
	            return null;
	        }
	        String contactName = null;
	        if(cursor.moveToFirst()) {
	            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
	           
	        }
		  else
			contactName=phoneNumber;
	       
	        if(cursor != null && !cursor.isClosed()) {
	            cursor.close();
	        }

	        return contactName;
	    }
	  public static String convertStringToDate(Date indate)
		{
		   String dateString = null;
		   SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
		   /*you can also use DateFormat reference instead of SimpleDateFormat 
		    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		    */
		   try{
			dateString = sdfr.format( indate );
		   }catch (Exception ex ){
			System.out.println(ex);
		   }
		   return dateString;
		}  
	  
	  public static String convertStringToDateTIme(Date indate)
		{
		   String dateString = null;
		   SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		   /*you can also use DateFormat reference instead of SimpleDateFormat 
		    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		    */
		   try{
			dateString = sdfr.format( indate );
		   }catch (Exception ex ){
			System.out.println(ex);
		   }
		   return dateString;
		}
	

	
	public static String getEmail(Context ctx)

    {
		String possibleEmail="";
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(ctx).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }
        return possibleEmail;

    }
}
