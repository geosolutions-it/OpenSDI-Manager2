OpenSDI Manager Core
====================

Contains common functionalities, services and model classes for OpenSDI Manager. 

Configuration
*************

The configuration system has a basic implementation based on properties files. That can store one of more instances of a configuration object (as key/value pairs).

By default the application creates the configuration directory in the working directory under path

* `CONFIG_DIR/propertiesConfigurations`

You can modify the CONFIG_DIR path using the system variable OSDI_CONFIG_DIR (e.g. -DOSDI_CONFIG_DIR=/work/OPENSDI2_CONFIG ) or using the confiuration overrider (`opensdi-config-ovr.properties`)
