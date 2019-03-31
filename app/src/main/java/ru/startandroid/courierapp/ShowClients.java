package ru.startandroid.courierapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//Допоміжний клас для адаптера
public class ShowClients extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ClientsConstruct> objects;

    ShowClients(Context context, ArrayList<ClientsConstruct> client) {
        ctx = context;
        objects = client;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        ClientsConstruct construct = getClients(position);

        ((TextView) view.findViewById(R.id.name)).setText(construct.name);
        ((TextView) view.findViewById(R.id.pay)).setText(construct.pay);
        ((TextView) view.findViewById(R.id.notes)).setText(construct.note);

        return view;
    }

    ClientsConstruct getClients(int position) {
        return ((ClientsConstruct) getItem(position));
    }
}
