package com.example.angelia.term4androidappproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CustomSpinnerAdapter extends ArrayAdapter<String>  {

    Context context;
    String[] objects;
    String firstElement;
    LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, String[] objects, String setText, String defaultText, String defaults) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.firstElement = defaultText;

        setDefaultText(setText,defaultText,defaults);
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        objects[0] = firstElement;
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }

    public void setDefaultText(String setText, String defaultText, String defaults ) {
        this.firstElement = objects[0];
        Log.i("kim", String.valueOf(setText.toLowerCase().equals(defaultText.toLowerCase())));
        if (!setText.toLowerCase().equals(defaultText.toLowerCase())) {
            objects[0] = setText;
            Log.i("kim","if loop " + setText);
        }else{
            Log.i("kim","elseloop " + defaults);
            objects[0] = defaults;
        }
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_row, parent, false);
        TextView label = (TextView) row.findViewById(R.id.spinner_text);
        label.setText(objects[position]);
        return row;
    }

}
