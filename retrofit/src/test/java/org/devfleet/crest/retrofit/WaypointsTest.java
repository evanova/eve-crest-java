package org.devfleet.crest.retrofit;

import java.util.Arrays;
import java.util.List;
import org.devfleet.crest.model.CrestContact;
import org.devfleet.crest.model.CrestSolarSystem;
import org.devfleet.crest.model.CrestWaypoint;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class WaypointsTest extends AuthenticatedCRESTTest {

    @Test
    @Ignore
    public void testSetWaypoints() {
       // final CrestSolarSystem jita = new CrestSolarSystem();
      //  jita.setId(30000142);

        final CrestSolarSystem amarr = new CrestSolarSystem();
        amarr.setId(30002187);

     /*   final CrestWaypoint from = new CrestWaypoint();
        from.setClearOtherWaypoints(true);
        from.setFirst(true);
        from.setSolarSystem(jita);*/

        final CrestWaypoint to = new CrestWaypoint();
        to.setClearOtherWaypoints(false);
        to.setFirst(false);
        to.setSolarSystem(amarr);
        service.setWaypoints(Arrays.asList(to));
    }

}
