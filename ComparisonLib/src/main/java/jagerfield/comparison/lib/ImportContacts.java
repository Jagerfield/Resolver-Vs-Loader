package jagerfield.comparison.lib;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import jagerfield.comparison.lib.Contact.Contact;
import jagerfield.comparison.lib.Cursors.ImportContactsContentResolver;
import jagerfield.comparison.lib.Cursors.ImportContactsCursorLoader;
import jagerfield.comparison.lib.Utilities.Utility;

import java.util.ArrayList;

public class ImportContacts {
    private Activity activity;
    private CursorLoaderCallback cursorLoaderCallback;
    private ContentResolverCallback contentResolverCallback;

    public ImportContacts(Activity activity, ContentResolverCallback contentResolverCallback)
    {
        this.activity = activity;
        this.contentResolverCallback = contentResolverCallback;

        boolean flag = Utility.hasPermission(activity, Manifest.permission.READ_CONTACTS);
        if (flag)
        {
            ImportContactsContentResolver importContactsContentResolver = new ImportContactsContentResolver(activity);
            this.contentResolverCallback.getMobileContacts(importContactsContentResolver.getMobileContacts());
        }
        else
        {
            Log.e(Utility.TAG_LIB, Manifest.permission.READ_CONTACTS + " permission is missing.");
        }

    }

    public ImportContacts(Activity activity, final CursorLoaderCallback cursorLoaderCallback) {
        this.activity = activity;
        this.cursorLoaderCallback = cursorLoaderCallback;

        boolean flag = Utility.hasPermission(activity, Manifest.permission.READ_CONTACTS);
        if (flag)
        {
            new ImportContactsCursorLoader(activity, new ImportContactsCursorLoader.Callback() {
                @Override
                public void getMobileContacts(ArrayList<Contact> contacts)
                {
                    cursorLoaderCallback.getMobileContacts(contacts);
                }
            });
        }
        else
        {
            Log.e(Utility.TAG_LIB, Manifest.permission.READ_CONTACTS + " permission is missing.");
        }

    }

    public interface ContentResolverCallback {
        void getMobileContacts(ArrayList<Contact> contacts);
    }

    public interface CursorLoaderCallback {
        void getMobileContacts(ArrayList<Contact> contacts);
    }

}



