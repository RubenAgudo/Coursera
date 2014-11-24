package org.coursera.ragudo.dailyselfie;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final String APP_NAME = "DailySelfie";
	private ListView list;
	private ListAdapter adapter;
	private File workingDirectory;
	
	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private PendingIntent mNotificationReceiverPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = (ListView) findViewById(R.id.list);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Photo toShow = (Photo) adapter.getItem(position);
				
				//expand the image maybe in another activity
				Intent i = new Intent(getApplicationContext(), ViewSelfie.class);
				i.putExtra("path", workingDirectory.getAbsolutePath() + 
						File.separator + toShow.getImageName());
				startActivity(i);
				
			}
			
			
		});
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setCancelable(true)
				.setMessage(getString(R.string.dialog_remove))
				.setTitle(R.string.dialog_remove_title)
				.setPositiveButton(getString(android.R.string.yes), new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Photo toRemove = adapter.remove(position);
						File fileToRemove = new File(workingDirectory.getAbsolutePath() + 
								File.separator + toRemove.getImageName());
						fileToRemove.delete();
						setupListView();
						dialog.dismiss();
					}
				})
				.setNegativeButton(android.R.string.no, null)
				.show();
				
				
				return false;
			}
		});
		
		
		workingDirectory = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), APP_NAME);
		setupWorkingDirectory();
		setupListView();
		
		setupAlarm();
		
	}

	private void setupAlarm() {
		// Get the AlarmManager Service
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		// Create an Intent to broadcast to the AlarmNotificationReceiver
		mNotificationReceiverIntent = new Intent(MainActivity.this,
				AlarmNotificationReceiver.class);

		// Create an PendingIntent that holds the NotificationReceiverIntent
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
				MainActivity.this, 0, mNotificationReceiverIntent, 0);
		
		// Set inexact repeating alarm
		mAlarmManager.setInexactRepeating(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
				INITIAL_ALARM_DELAY,
				mNotificationReceiverPendingIntent);
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
		
		switch(id) {
		case R.id.camera_shortcut:
			takePic();
			break;
		case R.id.remove_all:
			removeAllSelfies();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void removeAllSelfies() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setCancelable(true)
		.setMessage(getString(R.string.dialog_remove))
		.setTitle(R.string.dialog_remove_title)
		.setPositiveButton(getString(android.R.string.yes), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				deleteRecursive(workingDirectory);
				setupWorkingDirectory();
				setupListView();
				dialog.dismiss();
			}
			
			private void deleteRecursive(File fileOrDirectory) {
			    if (fileOrDirectory.isDirectory()) {
			        for (File child : fileOrDirectory.listFiles()) {
			            deleteRecursive(child);
			        }
			    }

			    fileOrDirectory.delete();
			}
			
		})
		.setNegativeButton(android.R.string.no, null)
		.show();
		
	}

	private void takePic() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri picUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		
		i.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
		
		if(i.resolveActivity(getPackageManager()) != null) {
			
			startActivityForResult(i, MEDIA_TYPE_IMAGE);
			
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode != RESULT_OK) return;
		
		if (requestCode == MEDIA_TYPE_IMAGE) {
	        
            setupListView();
	        
	    }

		
	}
	
	private void setupListView() {
		adapter = new ListAdapter(getApplicationContext(), workingDirectory);
		
		String[] photos = workingDirectory.list();
		
		for(String photo : photos) {
			adapter.add(photo);
		}
		
		list.setAdapter(adapter);
	}

	private File getOutputMediaFile(int type) {
		
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return null;
		
		
		
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		
		File mediaFile = null;
		
		if(type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(workingDirectory.getAbsolutePath() + File.separator + 
					"IMG_"+ timeStamp +".jpg");
		} 
		
		return mediaFile;
		
	}

	private void setupWorkingDirectory() {
		if(!workingDirectory.exists()) {
			
			if(!workingDirectory.mkdirs()) {
				Log.e(getPackageName(), "Directory not created");
			}
			
		}
	}
	
	private Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

}
