## Test project for creating / adding SSL contexts. 
Various services require ssl connections, and make their trusted cert available as Base64 encoded data.

Although most of the connection can be configured and left to other starters, often there is no way to 
configure trusted certs for a service via a property, it is either assumed the user has handled this via
the system (or other appropriate) truststore, or that they will tailor the ssl configuration manually in the
app to configure any ssl context or ssl socket factory required.

This project is an attempt to provide an extensible way for services to support trusted ssl connections
via certificate information from application properties. 

Initial support is prototyped for the Databases For MongoDB service in IBM Cloud. The eventual intent being
that a java-cfenv processor would read the cert from the vcap_services entry, and make it available as the 
appropriate property, causing this project to then tailor a custom MongoClientOptions bean to add the SSL 
configuration. 

Still very bit&pieces.. will make more sense as this project comes together. 

All basic concepts are proven and working, 