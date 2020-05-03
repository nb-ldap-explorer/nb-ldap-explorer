About
=====

LDAP Explorer / LDIF Editor
---------------------------

Maven-based NetBeans module for exploring LDAP services from within NetBeans. This modules does not replace a
professional LDAP client, rather it provides a simple and quick interface for basic interaction with LDAP services.

SSL Certificate Exception
-------------------------

This module hooks into the TLS/SSL infrastructure to allow connecting to LDAP servers with invalid/self-signed
certificates. It will not allow random connections, but let the user decide on a case-by-case basis whether or not a
connection should be established.

News
----
 * *03. May 2020: Version 0.15 released*
    * [Bugfix] Fix NullPointerException introduced by cancelable queries

 * *02. May 2020: Version 0.12 released (0.13 + 0.14 released to fix release issues)*
    * [Bugfix] Remove jdesktop dependency
    * [Bugfix] Don't log enviroment to not reveal password
    * [Bugfix] Drop JAXB usage, as module was removed from JDK after java 9
    * [Bugfix] Alias casing between keystore and trust settings was inconsistent and could lead to host names not cleared
    * [Feature] Make queries cancelable and show incremental results

 * *03. October 2016: Version 0.11 released*
    * [Bugfix] In case of a a hostname mismatch between target hostname and hostname in certificate, the target hostname will be displayed

 * *13. June 2015: Version 0.10 released*
    * [Bugfix] Handle defect certificate stores and XML structures more gracefully (#3, https://netbeans.org/bugzilla/show_bug.cgi?id=252846)
    * [Bugfix] Correct ordering of certificate chain
    * [Bugfix] Show/use correct certificate as the target certificate
    * [Bugfix] Fix calling from Swing EDT
    * [Feature] Indent certificate chain
    * [Feature] Tune certificate display: add SHA-256 fingerprint, display certificate serial as hexString


 * *15. October 2013: Version 0.9 released*
    * Correct XML Handling (solve problem with conflicting JAXB Classloaders)

 * *28. July 2012: Version 0.8 released*
    * Support for NetBeans 7.2 

 * *1.July 2012: Version 0.7 released*
    * Feature: On communication errors try to reestablish the connection instead of directly failing
    * Feature: Make use of PagedResultControl, to allow queries exceeding the server per-request-limits (for example default in active directory is 1000 items)
    * Feature: Remove "card blanche" SSL trust-manager (which removed all control over SSL certificates for the user) and add a trust-manager which asks the user for confirmation
    * Feature: Show not only the human readable translation of name of a ldap attribute, but also the untranslated name
    * Bugfix:  The whole netbeans gui was blocked when doing an expansive search operation (the search was done in the SWING EDT), the expansive operations where moved of the EDT und the GUI stays responsive
    * Bugfix:  Changes in the properties "Secure Socket Layers", "Authentication", "Bind", "Label", and "Timeout" did not trigger a save of the connection settings

* *6. March 2010 at 02:40 CET: Version 0.4 released*
    * Attributes are now sortable by clicking the column headings
    * Possible to give each LDAP server connection a label (issue #3)
    * Fixed NamingException when having more than one server connection / window open (issue #5)
    * Added connection timeout setting to LDAP server connection
    * More friendly attribute names
    * Support for Lotus Notes object classes (issue #6)
    * Silently accepts self-signed SSL certificates
    * Basic filtering

* *27. February 2010 at 00:17 CET: Version 0.3 released*
    * See [Roadmap] 

* *17. February 2010 at 01:11 CET: Version 0.2 released*
    * See [Roadmap] 

* *14. February 2010 at 12:35 CET: Version 0.1 released*
    * See [Roadmap] 
