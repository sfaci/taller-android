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
import com.sfaci.contacts.domain.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para los contactos de la agenda
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class ContactAdapter extends BaseAdapter {

    private Context context;
    private List<Contact> contactsList;
    private LayoutInflater inflater;

    public ContactAdapter(Activity context, List<Contact> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        ImageView picture;
        TextView nameSurname;
        TextView mobile;
        TextView telephone;
    }

    /**
     * Refresca la lista del adaptador
     * @param contacts Lista actual de contactos
     */
    public void updateList(List<Contact> contacts) {
        contactsList.clear();
        contactsList.addAll(contacts);
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

        Contact contact = contactsList.get(position);
        holder.picture.setImageBitmap(contact.getPicture());
        holder.nameSurname.setText(contact.getName());
        holder.telephone.setText(contact.getTelephone());
        holder.mobile.setText(contact.getMobile());

        return convertView;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int posicion) {
        return contactsList.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

}
