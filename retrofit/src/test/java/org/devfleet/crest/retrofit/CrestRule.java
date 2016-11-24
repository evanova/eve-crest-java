package org.devfleet.crest.retrofit;

import org.apache.commons.lang3.StringUtils;
import org.devfleet.crest.CrestService;
import org.junit.Assert;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Properties;

public class CrestRule extends ExternalResource {

    private Properties properties;
    private CrestClient client;

    @Override
    public Statement apply(Statement base, Description description) {
        return super.apply(base, description);
    }

    @Override
    protected void before() throws Throwable {
        properties = new Properties();
        properties.load(CrestRule.class.getResourceAsStream("/crest.properties"));
        client =
            CrestClient.TQ()
                    .id(property("crest.id"))
                    .key(property("crest.key"))
                    .build();
    }

    public CrestClient getClient() {
        return client;
    }

    public CrestService publicCrest() {
        return this.client.newCrestService();
    }

    public CrestService authenticatedCrest() {
        return this.client.newCrestService(property("crest.token"));
    }

    @Override
    protected void after() {
        this.client = null;
    }

    private final String property(final String key) {
        final String p = properties.getProperty(key);
        Assert.assertFalse("property not found '" + key + "'", StringUtils.isBlank(p));
        return p;
    }
}
