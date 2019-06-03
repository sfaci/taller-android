package com.sfaci.contacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sfaci.contacts.database.Database;
import com.sfaci.contacts.domain.Contact;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Activity con el formulario para registrar o modificar un contacto
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private int position;
    private final int LOAD_IMAGE_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        position = getIntent().getIntExtra("position", -1);
        if (position != -1)
            loadContact(position);

        Button btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(this);
        Button btCancel = findViewById(R.id.btClose);
        btCancel.setOnClickListener(this);
        ImageButton ibPicture = findViewById(R.id.ibFoto);
        ibPicture.setOnClickListener(this);
    }

    private void loadContact(int position) {
        Database db = new Database(this);
        Contact contact = db.getContacts().get(position);

        EditText etName = findViewById(R.id.etName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etTelephone = findViewById(R.id.etTelephone);
        EditText etBirthDate = findViewById(R.id.etBirthDate);
        EditText etMobile = findViewById(R.id.etMobile);
        EditText etDebts = findViewById(R.id.etDebts);
        ImageButton ibPicture = findViewById(R.id.ibFoto);

        etName.setText(contact.getName());
        etEmail.setText(contact.getEmail());
        etTelephone.setText(contact.getTelephone());
        etBirthDate.setText(String.valueOf(contact.getBirthDate()));
        etMobile.setText(contact.getMobile());
        etDebts.setText(String.valueOf(contact.getDebts()));
        ibPicture.setImageBitmap(contact.getPicture());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if ((requestCode == LOAD_IMAGE_RESULT) &&
                (resultCode == RESULT_OK) && (data != null)) {
            // Obtiene el Uri de la imagen seleccionada por el usuario
            Uri selectedImage = data.getData();
            String[] path = {MediaStore.Images.Media.DATA};

            // Realiza una consulta a la galería de imágenes solicitando la imagen seleccionada
            Cursor cursor = getContentResolver().query(selectedImage,
                    path, null, null, null);
            cursor.moveToFirst();

            // Obtiene la ruta a la imagen
            int index = cursor.getColumnIndex(path[0]);
            String picturePath = cursor.getString(index);
            cursor.close();

            // Carga la imagen en el botón ImageButton
            ImageButton ibPicture = findViewById(R.id.ibFoto);
            ibPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registro, menu);
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
            case R.id.btSave:
                EditText etName = findViewById(R.id.etName);
                EditText etEmail = findViewById(R.id.etEmail);
                EditText etTelephone = findViewById(R.id.etTelephone);
                EditText etBirthDate =  findViewById(R.id.etBirthDate);
                EditText etMobile = findViewById(R.id.etMobile);
                EditText etDebts = findViewById(R.id.etDebts);
                ImageButton ibPicture = findViewById(R.id.ibFoto);

                Contact contact = null;
                if (position == -1)
                    contact = new Contact();
                else {
                    Database db = new Database(this);
                    contact = db.getContacts().get(position);
                }
                try {
                    contact.setName(etName.getText().toString());
                    contact.setEmail(etEmail.getText().toString());
                    contact.setTelephone(etTelephone.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    contact.setBirthDate(sdf.parse(etBirthDate.getText().toString()));
                    contact.setMobile(etMobile.getText().toString());
                    if (etDebts.getText().toString().equals(""))
                        etDebts.setText("0");
                    contact.setDebts(Float.parseFloat(etDebts.getText().toString()));
                    contact.setPicture(((BitmapDrawable) ibPicture.getDrawable()).getBitmap());

                    if (position == -1) {
                        Database db = new Database(this);
                        db.addContact(contact);
                    }

                    Toast.makeText(this, R.string.msg_saved, Toast.LENGTH_LONG).show();
                } catch (ParseException pe) {
                    Toast.makeText(this, R.string.msg_date_error, Toast.LENGTH_SHORT).show();
                }

                // TODO Limpiar el contenido de las cajas de texto

                break;
            case R.id.btClose:
                onBackPressed();
                break;
            case R.id.ibFoto:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOAD_IMAGE_RESULT);
                break;
            default:
                break;
        }
    }
}
