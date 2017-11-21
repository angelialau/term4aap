package com.example.angelia.term4androidappproject;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * Created by Angelia on 21/11/17.
 */

public class MyListPreference extends ListPreference {
    public MyListPreference(final Context context) {
        this(context, null);
    }

    public MyListPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public CharSequence getSummary() {
        final CharSequence entry = getEntry();
        final CharSequence summary = super.getSummary();
        if (summary == null || entry == null) {
            return null;
        } else {
            return String.format(summary.toString(), entry);
        }
    }

    @Override
    public void setValue(final String value) {
        super.setValue(value);
        notifyChanged();
    }
}
