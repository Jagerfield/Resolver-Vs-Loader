package jagerfield.comparison.lib.ContactFields;

import android.database.Cursor;
import android.provider.ContactsContract;

import jagerfield.comparison.lib.Abstracts.FieldParent;
import jagerfield.comparison.lib.ElementContainers.AddressContainer;
import com.google.gson.annotations.Expose;
import java.util.LinkedList;
import java.util.Set;

public class AddressField extends FieldParent {
    public final String fieldMime = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE;
    @Expose
    private LinkedList<AddressContainer> addressList = new LinkedList<>();

    public AddressField() {

    }

    @Override
    public void execute(String mime, Cursor cursor) {
        if (mime.equals(fieldMime)) {
            addressList.add(new AddressContainer(cursor));
        }
    }

    @Override
    public Set<String> registerElementsColumns() {
        return AddressContainer.getFieldColumns();
    }

    public LinkedList<AddressContainer> getAddresses() {
        return addressList;
    }

    public interface IAddressField {
        public LinkedList<AddressContainer> getAddresses();
    }

}
