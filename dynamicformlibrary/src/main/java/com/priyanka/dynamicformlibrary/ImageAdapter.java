package com.priyanka.dynamicformlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyankam on 11-05-2017.
 */

public class ImageAdapter extends BaseAdapter {
    // Keep all Images in array
    public List<String> mAllImagesPath = new ArrayList<>();
    String mEditTextId;
    private Context mContext;

    // Constructor
    public ImageAdapter(Context context, String editTextId, List<String> allImagesPath) {
        mContext = context;
        mAllImagesPath = allImagesPath;
        mEditTextId = editTextId;
    }

    public int getCount() {

        return mAllImagesPath.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_single, null);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.grid_text);
            holder.imageView = convertView.findViewById(R.id.grid_image);
            holder.imageCancel = convertView.findViewById(R.id.image_cancel);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageCancel.setTag(position);//you can set the position to the button as a tag//important so we know which item to delete on button click

        holder.textView.setText("" + mAllImagesPath.get(position));


       /* Bitmap value = convertFileToBitmap(mAllImagesPath.get(position).toString());

       holder.imageView.setImageBitmap(value);*/

        String imagePath = mAllImagesPath.get(position);
        Picasso.with(mContext)
                .load(new File(imagePath))
                .noFade()
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.imageCancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                String valuePath = mAllImagesPath.get(position);
                // Remove the item on remove/button click
                file_delete(valuePath);

                CameraActivity.multiMapFileName.remove(mEditTextId, valuePath);

                int positionToRemove = (int) v.getTag(); //get the position of the view to delete stored in the tag

                mAllImagesPath.remove(positionToRemove);
                //if gridview is empty then deactivate the attach button
                if (mAllImagesPath.size() == 0) {
                    CameraActivity.buttonDeactivate();
                } else {
                    CameraActivity.buttonActivate();
                }
                notifyDataSetChanged();
            }
        });


        return convertView;
    }

    private Bitmap convertFileToBitmap(String fileName) {
        //  String filePath = Utility.FILE_PATH + "/" +fileName;
        File imgFile = new File(fileName);
        Bitmap myBitmap = null;
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }

    public void file_delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();
        }
        // Toast.makeText(mContext, "Image Deleted !", Toast.LENGTH_SHORT).show();
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
        ImageView imageCancel;
    }


}