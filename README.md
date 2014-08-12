OpenSDI-Manager2
================

This version of the OpenSDI-Manager differ by the [previous one](https://github.com/geosolutions-it/OpenSDI-Manager) because it use a different architecture: Instead of being a **single Java webapp** that implements **both frontend and backend services** in a single application the frontend layer and the backend services are splitted.

* the **backend** is a made up of a collection of services with a JSON output format implemented using Spring MVC
* the **frontend** is an extension of the current MapStore MapManager

See [this draft architecture document](https://docs.google.com/document/d/1tewEoDLVRZC7j-BHZjqR-ZevTQab8c0RwjJivC64Pd0/edit#) for more details.

OpenSDI-Manager is free and Open Source software, it is licensed under the terms of the ]GNU General Public License Version 3.0](http://www.gnu.org/licenses/gpl-3.0.txt).

