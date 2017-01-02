package jagerfield.comparison.lib.ContactFields;

import android.database.Cursor;
import android.provider.ContactsContract;
import jagerfield.comparison.lib.Abstracts.FieldParent;
import jagerfield.comparison.lib.ElementContainers.NoteContainer;
import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.Set;

public class NoteField extends FieldParent
{
    public final String fieldMime = ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE;

    @Expose
    private LinkedList<NoteContainer> notes = new LinkedList<>();

    public NoteField()
    {}

    @Override
    public void execute(String mime, Cursor cursor)
    {
        if (mime.equals(fieldMime))
        {
            notes.add(new NoteContainer(cursor));
        }
    }

    @Override
    public Set<String> registerElementsColumns()
    {
        return NoteContainer.getFieldColumns();
    }

    public LinkedList<NoteContainer> getNotes()
    {
        return notes;
    }

    public interface INoteField
    {
        public LinkedList<NoteContainer> getNotes();
    }

}






