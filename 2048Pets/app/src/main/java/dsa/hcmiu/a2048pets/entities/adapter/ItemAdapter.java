package dsa.hcmiu.a2048pets.entities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import dsa.hcmiu.a2048pets.entities.model.Pets;
import dsa.hcmiu.a2048pets.R;

public class ItemAdapter extends ArrayAdapter<Pets> {
    private Context context;
    private ArrayList<Pets> array;
    private int layout;


    public ItemAdapter(Context context, int resource, ArrayList<Pets> objects) {
        super(context, resource, objects);
        this.context = context;
        array = objects;
        layout = resource;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater view = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = view.inflate(layout, null);
        }
        if (array.size() > 0) {
            Pets currentPet = new Pets(array.get(position));
            ImageView ivItem = (ImageView) convertView.findViewById(R.id.ivItem);
            ivItem.setImageResource(currentPet.getPic()); //no2.jpg = 123 => ivItem.setImageResource(123);
        }
        return convertView;
    }
}
 /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){  //Check NULL
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //Chuyen layout thanh 1 item
            convertView = inflater.inflate(R.layout.item_pet,null);
        }
        if(array.size() > 0){
            Square sq = (Square)convertView.findViewById(R.id.square); // Take ID from "item_pet" //
            sq.textFormat(array.get(position));
        }
        return convertView;
    }
    */
