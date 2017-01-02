package jagerfield.comparison.lib;

import android.app.Activity;
import jagerfield.comparison.lib.Contact.Contact;
import jagerfield.comparison.lib.Cursors.ImportContactsContentResolver;
import jagerfield.comparison.lib.Cursors.ImportContactsCursorLoader;
import java.util.ArrayList;

public class ImportContacts {
    private Activity context;
    private CursorLoaderCallback cursorLoaderCallback;
    private ContentResolverCallback contentResolverCallback;

    public ImportContacts(Activity context, ContentResolverCallback contentResolverCallback) {
        this.context = context;
        this.contentResolverCallback = contentResolverCallback;
        ImportContactsContentResolver importContactsContentResolver = new ImportContactsContentResolver(context);
        this.contentResolverCallback.getMobileContacts(importContactsContentResolver.getMobileContacts());
    }

    public ImportContacts(Activity context, final CursorLoaderCallback cursorLoaderCallback) {
        this.context = context;
        this.cursorLoaderCallback = cursorLoaderCallback;

        new ImportContactsCursorLoader(context, new ImportContactsCursorLoader.Callback() {
            @Override
            public void getMobileContacts(ArrayList<Contact> contacts)
            {
                cursorLoaderCallback.getMobileContacts(contacts);
            }
        });

    }

    public interface ContentResolverCallback {
        void getMobileContacts(ArrayList<Contact> contacts);
    }

    public interface CursorLoaderCallback {
        void getMobileContacts(ArrayList<Contact> contacts);
    }

}



