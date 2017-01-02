package jagerfield.comparison.lib.ContactFields;

import android.database.Cursor;
import android.provider.ContactsContract;

import jagerfield.comparison.lib.Abstracts.FieldParent;
import jagerfield.comparison.lib.ElementContainers.EmailContainer;
import com.google.gson.annotations.Expose;
import java.util.LinkedList;
import java.util.Set;

public class EmailField extends FieldParent
{
    public final String fieldMime = ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE;
    @Expose
    private LinkedList<EmailContainer> emails = new LinkedList<>();

    public EmailField()
    {

    }

    @Override
    public void execute(String mime, Cursor cursor)
    {
        if (mime.equals(fieldMime))
        {
            emails.add(new EmailContainer(cursor));
        }

    }

    @Override
    public Set<String> registerElementsColumns()
    {
        return EmailContainer.getFieldColumns();
    }

    public LinkedList<EmailContainer> getEmails()
    {
        return emails;
    }

    public interface IEmailField
    {
        public LinkedList<EmailContainer> getEmails();
    }

}
