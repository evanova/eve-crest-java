package org.devfleet.crest;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class CrestAccessTest {

    @Test
    public void testGetOneScope() {
        final List<String> scopes = CrestAccess.getScope(268435456l);
        Assert.assertEquals(1, scopes.size());
        Assert.assertEquals("characterBookmarksRead", scopes.get(0));
    }


    @Test
    public void testGetTwoScope() {
        final List<String> scopes = CrestAccess.getScope(33554432l | 268435456l);
        Assert.assertEquals(2, scopes.size());
        Assert.assertTrue(scopes.contains("characterAccountRead"));
        Assert.assertTrue(scopes.contains("characterBookmarksRead"));
    }


}
