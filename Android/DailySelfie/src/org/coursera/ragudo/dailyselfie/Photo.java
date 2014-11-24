package org.coursera.ragudo.dailyselfie;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;

public class Photo {

	private String path;
	
	public Photo(String path) {
		this.path = path;
	}
	
	public Drawable getThumbnail(Context context, File workingDirectory) {
		Bitmap bm = BitmapFactory.decodeFile(workingDirectory.getAbsolutePath() + File.separator + path);
		Drawable dr = new BitmapDrawable(context.getResources(), 
				Bitmap.createScaledBitmap(bm, 150, 150, true));
//		dr.setBounds(50, 50, 50, 50);
		return dr;
		
	}
	
	public String getImageName() {
		return path;
	}
}
