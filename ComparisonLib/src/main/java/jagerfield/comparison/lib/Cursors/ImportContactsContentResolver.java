package jagerfield.comparison.lib.Cursors;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import jagerfield.comparison.lib.Contact.Contact;
import java.util.ArrayList;


public class ImportContactsContentResolver extends QueryFields
{
    private Activity context;

    public ImportContactsContentResolver(Activity context)
    {
        this.context = context;
    }

    public ArrayList<Contact> getMobileContacts()
    {
        Cursor cursor = getContentResolverCursor();
        ArrayList<Contact> contacts = null;

        if (cursor!=null)
        {
            contacts = super.getContacts(cursor);
        }

        if (cursor==null || contacts==null)
        {
            contacts = new ArrayList<>();
        }

        if (cursor != null)
        {
            cursor.close();
        }

        return contacts;
    }

    private Cursor getContentResolverCursor() {
        return context.getContentResolver().
                    query(ContactsContract.Data.CONTENT_URI,       // The content URI of the words table
                    super.getColumns(),                            // The columns to return for each row
                    super.getFilter(),                             // Selection criteria
                    super.getKeyWord(),                            // Selection criteria
                    ContactsContract.Data.DISPLAY_NAME);
    }

}



