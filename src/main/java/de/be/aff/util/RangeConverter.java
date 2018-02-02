package de.be.aff.util;

import org.springframework.data.domain.Range;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

public class RangeConverter extends PropertyEditorSupport {

    public void setAsText(final String text) throws IllegalArgumentException {

        String[] range=StringUtils.split(text, "-");

        if(range==null)//there was no delimiter in the text
            throw new IllegalArgumentException();

        setValue(new Range<>(Integer.valueOf(range[0]),Integer.valueOf(range[1])));
    }
}