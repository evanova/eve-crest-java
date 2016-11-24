package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestToken;

public interface CrestStore {

    void save(final CrestToken token);

    void delete(final String refresh);

    CrestToken get(final String refresh);

}
