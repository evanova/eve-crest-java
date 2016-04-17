package org.devfleet.crest.retrofit;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestCharacter;
import org.junit.Assert;
import org.junit.BeforeClass;

public abstract class AbstractCrestServiceTest {

    protected static CrestService service;
    protected static CrestCharacter character;

    private static Properties properties;

    @BeforeClass
    public static void setupClient() throws Exception {
        properties = new Properties();
        properties.load(AbstractCrestServiceTest.class.getResourceAsStream("/crest.properties"));

        final CrestClient client =
                CrestClient.TQ()
                .id(property("crest.id"))
                .key(property("crest.key"))
                .build();

        service = client.fromRefreshToken(property("crest.token"));
        character = service.getCharacter();

        Assert.assertNotNull(character);
        System.err.println("Selected character " + character.getName());
    }

    protected static String property(final String key) {
        final String p = properties.getProperty(key);
        Assert.assertFalse("property not found '" + key + "'", StringUtils.isBlank(p));
        return p;
    }
}
