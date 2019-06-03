package com.sfaci.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfaci.contacts.database.Database;
import com.sfaci.contacts.domain.Contact;
import com.sfaci.contacts.util.DateUtils;

import java.text.SimpleDateFormat;

/**
 * Activity donde se muestran los detalles de cada contacto
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        int posicion = getIntent().getIntExtra("position", -1);
        if (posicion != -1)
            loadDetails(posicion);
    }

    private void loadDetails(int position) {
        ImageView ivPicture = findViewById(R.id.ivPicture);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvPhone = findViewById(R.id.tvTelephone);
        TextView tvMobile = findViewById(R.id.tvMobile);
        TextView tvBirthDate = findViewById(R.id.tvBirthDate);
        TextView tvDebts = findViewById(R.id.tvDebts);

        Database db = new Database(this);
        Contact contact = db.getContacts().get(position);

        ivPicture.setImageBitmap(contact.getPicture());
        tvName.setText(contact.getName());
        tvEmail.setText(contact.getEmail());
        tvPhone.setText(contact.getTelephone());
        tvMobile.setText(contact.getMobile());
        tvBirthDate.setText(DateUtils.toString(contact.getBirthDate()));
        tvDebts.setText(String.valueOf(contact.getDebts()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
