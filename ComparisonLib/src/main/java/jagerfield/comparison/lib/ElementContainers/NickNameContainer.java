package jagerfield.comparison.lib.ElementContainers;

import android.database.Cursor;

import jagerfield.comparison.lib.FieldElements.NickNameElement.NickNameElement;
import jagerfield.comparison.lib.FieldElements.NickNameElement.NickNameTypeElement;
import jagerfield.comparison.lib.Utilities.Utility;
import com.google.gson.annotations.Expose;
import java.util.HashSet;
import java.util.Set;

public class NickNameContainer
{
    private transient Cursor cursor;
    @Expose
    private NickNameElement nickName;
    @Expose
    private NickNameTypeElement nickNameType;

    public NickNameContainer(Cursor cursor)
    {
        this.cursor = cursor;
        nickName = new NickNameElement(cursor);
        nickNameType = new NickNameTypeElement(cursor);
    }

    public static Set<String> getFieldColumns()
    {
        Set<String> columns = new HashSet<>();
        columns.add(NickNameElement.column);
        columns.add(NickNameTypeElement.column);
        return columns;
    }

    public String getNickName()
    {
        String result = Utility.elementValue(nickName);
        return result;
    }
    public String getNickNameType()
    {
        String result = Utility.elementValue(nickNameType);
        return result;
    }

}