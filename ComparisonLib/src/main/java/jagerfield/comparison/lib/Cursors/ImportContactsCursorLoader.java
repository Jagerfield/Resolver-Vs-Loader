package jagerfield.comparison.lib.Cursors;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import jagerfield.comparison.lib.Contact.Contact;

import java.util.ArrayList;


public class ImportContactsCursorLoader extends QueryFields implements LoaderManager.LoaderCallbacks<Cursor> {
    private Activity context;
    private Callback client;
    private static int LOADER_ID = 100;

    public ImportContactsCursorLoader(Activity context, Callback client) {
        this.context = context;
        this.client = client;

        context.getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new CursorLoader(context,
                    ContactsContract.Data.CONTENT_URI,      // The content URI of the words table
                    super.getColumns(),                     // The columns to return for each row
                    super.getFilter(),                      // Selection criteria
                    super.getKeyWord(),                     // Selection criteria
                    ContactsContract.Data.DISPLAY_NAME);    // The sort order for the returned rows
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Contact> contacts = null;

        if (cursor != null) {
            contacts = super.getContacts(cursor);
        }

        if (contacts != null) {
            client.getMobileContacts(contacts);
        }

        if (cursor == null || contacts == null) {
            contacts = new ArrayList<>();
            client.getMobileContacts(contacts);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface Callback {
        void getMobileContacts(ArrayList<Contact> contacts);
    }
}



