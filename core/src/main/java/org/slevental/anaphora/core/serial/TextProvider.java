package org.slevental.anaphora.core.serial;

import org.slevental.anaphora.core.txt.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface TextProvider {
    Text deserialize(InputStream stream) throws IOException;

    void serialize(Text txt, OutputStream stream) throws IOException;
}
