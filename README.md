OpenSDI-Manager2
================

This version of the OpenSDI-Manager differ by the [previous one](https://github.com/geosolutions-it/OpenSDI-Manager) because it use a different architecture: Instead of being a **single Java webapp** that implements **both frontend and backend services** in a single application the frontend layer and the backend services are splitted.

* the **backend** is a made up of a collection of services with a JSON output format implemented using Spring MVC
* the **frontend** is an extension of the current MapStore MapManager

See [this draft architecture document](https://docs.google.com/document/d/1tewEoDLVRZC7j-BHZjqR-ZevTQab8c0RwjJivC64Pd0/edit#) for more details.

OpenSDI-Manager is free and Open Source software, it is licensed under the terms of the [GNU General Public License Version 3.0](http://www.gnu.org/licenses/gpl-3.0.txt).

Build and run the application
-----------------------------

Let's Assume you have cloned the repo in the location **/OpenSDI-Manager2**

`$/OpenSDI-Manager2/src # mvn clean install` Build the war file and install it in the local m2 repo

`$/OpenSDI-Manager2/src # mvn clean eclipse:eclipse` Generate the eclipse project files

`$/OpenSDI-Manager2/src/web # mvn jetty:run` Run locally the latest war built using jetty

`$/OpenSDI-Manager2/src/web # mvn jetty:run -DOSDI_CONFIG_DIR=/work/OPENSDI2_CONFIG` Run locally the application specifing a datadir location

`$/OpenSDI-Manager2/src/web # mvnDebug jetty:run -DOSDI_CONFIG_DIR=/work/OPENSDI2_CONFIG` Run locally in debug mode, dt_socket is listening on port 8000, the configure remote application Starter in eclipse

Base application URL `http://localhost:9191/opensdi2-manager` (you have to add also `/mvc/A_NAME_OF_A_CONTROLLER` to see something happen)

Currently available profiles: **geocollect**



