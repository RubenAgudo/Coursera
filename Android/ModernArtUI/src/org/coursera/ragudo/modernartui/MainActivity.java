package org.coursera.ragudo.modernartui;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends Activity {

	private final String URL = "http://www.moma.org";
	private final int COLOR_CHANGEABLE_VIEWS = 5;
	private SeekBar seekBar;
	private Random rand;
	
	//Non white and Non gray views
	private View[] views;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//getting the view and attaching the listener
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				for(View view : views) {
					int r = rand.nextInt(256);
					int g = rand.nextInt(256);
					int b = rand.nextInt(256);
					
					String rr = Integer.toHexString(r);
					String gg = Integer.toHexString(g);
					String bb = Integer.toHexString(b);
					
					rr = adjustColor(rr);
					gg = adjustColor(gg);
					bb = adjustColor(bb);
					
					//view.getBackground().setColorFilter(Color.parseColor("#"+rr+gg+bb), PorterDuff.Mode.DARKEN);
					String color = "#FF"+rr+gg+bb;
					view.setBackgroundColor(Color.parseColor(color));
					view.invalidate();
				}
				
			}

			private String adjustColor(String color) {
				if(color.length() == 1) {
					color = "0" + color;
				}
				return color;
			}
		});
		
		views = new View[COLOR_CHANGEABLE_VIEWS];
		views[0] = findViewById(R.id.black_rectangle);
		views[1] = findViewById(R.id.holo_blue_bright_rectangle);
		views[2] = findViewById(R.id.holo_blue_dark_rectangle);
		views[3] = findViewById(R.id.holo_orange_dark_rectangle);
		views[4] = findViewById(R.id.holo_green_dark_rectangle);
		
		rand = new Random();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.more_info) {
			showDialog();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Method that creates the AlertDialog
	 * @return
	 */
	private AlertDialog createDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.dialog_title));
		dialog.setMessage(getString(R.string.dialog_description));
		dialog.setNegativeButton(getString(R.string.not_now), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//closing the dialog
				dialog.dismiss();
			}
		});
		
		dialog.setPositiveButton(getString(R.string.visit_moma), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Implictly call a browser
				PackageManager packageManager = getPackageManager();
				
				Intent baseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				List<ResolveInfo> activities = packageManager.queryIntentActivities(baseIntent, 0);
				if(activities.size() > 0) {
					startActivity(baseIntent);					
				}
			}
		});
		dialog.setCancelable(true);
		return dialog.create();
	}
	
	/**
	 * Method that creates and then shows the dialog
	 */
	private void showDialog() {
		AlertDialog dialog = createDialog();
		dialog.show();
	}
}
