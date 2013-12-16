package org.slevental.anaphora.core.txt;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Relations {
    private Multimap<Annotation, Annotation> synsets = HashMultimap.create();
    private Multimap<Annotation, Annotation> anaphora = HashMultimap.create();

    public Iterable<Annotation> getSyn(Annotation a){
        return synsets.get(a);
    }

    public Iterable<Annotation> getAnaphora(Annotation a){
        return anaphora.get(a);
    }

    public Relations putSyn(Annotation a, Annotation b){
        return put(a, b, synsets);
    }

    public Relations putAnaphora(Annotation a, Annotation b){
        return put(a, b, anaphora);
    }

    public Multimap<Annotation, Annotation> getSynsets() {
        return synsets;
    }

    public Multimap<Annotation, Annotation> getAnaphora() {
        return anaphora;
    }

    private Relations put(Annotation a, Annotation b, Multimap<Annotation, Annotation> map) {
        map.put(a, b);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relations)) return false;

        Relations relations = (Relations) o;

        if (anaphora != null ? !anaphora.equals(relations.anaphora) : relations.anaphora != null) return false;
        if (synsets != null ? !synsets.equals(relations.synsets) : relations.synsets != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = synsets != null ? synsets.hashCode() : 0;
        result = 31 * result + (anaphora != null ? anaphora.hashCode() : 0);
        return result;
    }
}
