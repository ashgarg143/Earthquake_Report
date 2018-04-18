package com.example.ashis.earthquake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.android.earthquake.R;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import static java.security.AccessController.getContext;

/**
 * Created by ashis on 26-03-2018.
 */

public class EarthquakeAdapter extends ArrayAdapter<com.example.ashis.earthquake.Earthquake> {
    public EarthquakeAdapter(Context context, ArrayList<com.example.ashis.earthquake.Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }


        TextView textView = listItemView.findViewById(R.id.magnitude);
        TextView textView1 = listItemView.findViewById(R.id.location);
        TextView textView4 = listItemView.findViewById(R.id.place);
        TextView textView2 = listItemView.findViewById(R.id.date);
        TextView textView3 = listItemView.findViewById(R.id.time);


        com.example.ashis.earthquake.Earthquake currentEarthquake = getItem(position);
        Double magnitude = currentEarthquake.getEMagnitude();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String mag = decimalFormat.format(magnitude);

        Date date = new Date(currentEarthquake.getTimeInMilliseconds());
        SimpleDateFormat dateToDisplay = new SimpleDateFormat("MMM dd, yyyy");
        textView2.setText(dateToDisplay.format(date));
        textView3.setText(new SimpleDateFormat("hh:mm a").format(date));
        textView.setText(mag);
        String loc = currentEarthquake.getLocation();
        if (loc.contains("of")) {
            String[] string = loc.split(" of ");
            textView1.setText(string[0] + " of");
            textView4.setText(string[1]);
        } else {
            textView1.setText(getContext().getString(R.string.near_the));
            textView4.setText(loc);
        }

        GradientDrawable magnitudeCircle = (GradientDrawable) textView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getEMagnitude());
        magnitudeCircle.setColor(magnitudeColor);
        return listItemView;

    }

    private int getMagnitudeColor(Double magnitude) {
        int colorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                colorResourceId = R.color.magnitude1;
                break;
            case 2:
                colorResourceId = R.color.magnitude2;
                break;
            case 3:
                colorResourceId = R.color.magnitude3;
                break;
            case 4:
                colorResourceId = R.color.magnitude4;
                break;
            case 5:
                colorResourceId = R.color.magnitude5;
                break;
            case 6:
                colorResourceId = R.color.magnitude6;
                break;
            case 7:
                colorResourceId = R.color.magnitude7;
                break;
            case 8:
                colorResourceId = R.color.magnitude8;
                break;
            case 9:
                colorResourceId = R.color.magnitude9;
                break;
            default:
                colorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), colorResourceId);

    }
}
