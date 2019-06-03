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

import com.sfaci.contacts.domain.Friend;

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
        Friend friend = MainActivity.friendsList.get(position);

        EditText etName = findViewById(R.id.etName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etTelephone = findViewById(R.id.etTelephone);
        EditText etBirthDate = findViewById(R.id.etBirthDate);
        EditText etMobile = findViewById(R.id.etMobile);
        EditText etDebts = findViewById(R.id.etDebts);
        ImageButton ibPicture = findViewById(R.id.ibFoto);

        etName.setText(friend.getName());
        etEmail.setText(friend.getEmail());
        etTelephone.setText(friend.getTelephone());
        etBirthDate.setText(String.valueOf(friend.getBirthDate()));
        etMobile.setText(friend.getMobile());
        etDebts.setText(String.valueOf(friend.getDebts()));
        ibPicture.setImageBitmap(friend.getPicture());
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

                Friend friend = null;
                if (position == -1)
                    friend = new Friend();
                else
                    friend = MainActivity.friendsList.get(position);

                try {
                    friend.setName(etName.getText().toString());
                    friend.setEmail(etEmail.getText().toString());
                    friend.setTelephone(etTelephone.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    friend.setBirthDate(sdf.parse(etBirthDate.getText().toString()));
                    friend.setMobile(etMobile.getText().toString());
                    if (etDebts.getText().toString().equals(""))
                        etDebts.setText("0");
                    friend.setDebts(Float.parseFloat(etDebts.getText().toString()));
                    friend.setPicture(((BitmapDrawable) ibPicture.getDrawable()).getBitmap());

                    if (position == -1)
                        MainActivity.friendsList.add(friend);

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
