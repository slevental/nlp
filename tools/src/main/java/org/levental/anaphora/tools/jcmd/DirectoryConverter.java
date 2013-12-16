package org.levental.anaphora.tools.jcmd;

import com.beust.jcommander.IStringConverter;

import java.io.File;

public class DirectoryConverter implements IStringConverter<File> {
    @Override
    public File convert(String value) {
        File f = new File(value);
        if (!f.exists())
            throw new RuntimeException("File not found: " + f.getAbsolutePath());
        if (!f.isDirectory())
            throw new RuntimeException("Directory required: " + f.getAbsolutePath());
        return f;
    }
}
