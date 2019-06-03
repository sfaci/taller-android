package com.sfaci.contacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfaci.contacts.R;
import com.sfaci.contacts.domain.Event;
import com.sfaci.contacts.util.NumberUtils;

import java.util.List;

/**
 * Adaptador para listar los eventos
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class EventAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int idLayout;
    private List<Event> events;

    public EventAdapter(Context contexto, int idLayout,
                        List<Event> events) {

        inflater = LayoutInflater.from(contexto);
        this.idLayout = idLayout;
        this.events = events;
    }

    static class ViewHolder {
        ImageView ivPicture;
        TextView tvName;
        TextView tvDescription;
        TextView tvPrice;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(idLayout, null);

            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        Event event = events.get(posicion);
        holder.tvName.setText(event.getName());
        holder.tvDescription.setText(event.getDescription());
        holder.tvPrice.setText(NumberUtils.toMoney(event.getPrice()));

        return convertView;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int posicion) {
        return events.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return events.get(posicion).getId();

    }
}
