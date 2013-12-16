package org.levental.anaphora.tools.jcmd;

import com.beust.jcommander.IStringConverter;
import org.slevental.anaphora.core.txt.AnnotationType;

public class AnnotationTypeConverter implements IStringConverter<AnnotationType> {
    @Override
    public AnnotationType convert(String value) {
        return AnnotationType.valueOf(value);
    }
}
