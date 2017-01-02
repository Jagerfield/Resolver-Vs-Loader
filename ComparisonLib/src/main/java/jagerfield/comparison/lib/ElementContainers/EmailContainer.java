package jagerfield.comparison.lib.ElementContainers;

import android.database.Cursor;

import jagerfield.comparison.lib.FieldElements.EmailElements.EmailElement;
import jagerfield.comparison.lib.FieldElements.EmailElements.EmailLabelElement;
import jagerfield.comparison.lib.FieldElements.EmailElements.EmailTypeElement;
import jagerfield.comparison.lib.Utilities.Utility;
import com.google.gson.annotations.Expose;
import java.util.HashSet;
import java.util.Set;

public class EmailContainer
{
    private transient Cursor cursor;
    @Expose
    private EmailElement email;
    @Expose
    private EmailTypeElement emailType;
    @Expose
    private EmailLabelElement emailLabel;

    public EmailContainer(Cursor cursor)
    {
        this.cursor = cursor;
        email = new EmailElement(cursor);
        emailType = new EmailTypeElement(cursor);
        emailLabel = new EmailLabelElement(cursor);
    }

    public static Set<String> getFieldColumns()
    {
        Set<String> columns = new HashSet<>();
        columns.add(EmailElement.column);
        columns.add(EmailTypeElement.column);
        columns.add(EmailLabelElement.column);
        return columns;
    }

    public String getEmail()
    {
        String result = Utility.elementValue(email);
        return result;
    }
    public String getEmailType() {
        String result = Utility.elementValue(emailType);
        return result;
    }
    public String getEmailLabel() {
        String result = Utility.elementValue(emailLabel);
        return result;
    }
}