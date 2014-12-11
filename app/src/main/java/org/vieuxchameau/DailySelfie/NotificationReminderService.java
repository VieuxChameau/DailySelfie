package org.vieuxchameau.DailySelfie;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Service to handle the alarm and create the notification
 */
public class NotificationReminderService extends IntentService {

	private static final String TAG = "NotificationReminderService";

	public NotificationReminderService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.d(TAG, "Handle alarm");
		final Intent launchMainActivityIntent = new Intent(this, MainActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchMainActivityIntent, 0);

		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, getNotification(pendingIntent));
	}

	private Notification getNotification(final PendingIntent pendingIntent) {
		return new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_action_camera)
				.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.selfieNotificationDescription))
				.setAutoCancel(true)
				.setContentIntent(pendingIntent).build();
	}
}
