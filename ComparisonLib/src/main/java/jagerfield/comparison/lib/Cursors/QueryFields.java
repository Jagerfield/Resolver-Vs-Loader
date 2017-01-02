package jagerfield.comparison.lib.Cursors;

import android.database.Cursor;
import android.provider.ContactsContract;

import jagerfield.comparison.lib.Contact.Contact;
import jagerfield.comparison.lib.FieldContainer.FieldsContainer;
import jagerfield.comparison.lib.Utilities.Utility;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class QueryFields
{
    private LinkedHashMap<Long, Contact> contactsIdMap;
    @Expose
    private ArrayList<Contact> contacts;

    public String[] getKeyWord() {
        return null;
    }

    public String getFilter() {
        return null;
    }

    public String[] getColumns()
    {
        Set<String> columns = new HashSet<>();

        FieldsContainer fieldsContainer = new FieldsContainer();
        columns.addAll(fieldsContainer.getElementsColumns());
        columns.add(ContactsContract.RawContacts.CONTACT_ID);
        columns.add(ContactsContract.Data.MIMETYPE);
        columns.add(ContactsContract.Data.PHOTO_URI);

        return columns.toArray(new String[columns.size()]);
    }

    public void insertContact(Contact contact) {
        this.contacts.add(contact);
    }

    public ArrayList<Contact> getContacts(Cursor cursor)
    {

        if (cursor != null)
        {
            long id;
            String photoUri;
            String columnIndex;
            contactsIdMap = new LinkedHashMap<>();
            contacts = new ArrayList<>();

            while (cursor.moveToNext()) {
                id = Utility.getLong(cursor, ContactsContract.RawContacts.CONTACT_ID);

                Contact contact = contactsIdMap.get(id);
                if (contact == null) {
                    contact = new Contact(id);
                    contactsIdMap.put(id, contact);
                    insertContact(contact);
                }

                try
                {
                    photoUri = Utility.getColumnIndex(cursor, ContactsContract.Data.PHOTO_URI);
                    if (photoUri != null && !photoUri.isEmpty()) {
                        contact.setPhotoUri(photoUri);
                    }
                } catch (Exception e) {
                    photoUri = "";
                }

                columnIndex = Utility.getColumnIndex(cursor, ContactsContract.Data.MIMETYPE);
                contact.execute(cursor, columnIndex);
            }
        }

        return contacts;
    }

}
