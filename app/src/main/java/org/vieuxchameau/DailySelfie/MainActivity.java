package org.vieuxchameau.DailySelfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.vieuxchameau.DailySelfie.utils.SelfieFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO : maven or graddle
 * TODO : lib for thumbnail
 * TODO : add tests
 * TODO : swipe to delete
 * TODO : delete selection(s)
 * TODO : swipe to view in detail view + toast for file
 * TODO : boot alarm
 * TODO : layout notification view
 * TODO : external review
 * TODO : app in store
 */
public class MainActivity extends ListActivity {
	private static final String TAG = "MainActivity";
	private static final int REQUEST_TAKE_PHOTO = 42;
	private static final FileFilter SELFIE_FILE_FILTER = new SelfieFileFilter();
	private static final int TWO_MIN_IN_MILLIS = 2 * 60 * 1000;
	public static final String SELFIE_EXTENSION = ".jpg";
	public static final String SELFIE_DATE_FORMAT = "yyyyMMdd_HHmmss";
	public static final String SELFIE_PATH = "SELFIE_PATH";

	private String mCurrentPhotoPath;
	private SelfieViewAdapter adapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		adapter = new SelfieViewAdapter(getApplicationContext());
		setListAdapter(adapter);

		final ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				final Intent viewSelfieIntent = new Intent(MainActivity.this, DisplaySelfieActivity.class);
				final Selfie selfie = (Selfie) adapter.getItem(position);
				viewSelfieIntent.putExtra(SELFIE_PATH, selfie.getBitmapPath());
				startActivity(viewSelfieIntent);
			}
		});

		loadExistingSelfies();
		setAlarmNotification();
	}

	/**
	 * Enable the alarm notification every two minutes
	 */
	private void setAlarmNotification() {
		final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		final Intent launchService = new Intent(this, NotificationReminderService.class);
		final PendingIntent pendingIntent = PendingIntent.getService(this, 0, launchService, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), TWO_MIN_IN_MILLIS, pendingIntent);
	}

	/**
	 * Looking for existing selfies on application directory
	 */
	private void loadExistingSelfies() {
		final File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		final List<Selfie> selfies = new LinkedList<>();
		for (File selfieFile : storageDir.listFiles(SELFIE_FILE_FILTER)) {
			Log.d(TAG, "Found a selfieFile " + selfieFile);
			selfies.add(new Selfie(selfieFile.getAbsolutePath()));
		}
		Collections.sort(selfies);
		adapter.addAll(selfies);
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			adapter.add(new Selfie(mCurrentPhotoPath));
			mCurrentPhotoPath = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_selfie) {
			dispatchTakePictureIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Start the camera
	 */
	private void dispatchTakePictureIntent() {
		final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			final File photoFile = createImageFile();
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	/**
	 * Create the target file for the selfie
	 */
	private File createImageFile() {
		// Create an image file name
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SELFIE_DATE_FORMAT);
		simpleDateFormat.setLenient(false);
		final String timeStamp = simpleDateFormat.format(new Date()) + "_";
		final File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		try {
			final File pictureFile = File.createTempFile(timeStamp,
					SELFIE_EXTENSION,
					storageDir);
			mCurrentPhotoPath = pictureFile.getAbsolutePath();
			Log.d(TAG, "Create file : " + mCurrentPhotoPath);
			return pictureFile;
		} catch (IOException e) {
			Log.e(TAG, "Cannot create file", e);
			return null;
		}
	}
}
