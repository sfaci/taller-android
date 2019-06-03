package com.sfaci.contacts.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfaci.contacts.R;
import com.sfaci.contacts.domain.Friend;

import java.util.ArrayList;

/**
 * Adaptador para los contactos de la agenda
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class FriendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Friend> friendsList;
    private LayoutInflater inflater;

    public FriendAdapter(Activity context, ArrayList<Friend> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        ImageView picture;
        TextView nameSurname;
        TextView mobile;
        TextView telephone;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        // Si la View es null se crea de nuevo
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fila, null);

            holder = new ViewHolder();
            holder.picture = convertView.findViewById(R.id.ivPicture);
            holder.nameSurname = convertView.findViewById(R.id.tvName);
            holder.telephone = convertView.findViewById(R.id.tvTelephone);
            holder.mobile = convertView.findViewById(R.id.tvMobile);

            convertView.setTag(holder);
        }
		/*
		 *  En caso de que la View no sea null se reutilizará con los
		 *  nuevos valores
		 */
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Friend friend = friendsList.get(position);
        holder.picture.setImageBitmap(friend.getPicture());
        holder.nameSurname.setText(friend.getName());
        holder.telephone.setText(friend.getTelephone());
        holder.mobile.setText(friend.getMobile());

        return convertView;
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int posicion) {
        return friendsList.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

}
