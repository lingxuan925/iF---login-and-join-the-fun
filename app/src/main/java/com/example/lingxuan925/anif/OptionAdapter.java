package com.example.lingxuan925.anif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OptionAdapter extends ArrayAdapter<Option> {
    private final int resourceId;
    private ArrayList<Option> arraylist;

    public OptionAdapter(Context context, int viewResourceId, ArrayList<Option> options) {
        super(context, viewResourceId, options);
        resourceId = viewResourceId;
        arraylist = options;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Option option = (Option) getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView optionImage = (ImageView)view.findViewById(R.id.option_image);
        TextView optionText = (TextView)view.findViewById(R.id.option_name);
        if (option != null) {
            optionImage.setImageResource(option.getImageId());
            optionText.setText(option.getName());
        }
        return view;
    }

    public void refreshList(ArrayList<Option> options){
        arraylist = options;
        notifyDataSetChanged();
    }
}
