package org.slevental.anaphora.core.serial;

import org.apache.commons.io.IOUtils;
import org.slevental.anaphora.core.txt.Text;

import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TextProviders {
    private static final Map<String, Class<? extends TextProvider>> providers = new ConcurrentHashMap<String, Class<? extends TextProvider>>();

    /**
     * Register standard providers
     */
    static {
        providers.put(JsonProvider.JSON, JsonProvider.class);
    }

    private TextProviders() {
    }

    public static Class<? extends TextProvider> register(String scheme, Class<? extends TextProvider> provider) {
        return providers.put(scheme, provider);
    }

    public static Class<? extends TextProvider> getProvider(URI uri) {
        return getProvider(uri.getScheme());
    }

    private static Class<? extends TextProvider> getProvider(String scheme) {
        if (!providers.containsKey(scheme)) {
            throw new IllegalArgumentException("Cannot find provider for scheme : " + scheme);
        }
        return providers.get(scheme);
    }

    public static void write(Text text, URI uri) throws IOException {
        TextProvider textProvider = createProvider(uri);
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(new File(uri.getRawPath())));
            textProvider.serialize(text, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    private static TextProvider createProvider(URI uri) {
        Class<? extends TextProvider> provider = getProvider(uri);
        try {
            return provider.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Provider \'" + provider.getCanonicalName() +
                    "\' cause exception on new instance creation: " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Provider \'" + provider.getCanonicalName() +
                    "\' cause exception on class access during creating: " + e.getMessage(), e);
        }
    }

    public static Text read(URI uri) throws IOException {
        TextProvider provider = createProvider(uri);
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(uri.getSchemeSpecificPart())));
            return provider.deserialize(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
