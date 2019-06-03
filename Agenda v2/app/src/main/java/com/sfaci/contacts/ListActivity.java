package com.sfaci.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.sfaci.contacts.adapters.ContactAdapter;
import com.sfaci.contacts.domain.Contact;

/**
 * Activity donde se listan todos los contactos
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class ListActivity extends Activity {

    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView lvContacts = findViewById(R.id.lvFriends);
        adapter = new ContactAdapter(this, MainActivity.contactsList);
        lvContacts.setAdapter(adapter);

        registerForContextMenu(lvContacts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_registrar:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_context_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info =
                (AdapterContextMenuInfo) item.getMenuInfo();
        final int selectedItem = info.position;
        Contact contact = null;

        switch (item.getItemId()) {
            case R.id.action_fijo:
                contact = MainActivity.contactsList.get(selectedItem);
                String telephone = contact.getTelephone();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: " + telephone));
                /*askForCallingPermission();
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }*/
                startActivity(callIntent);
                break;
            case R.id.action_movil:
                contact = MainActivity.contactsList.get(selectedItem);
                String mobile = contact.getMobile();
                Intent mobileIntent = new Intent(Intent.ACTION_CALL);
                mobileIntent.setData(Uri.parse("tel: " + mobile));
                startActivity(mobileIntent);
                break;
            case R.id.action_editar:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("position", selectedItem);
                startActivity(intent);
                break;
            case R.id.action_eliminar:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setMessage(R.string.lb_are_you_sure)
                        .setPositiveButton(R.string.lb_yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MainActivity.contactsList.remove(selectedItem);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(ListActivity.this, R.string.msg_deleted, Toast.LENGTH_LONG).show();
                                    }
                                })
                        .setNegativeButton(R.string.lb_yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                builder.create().show();
                break;
            case R.id.action_email:
                contact = MainActivity.contactsList.get(selectedItem);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, contact.getEmail());
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, getString(R.string.msg_select_email)));
                break;
            case R.id.action_detalles:
                Intent detailsIntent = new Intent(this, DetailsActivity.class);
                detailsIntent.putExtra("position", selectedItem);
                startActivity(detailsIntent);

                Notification.Builder nBuilder = new Notification.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_notification_overlay)
                        .setContentTitle("Prueba")
                        .setContentText("Esto es una notificacion");
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, nBuilder.build());
                break;
            default:
                break;
        }

        return false;
    }

    private void askForCallingPermission() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }
}
