package org.devfleet.crest.retrofit;

import org.devfleet.crest.model.CrestFitting;
import org.junit.Ignore;
import org.junit.Test;

public class FittingsTest extends AuthenticatedCRESTTest {

    @Test
    public void testFittings() {
        for (CrestFitting c: service.getFittings()) {
            System.err.println(c.getName());
        }
    }
}
