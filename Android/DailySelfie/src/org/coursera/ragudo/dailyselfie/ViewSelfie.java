package org.coursera.ragudo.dailyselfie;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ViewSelfie extends Activity {
	
	private ImageView mImageView;
	private String mCurrentPhotoPath;
	private RelativeLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_selfie);
		
		mImageView = (ImageView) findViewById(R.id.imageView);
		mCurrentPhotoPath = getIntent().getStringExtra("path");
		
		layout = (RelativeLayout) findViewById(R.id.selfieLayout);
		
		ViewTreeObserver vto = layout.getViewTreeObserver();
		
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				
				layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				setPic();
			}
			
		});
	}
	
	private void setPic() {
		
	    // Get the dimensions of the View
	    int targetW = mImageView.getMeasuredWidth();
	    int targetH = mImageView.getMeasuredHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    mImageView.setImageBitmap(bitmap);
	}
	
	
}
