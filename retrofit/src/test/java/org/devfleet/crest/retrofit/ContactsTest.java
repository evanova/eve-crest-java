package org.devfleet.crest.retrofit;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.devfleet.crest.model.CrestContact;
import org.devfleet.crest.model.CrestItem;
import org.junit.Assert;
import org.junit.Test;

public class ContactsTest extends AbstractCrestServiceTest {

    @Test
    public void testContacts() {
        for (CrestContact c: service.getContacts()) {
            System.err.println(ToStringBuilder.reflectionToString(c));
        }
    }

/*{
 "standing": 10,
 "contactType": "Alliance"
 "contact": {
   "id_str": "99000003",
   "href": "http://crest.regner.dev/alliances/99000003/",
   "name": "One One Corporation Alliance",
   "id": 99000003
 }
}

[6:40]
That is what it should look like*/
    @Test
    public void addContact() {
        final long id = Long.parseLong(property("crest.contact"));

        CrestContact contact = service.getContact(id);
        if (null == contact) {
            contact = new CrestContact();
            contact.setContactType("Character");
            contact.setStanding(10);
            final CrestItem item = new CrestItem();
            item.setId(id);
            item.setName("");//F4 says it should be blank
            contact.setContact(item);
        }
        else {
            Assert.assertTrue(service.deleteContact(id));
        }
        Assert.assertTrue(service.addContact(contact));

    }
}
