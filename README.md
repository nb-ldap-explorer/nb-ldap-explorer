About
=====

Maven-based NetBeans module for exploring LDAP services from within NetBeans. This modules does not replace a professional LDAP client, rather it provides a simple and quick interface for basic interaction with LDAP services.

Installation
------------

You can install the module by downloading the NBM file or through the this update center: http://nb-ldap-explorer.googlecode.com/files/updates.xml.gz

_Note: From version 0.3 an update center was automatically created upon installation. To upgrade simply go to Tools - Plugins - Updates and click "Reload Catalog" followed by selecting the new version and clicking "Upgrade"_  

News
----
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
