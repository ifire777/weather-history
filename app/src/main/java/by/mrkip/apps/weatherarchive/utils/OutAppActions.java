package by.mrkip.apps.weatherarchive.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;

import by.mrkip.apps.weatherarchive.App;
import by.mrkip.apps.weatherarchive.R;

//TODO singletone
public class OutAppActions {

	public static final String MAILTO = "mailto:";
	public static final String SUBJECT = "?subject=";
	public static final String BODY = "&body=";

	//TODO move to utils[+]
	public void gotoMail(Context context) {
		String mailTo = MAILTO + App.getAppContext().getString(R.string.app_support_mail) +
				SUBJECT + Uri.encode(App.getAppContext().getString(R.string.mail_head)) +
				BODY + Uri.encode(App.getAppContext().getString(R.string.mail_body));

		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		emailIntent.setData(Uri.parse(mailTo));

		try {
			context.startActivity(emailIntent);
		} catch (ActivityNotFoundException e) {
			Snackbar.make(null, App.getAppContext().getString(R.string.message_email_app_unevailable) + e.toString(), Snackbar.LENGTH_LONG)
					.setAction("", null).show();
		} catch (Exception e) {
			Snackbar.make(null, App.getAppContext().getString(R.string.message_error) + e.toString(), Snackbar.LENGTH_LONG)
					.setAction("", null).show();

		}

	}

}
