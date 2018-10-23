package com.example.d_plan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class user_disaster_adapter extends ArrayAdapter<Disaster_List> {
    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public user_disaster_adapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Disaster_List currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }


        TextView dtype = (TextView) row.findViewById(R.id.dtype);
        TextView dloc  = (TextView) row.findViewById(R.id.dloc);
        TextView id = (TextView) row.findViewById(R.id.did);
        try {
            assert currentItem != null;
            dtype.setText(currentItem.getText_name());
            dloc.setText(currentItem.getText_place());
            id.setText(currentItem.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        dtype.setTag(position);
        dtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
            }
        });
        return row;
    }

}