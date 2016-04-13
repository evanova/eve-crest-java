package org.devfleet.crest;

import java.util.List;
import org.devfleet.crest.model.CrestCharacter;
import org.devfleet.crest.model.CrestContact;
import org.devfleet.crest.model.CrestServerStatus;
import org.devfleet.crest.model.CrestSolarSystem;
import org.devfleet.crest.model.CrestFitting;

public interface CrestService {

    CrestServerStatus getServerStatus();

    CrestCharacter getCharacter();

    CrestSolarSystem getLocation();

    List<CrestContact> getContacts();

    List<CrestFitting> getFittings();

}
