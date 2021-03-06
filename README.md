[![Build Status](https://travis-ci.org/evanova/eve-crest-java.svg?branch=master)](https://travis-ci.org/evanova/eve-crest-java)

## eve-crest-java

[Eve Online CREST](https://eveonline-third-party-documentation.readthedocs.org/en/latest/crest/index.html) implementation in Java.


#### Getting Started

* Insert Jitpack Dependency in Gradle
```
repositories {
    maven { url "https://jitpack.io" }
}
```

* Add project to dependencies
```
compile 'com.github.evanova:eve-crest-java:master-SNAPSHOT'
```
* If you don't want the latest version of master, Replace `master-SNAPSHOT` with a release version - current is `1.0`

#### Using the library

* Configure a CrestClient
```
final CrestClient client =
    CrestClient.TQ()
    .id("Your CREST application ID")
    .key("Your CREST application key")
    .build();

```

There are more options available to build a client. Please see [CrestClient.Builder](https://github.com/evanova/eve-crest-java/blob/master/retrofit/src/main/java/org/devfleet/crest/retrofit/CrestClient.java) for more details.

* Obtain a [CrestService](https://github.com/evanova/eve-crest-java/blob/master/api/src/main/java/org/devfleet/crest/CrestService.java) instance from a CrestClient
```
 //Public CREST access
 service = client.newCrestService();
 
 //Obtain a token from an authorization code 
 CrestToken token = client.fromAuthCode(authCode);
 
 //or an existing refresh token
 CrestToken token = client.fromRefresh(refreshToken);
 
 //use the refresh value
 //Authenticated CREST access
 service = client.newCrestService(token.getRefreshToken());
```

From then on, the CrestService will use the refresh token to get proper access to CREST and will take care of refreshing its access token as necessary.

* Interact with CREST

Once you obtained a `CrestService`, all methods in the interface are available to you.
```
 final List<CrestContact> contacts = service.getContacts();
 Assert.assertFalse("No contact found. Do you have any friend?", contacts.isEmpty());

 service.deleteContact(contact.getContact().getId());
 service.addContact(contact);
```


#### Testing
You will need to edit the `crest.properties` file in the test folder and fill it with the required information (see that file for details), then remove the Ignore tag on the tests. Run `./gradlew check` afterward.


#### Further documentation
[Eve Online 3rd Party documentation](https://eveonline-third-party-documentation.readthedocs.org/en/latest/)

#### Contributing

You are encouraged to add more `CrestService` method and/or add new CREST endpoints to the CREST Retrofit interface.
Ask around on [slack/#devfleet](https://tweetfleet.slack.com/messages/devfleet/) for details.

#### Dependencies

This library uses

[Jackson](https://github.com/FasterXML/jackson-core)

[Retrofit2](https://square.github.io/retrofit/)

[OkKHttp3](https://github.com/square/okhttp)

[OpenPojo](https://github.com/oshoukry/openpojo)
