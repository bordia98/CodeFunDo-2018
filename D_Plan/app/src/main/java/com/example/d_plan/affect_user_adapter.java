package com.example.d_plan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class affect_user_adapter extends ArrayAdapter<Affected_Person> {/**
 * Adapter context
 */
Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public affect_user_adapter(Context context, int layoutResourceId) {
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

        final Affected_Person currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }


        TextView name  = (TextView) row.findViewById(R.id.person_name);
        TextView lat = (TextView) row.findViewById(R.id.lat);
        TextView longitude  = (TextView) row.findViewById(R.id.longi);
        TextView id = (TextView) row.findViewById(R.id.id);
        TextView place = (TextView) row.findViewById(R.id.location);
        TextView contactno = (TextView) row.findViewById(R.id.contactno);
        try {
            assert currentItem != null;
            name.setText(currentItem.getText_name());
            lat.setText(currentItem.getText_lat());
            longitude.setText(currentItem.getText_long());
            place.setText(currentItem.getPlace());
            contactno.setText(currentItem.getText_mob());
            id.setText(currentItem.getId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return row;
    }

}
