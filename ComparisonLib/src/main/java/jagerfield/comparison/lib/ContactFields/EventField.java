package jagerfield.comparison.lib.ContactFields;

import android.database.Cursor;
import android.provider.ContactsContract;
import jagerfield.comparison.lib.Abstracts.FieldParent;
import jagerfield.comparison.lib.ElementContainers.EventContainer;
import com.google.gson.annotations.Expose;
import java.util.LinkedList;
import java.util.Set;

public class EventField extends FieldParent
{
    public final String fieldMime = ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE;
    @Expose
    private LinkedList<EventContainer> events = new LinkedList<>();

    public EventField()
    {

    }

    @Override
    public void execute(String mime, Cursor cursor)
    {
        if (mime.equals(fieldMime))
        {
            events.add(new EventContainer(cursor));
        }
    }

    @Override
    public Set<String> registerElementsColumns()
    {
        return EventContainer.getFieldColumns();
    }

    public LinkedList<EventContainer> getEvents()
    {
        return events;
    }

    public interface IEventField
    {
        public LinkedList<EventContainer> getEvents();
    }

}