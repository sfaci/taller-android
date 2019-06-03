package com.sfaci.contacts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sfaci.contacts.domain.Contact;
import com.sfaci.contacts.util.DateUtils;
import com.sfaci.contacts.util.PictureUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.sfaci.contacts.util.Constants.CONTACTS_TABLE;

import static android.provider.BaseColumns._ID;
import static com.sfaci.contacts.util.Constants.CONTACT_BIRTHDATE;
import static com.sfaci.contacts.util.Constants.CONTACT_EMAIL;
import static com.sfaci.contacts.util.Constants.CONTACT_MOBILE;
import static com.sfaci.contacts.util.Constants.CONTACT_NAME;
import static com.sfaci.contacts.util.Constants.CONTACT_PICTURE;
import static com.sfaci.contacts.util.Constants.CONTACT_TELEPHONE;

/**
 * Clase para la gestión de la base de datos
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 *
 * TODO Faltaría añadir métodos para eliminar, modificar y demás operaciones sobre la base de datos
 */
public class Database extends SQLiteOpenHelper {
    // Nombre de la base de datos
    private static final String DATABASE_NAME = "contacts.db";
    // Versión de la base de datos
    private static final int DATABASE_VERSION = 1;

    // Claúsula FROM y ORDER BY para utilizar a la hora de consultar los datos
    private static String[] FROM_CURSOR = {_ID, CONTACT_NAME, CONTACT_EMAIL, CONTACT_TELEPHONE, CONTACT_MOBILE,
                                           CONTACT_BIRTHDATE, CONTACT_PICTURE};
    private static String ORDER_BY = CONTACT_NAME + " DESC";

    public Database(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Crea la estructura de las tablas de la Base de Datos
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CONTACTS_TABLE + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CONTACT_NAME + " TEXT NOT NULL, "
                + CONTACT_EMAIL + " TEXT NOT NULL, "
                + CONTACT_TELEPHONE + " TEXT NOT NULL, "
                + CONTACT_MOBILE + " TEXT NOT NULL, "
                + CONTACT_PICTURE + " BLOB NOT NULL, "
                + CONTACT_BIRTHDATE + " TEXT NOT NULL"
                + ")");
    }

    /**
     * Realiza las operaciones para actualizar la Base de Datos cuando se
     * actualiza la aplicación
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        // Sería más conveniente lanzar un ALTER TABLE en vez de eliminar la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE);
        onCreate(db);
    }

    /**
     * Registra un nuevo contacto en la tabla
     * @param contact El contacto a registrar
     */
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contact.getName());
        values.put(CONTACT_EMAIL, contact.getEmail());
        values.put(CONTACT_TELEPHONE, contact.getTelephone());
        values.put(CONTACT_MOBILE, contact.getMobile());
        values.put(CONTACT_PICTURE, PictureUtils.getBytes(contact.getPicture()));
        values.put(CONTACT_BIRTHDATE, DateUtils.toString(contact.getBirthDate()));
        db.insertOrThrow(CONTACTS_TABLE, null, values);
    }

    /**
     * Obtiene un cursor con todos los contactos de la tabla
     * @return Todos los contactos de la tabla
     */
    public List<Contact> getContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Lanza una consulta sobre la base de datos con claúsula FROM y ORDER BY
        Cursor cursor = db.query(CONTACTS_TABLE, FROM_CURSOR, null, null, null, null, ORDER_BY);
        List<Contact> contactsList = new ArrayList<>();
        Contact contact = null;
        while (cursor.moveToNext()) {
            contact = new Contact();
            contact.setName(cursor.getString(1));
            contact.setEmail(cursor.getString(2));
            contact.setTelephone(cursor.getString(3));
            contact.setMobile(cursor.getString(4));
            try {
                contact.setBirthDate(DateUtils.toDate(cursor.getString(5)));
            } catch (ParseException pe) {
                contact.setBirthDate(null);
            }
            contact.setPicture(PictureUtils.getBitmap(cursor.getBlob(6)));

            contactsList.add(contact);
        }
        return contactsList;
    }
}
