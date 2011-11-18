package org.apache.tapestry.scopes.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class Index
{
    @Persist
    @Property
    private int counter;

    void onIncrease()
    {
        counter++;
    }

    void onDecrease()
    {
        counter--;
    }
}
