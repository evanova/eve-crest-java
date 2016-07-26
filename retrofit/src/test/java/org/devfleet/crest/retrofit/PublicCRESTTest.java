package org.devfleet.crest.retrofit;

import java.util.List;

import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PublicCRESTTest {

    private static final Logger LOG = LoggerFactory.getLogger(PublicCRESTTest.class);

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
    
    @Test
    @Ignore
    public void testGetJita150mmRailIIMarketHistory() {
        final List<CrestMarketHistory> h = service.getMarketHistory(10000002, 23713);
        Assert.assertFalse(h.isEmpty());
    }

    @Test
    @Ignore
    public void testGetJita150mmRailIIMarketPrices ( ) {
        final List<CrestMarketOrder> o = service.getMarketOrders(10000002, "sell", 3074);
        LOG.info("Retrieved " + o.size() + " items");
        Assert.assertFalse(o.isEmpty());
    }
    
    @Test
    @Ignore
    public void testGetAllMarketOrders ( ) {
        final List<CrestMarketBulkOrder> bo = service.getAllMarketOrders(10000002);
        Assert.assertFalse(bo.isEmpty());
    }

    @Test
    @Ignore
    public void testGetAllMarketPrices ( ) {
        final List<CrestMarketPrice> prices = service.getAllMarketPrices();
        Assert.assertFalse(prices.isEmpty());
    }

    @Test
    @Ignore
    public void testGet150mmLightAutoCannonInventoryType ( ) {
        final CrestType type = service.getInventoryType(485);
        Assert.assertNotNull(type);
        Assert.assertEquals(type.getName(), "150mm Light AutoCannon I");
    }

    @Test
    @Ignore
    public void testGetRegions ( ) {
        final List<CrestItem> regions = service.getRegions();
        Assert.assertNotNull(regions);
        Assert.assertEquals(regions.size(), 100);
    }
}
