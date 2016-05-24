package org.devfleet.crest.retrofit;

import java.util.List;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestMarketHistory;
import org.devfleet.crest.model.CrestSolarSystem;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public final class PublicCRESTTest {

    private static CrestService service;

    @BeforeClass
    public static void setupCREST() throws Exception {
        service = CrestClient.TQ().build().fromDefault();
    }

    @Test
    @Ignore
    public void testLocations() {
        final List<CrestSolarSystem> locations = service.getLocations();
        Assert.assertFalse(locations.isEmpty());
        Assert.assertNotNull(service.getSolarSystem(locations.get(0).getId()));
    }

    @Test
    @Ignore
    public void testMarketHistory() {
        final List<CrestMarketHistory> h = service.getMarketHistory(10000033, 23713);
        Assert.assertFalse(h.isEmpty());
    }
}
