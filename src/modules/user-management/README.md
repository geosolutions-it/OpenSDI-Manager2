User Manager
============
Provides advanced features for user expiring and messaging (via email) . 
Actually the only implementation uses _GeoStore_ users. 
The email dependency should be externalized to a separate module and the implementation have to 
be separated from the interface. Expiring task should be Configurable using the configuration system.
Actually the interval of running and the task enabling is available at runtime. 

In the future this tool can contain user management of the application itself. 
