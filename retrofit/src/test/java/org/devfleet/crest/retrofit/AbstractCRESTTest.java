package org.devfleet.crest.retrofit;

import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.devfleet.crest.model.CrestServerStatus;
import org.junit.Assert;
import org.junit.BeforeClass;

public abstract class AbstractCRESTTest {

    private static Properties properties;
    protected static CrestClient client;

    @BeforeClass
    public static void setupProperties() throws Exception {
        properties = new Properties();
        properties.load(AbstractCRESTTest.class.getResourceAsStream("/crest.properties"));

        client =
            CrestClient.SISI()
            .id(property("crest.id"))
            .key(property("crest.key"))
            .build();

        final CrestServerStatus status = client.fromDefault().getServerStatus();
        Assert.assertNotNull("SISI is not available", status);
        Assert.assertEquals(
                "Expected to be on SISI",
                "SINGULARITY",
                status.getServerName().toUpperCase());
    }

    protected static String property(final String key) {
        final String p = properties.getProperty(key);
        Assert.assertFalse("property not found '" + key + "'", StringUtils.isBlank(p));
        return p;
    }
}
