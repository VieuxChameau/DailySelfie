package org.vieuxchameau.DailySelfie;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class DisplaySelfieActivity extends Activity {
	private static final String TAG = "DisplaySelfieActivity";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selfie_detail);

		final String selfiePath = getIntent().getStringExtra(MainActivity.SELFIE_PATH);
		Log.d(TAG, "Detail view of " + selfiePath);

		final ImageView selfieDetailView = (ImageView) findViewById(R.id.selfieDetail);
		selfieDetailView.setImageBitmap(BitmapFactory.decodeFile(selfiePath));
	}
}
