package com.example.priyankam.dynamicform;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<String> UserIDList = new ArrayList<>();
    private ArrayList<String> UserNameList = new ArrayList<>();
    private ArrayList<String> UserEmailList = new ArrayList<>();

    public CustomAdapter(Context context, ArrayList<String> userIDList, ArrayList<String> userNameList, ArrayList<String> userEmailList) {

        super();
        this.context = context;
        this.UserIDList = userIDList;
        this.UserNameList = userNameList;
        this.UserEmailList = userEmailList;
    }

    @Override
    public int getCount() {
        return UserIDList.size();
    }

    @Override
    public Object getItem(int position) {
        String itemName = UserIDList.get(position);
        return itemName;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_adapter_row, null);
            holder = new ViewHolder();
            holder.tvUserName = (TextView) convertView.findViewById(R.id.nametextView);
            holder.tvUserEmail = (TextView) convertView.findViewById(R.id.emailtextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvUserName.setText("" + UserNameList.get(position));
        holder.tvUserEmail.setText("" + UserEmailList.get(position));

        return convertView;
    }

    public class ViewHolder {
        TextView tvUserName;
        TextView tvUserEmail;

    }
}
