package com.example.skra.filebrowser2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Skóra on 2015-11-16.
 */
public class ListAdapter extends BaseAdapter {
    private final Context context;
    public List<FileInfo> values;
    private static LayoutInflater inflater = null;
    String path;

    public ListAdapter(Context context, List<FileInfo> values) {
        //super(context, R.layout.row);
        this.context = context;
        this.values = values;
        this.path = path;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return values.get(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return values.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.listtextView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        FileInfo item = values.get(position);

        //Ustawainie powrotu jeśli jesteśmy głębiej
        if(item.getName().equals("/.."))
        {
            textView.setText("/..");
            return rowView;
        }



        textView.setText(item.getName());
        if (item.isDirectory())
        {
            imageView.setImageResource(R.drawable.folder_icon);
        }
        else
        {
            imageView.setImageResource(R.drawable.svg_icon);
        }
        /*textView.setText(values.get(position).getModel());
        if (values.get(position).getPhotoId()!=0) {
            imageView.setImageResource(values.get(position).getPhotoId());
        }
        if (values.get(position).getBitmapId() > (-1))
        {
            for (int i=0; i<bitmap_id.size(); i++)
            {
                if (position == bitmap_id.get(i).getId()) imageView.setImageBitmap(bitmap_id.get(i).getBitmap());
            }
        }

        ratingBar.setRating(values.get(position).getRating());
        // change the icon for Windows and iPhone*/

        return rowView;
    }
}
