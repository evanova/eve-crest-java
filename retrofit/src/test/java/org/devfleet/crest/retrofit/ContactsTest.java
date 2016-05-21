package org.devfleet.crest.retrofit;

import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.devfleet.crest.model.CrestContact;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ContactsTest extends AuthenticatedCRESTTest {

    @Test
    @Ignore
    public void testContacts() {
        for (CrestContact c: service.getContacts()) {
            System.err.println(ToStringBuilder.reflectionToString(c));
        }
    }

    @Test
    @Ignore
    public void addContact() {
        final List<CrestContact> contacts = service.getContacts();
        Assert.assertFalse("No contact found. Do you have any friend?", contacts.isEmpty());

        final CrestContact contact = contacts.get(0);
        Assert.assertTrue(service.deleteContact(contact.getContact().getId()));
        Assert.assertTrue(service.addContact(contact));
    }
}
