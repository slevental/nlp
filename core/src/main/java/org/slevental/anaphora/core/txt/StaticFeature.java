package org.slevental.anaphora.core.txt;

public enum StaticFeature implements Feature{
    category, // PartOfSpeech
    roleInSentence, // subject/object/predicate
    string,
    kind,
    length,
    matches,
    score(0d),
    adjective,
    relation,
    creationRule("unknown");

    final Object def;

    StaticFeature() {
        this(null);
    }

    StaticFeature(Object def) {
        this.def = def;
    }

    public Object getDefault(){
        return def;
    }

    public static StaticFeature of(Object obj){
        if (obj instanceof String){
            for (StaticFeature each : values()) {
                if (each.name().equalsIgnoreCase((String) obj))
                    return each;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return this.name();
    }


}
