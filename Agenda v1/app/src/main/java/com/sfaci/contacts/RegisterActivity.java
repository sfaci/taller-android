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

import com.sfaci.contacts.domain.Contact;
import com.sfaci.contacts.util.DateUtils;

import java.text.ParseException;

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
        setContentView(R.layout.activity_register);

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
        Contact contact = MainActivity.friendsList.get(position);

        EditText etName = findViewById(R.id.etName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etTelephone = findViewById(R.id.etTelephone);
        EditText etBirthDate = findViewById(R.id.etBirthDate);
        EditText etMobile = findViewById(R.id.etMobile);
        ImageButton ibPicture = findViewById(R.id.ibFoto);

        etName.setText(contact.getName());
        etEmail.setText(contact.getEmail());
        etTelephone.setText(contact.getTelephone());
        etBirthDate.setText(DateUtils.toString(contact.getBirthDate()));
        etMobile.setText(contact.getMobile());
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
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
                ImageButton ibPicture = findViewById(R.id.ibFoto);

                Contact contact = null;
                if (position == -1)
                    contact = new Contact();
                else
                    contact = MainActivity.friendsList.get(position);

                try {
                    contact.setName(etName.getText().toString());
                    contact.setEmail(etEmail.getText().toString());
                    contact.setTelephone(etTelephone.getText().toString());
                    contact.setBirthDate(DateUtils.toDate(etBirthDate.getText().toString()));
                    contact.setMobile(etMobile.getText().toString());
                    contact.setPicture(((BitmapDrawable) ibPicture.getDrawable()).getBitmap());

                    if (position == -1)
                        MainActivity.friendsList.add(contact);

                    Toast.makeText(this, R.string.msg_saved, Toast.LENGTH_LONG).show();
                } catch (ParseException pe) {
                    Toast.makeText(this, R.string.msg_date_error, Toast.LENGTH_SHORT).show();
                }

                etName.setText("");
                etEmail.setText("");
                etTelephone.setText("");
                etMobile.setText("");
                etBirthDate.setText("");
                ibPicture.setImageDrawable(null);

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
