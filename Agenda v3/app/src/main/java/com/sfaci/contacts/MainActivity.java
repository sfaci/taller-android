package com.sfaci.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sfaci.contacts.domain.Contact;

import java.util.ArrayList;

/**
 * Activity principal. Muestra el menú de la aplicación
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btRegister = findViewById(R.id.btRegister);
        btRegister.setOnClickListener(this);
        Button btList = findViewById(R.id.btList);
        btList.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btRegister:
                // El usuario ha pulsado el botón de Registro. Se lanza la Activity para registrar un nuevo Amigo
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btList:
                // El usuario ha pulsado el botón de Listado. Se mostrará el listado de amigos registrados
                startActivity(new Intent(this, ListActivity.class));
                break;
            default:
                break;
        }
    }
}
