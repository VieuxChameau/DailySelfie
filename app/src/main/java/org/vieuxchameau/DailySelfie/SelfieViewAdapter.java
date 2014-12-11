package org.vieuxchameau.DailySelfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class SelfieViewAdapter extends BaseAdapter {
	private static final int THUMBNAIL_WIDTH = 160;
	private static final int THUMBNAIL_HEIGHT = 120;
	private LinkedList<Selfie> selfies = new LinkedList<>();
	private LayoutInflater inflater;
	private Context context;

	public SelfieViewAdapter(final Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return selfies.size();
	}

	@Override
	public Object getItem(final int index) {
		return selfies.get(index);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int index, final View convertView, final ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.selfie_row, null);
			holder = new ViewHolder();
			holder.selfieThumbnail = (ImageView) rowView.findViewById(R.id.selfieThumbnail);
			holder.selfieDay = (TextView) rowView.findViewById(R.id.selfieDay);
			holder.selfieHour = (TextView) rowView.findViewById(R.id.selfieHour);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		final Selfie selfie = selfies.get(index);
		holder.selfieThumbnail.setImageBitmap(getThumbnail(selfie.getBitmapPath()));
		holder.selfieDay.setText(selfie.getDay());
		holder.selfieHour.setText(selfie.getHour());

		return rowView;
	}

	private Bitmap getThumbnail(final String bitmapPath) {
		// Get the dimensions of the View

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(bitmapPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / THUMBNAIL_WIDTH, photoH / THUMBNAIL_HEIGHT);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		return BitmapFactory.decodeFile(bitmapPath, bmOptions);
	}

	public void addAll(final List<Selfie> selfies) {
		this.selfies.addAll(selfies);
	}

	public void add(final Selfie selfie) {
		selfies.addFirst(selfie);
		notifyDataSetChanged();
	}

	private static class ViewHolder {
		ImageView selfieThumbnail;
		TextView selfieDay;
		TextView selfieHour;
	}
}
