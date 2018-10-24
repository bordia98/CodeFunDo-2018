package com.example.d_plan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class center_view_adapter extends ArrayAdapter<Local_help>{
    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public center_view_adapter(Context context, int layoutResourceId) {
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

        final Local_help currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }


        TextView gname  = (TextView) row.findViewById(R.id.group_name);
        TextView curr = (TextView) row.findViewById(R.id.curr);
        TextView max  = (TextView) row.findViewById(R.id.max);
        TextView id = (TextView) row.findViewById(R.id.id);
        TextView place = (TextView) row.findViewById(R.id.location);
        TextView auth = (TextView) row.findViewById(R.id.status);
        try {
            assert currentItem != null;
            gname.setText(currentItem.getText_name());
            curr.setText(currentItem.getText_currentcapacity()+"");
            max.setText(currentItem.getText_maxcapacity()+"");
            place.setText(currentItem.getPlace());
            if(currentItem.getAuth()==false){
                auth.setText("NO");
            }
            else{
                auth.setText("YES");
            }
            id.setText(currentItem.getId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return row;
    }

}
