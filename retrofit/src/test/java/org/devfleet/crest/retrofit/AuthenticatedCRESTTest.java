package org.devfleet.crest.retrofit;

import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestCharacter;
import org.devfleet.crest.model.CrestServerStatus;
import org.junit.Assert;
import org.junit.BeforeClass;

public abstract class AuthenticatedCRESTTest extends AbstractCRESTTest {

    protected static CrestService service;
    protected static CrestCharacter character;

    @BeforeClass
    public static void setupCREST() throws Exception {
        service = client.fromRefreshToken(property("crest.token"));
        character = service.getCharacter();

        Assert.assertNotNull(character);
        System.err.println("Selected character " + character.getName());
    }
}
