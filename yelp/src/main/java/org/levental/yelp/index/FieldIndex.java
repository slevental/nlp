package org.levental.yelp.index;

import java.util.Collection;

public class FieldIndex<Entry, Field> {
    private java.lang.reflect.Field field;

    public FieldIndex(Collection<Entry> entries, String fieldName, Class<Entry> clazz) throws NoSuchFieldException {
        field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        buildIndex(entries);
    }

    private void buildIndex(Collection<Entry> entries) {

    }


}
