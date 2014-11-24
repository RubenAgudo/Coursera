package org.coursera.ragudo.dailyselfie;

import java.io.File;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	private Context context;
	private LinkedList<Photo> photos;
	private File workingDirectory;
	
	public ListAdapter(Context pContext, File workingDirectory) {
		context = pContext;
		photos = new LinkedList<Photo>();
		this.workingDirectory = workingDirectory;
	}
	
	public void add(String path) {
		photos.add(new Photo(path));
	}
	
	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.list_row, parent, false);
            
           
        }
		
		Photo aPhoto = photos.get(position);
		
		TextView row = (TextView) convertView.findViewById(R.id.row);
		
		row.setText(aPhoto.getImageName());
		row.setCompoundDrawablesWithIntrinsicBounds(
				aPhoto.getThumbnail(context, workingDirectory), null, null, null);
		row.invalidate();
		
		return convertView;
	}

	public Photo remove(int position) {
		return photos.remove(position);
	}

}
