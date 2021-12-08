package com.example.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeCustomAdapter extends ArrayAdapter {
    public static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeCustomAdapter(@NonNull Context context, ArrayList<Data> information) {
        super(context, 0, information);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        check if the existing view is being reused , otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_information_list, parent, false);
        }
//        get the Data object at current position
        final Data currentItem = (Data) getItem(position);


        TextView magnitudeView = listItemView.findViewById(R.id.magnitude_text_view);
        String formattedMagnitude = formatMagnitude(currentItem.getMagnitude());
        magnitudeView.setText(formattedMagnitude);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentItem.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        String originalLocation = currentItem.getPlace();
        String primaryLocation = "";
        String nearbyLocation = "";

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            nearbyLocation = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            nearbyLocation = getContext().getString(R.string.near_by);
            primaryLocation = originalLocation;
        }
        TextView nearby = listItemView.findViewById(R.id.nearby_text_view);
        nearby.setText(nearbyLocation);

        TextView place = listItemView.findViewById(R.id.places_text_view);
        place.setText(primaryLocation);

//      create a new dateObject using timeInMillisecond
        Date dateObject = new Date(currentItem.getTimeinMillisecond());

//      find textView with id date_text_view
        TextView dateView = listItemView.findViewById(R.id.date_text_view);

//      set the date of current earthquake in dateView textView
        dateView.setText(formatDate(dateObject));

//      find textView with id time_text_view
        TextView timeView = listItemView.findViewById(R.id.time_text_view);

//      set the time of current earthquake in timeView textView
        timeView.setText(formateTime(dateObject));

//      return the updated list item view that is showing the appropriate date
        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) magnitude;
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }

    //    return the formatted date as String
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MM, yyyy");
        return dateFormat.format(dateObject);
    }

    //    return the formatted time as String
    private String formateTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:a");
        return timeFormat.format(dateObject);
    }

    //    return the formatted magnitude String upto 1 decimal place (i:e "2.3")
    private String formatMagnitude(Double magnitude) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);

    }


}
