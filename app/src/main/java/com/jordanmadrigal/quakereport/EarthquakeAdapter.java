package com.jordanmadrigal.quakereport;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Adapter to inflate each list view item
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake>{

    public EarthquakeAdapter(Activity context, List<Earthquake> earthquakes){
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_items,parent, false);
        }

        Earthquake currentQuake = getItem(position);

        TextView magTextView = listItemView.findViewById(R.id.mag);
        String formatMagnitude = formatDecimal(currentQuake.getMag());
        magTextView.setText(formatMagnitude);

        TextView cityTextView = listItemView.findViewById(R.id.location);
        cityTextView.setText(currentQuake.getCity());

        TextView subLocationTextView = listItemView.findViewById(R.id.subLocation);
        subLocationTextView.setText(currentQuake.getSubLocation());

        Date dateObj = new Date(currentQuake.getDate());

        //modify unix time to standard date
        TextView dateTextView = listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObj);
        dateTextView.setText(formattedDate);

        //modify unix time to standard time
        TextView timeTextView = listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObj);
        timeTextView.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = magColorPicker(currentQuake.getMag());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);



        return listItemView;
    }

    private String formatDate (Date unixTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(unixTime);
    }

    private String formatTime (Date unixTime){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(unixTime);
    }

    private String formatDecimal(double magnitude){
        DecimalFormat tenthFormat = new DecimalFormat("0.0");
        return tenthFormat.format(magnitude);
    }

    private int magColorPicker(double mag) {

        int magnitudeColor;

        switch ((int) mag) {
            case 1:
                magnitudeColor = R.color.magnitude1;
                break;
            case 2:
                magnitudeColor = R.color.magnitude2;
                break;
            case 3:
                magnitudeColor = R.color.magnitude3;
                break;
            case 4:
                magnitudeColor = R.color.magnitude4;
                break;
            case 5:
                magnitudeColor = R.color.magnitude5;
                break;
            case 6:
                magnitudeColor = R.color.magnitude6;
                break;
            case 7:
                magnitudeColor = R.color.magnitude7;
                break;
            case 8:
                magnitudeColor = R.color.magnitude8;
                break;
            case 9:
                magnitudeColor = R.color.magnitude9;
                break;
            default:
                magnitudeColor = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), magnitudeColor);
    }

}
