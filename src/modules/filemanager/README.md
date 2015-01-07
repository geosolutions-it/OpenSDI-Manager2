File Manager Module
===================

Expose base classes and services to provide file browsing functionalities via web interface.

This module uses the opensdi manager configuration system. By default it gets the parameters scopeId and instanceId from the HTTP request. 
If they are not present the module will get
* the path of the file manager controller (`fileManager`) as **scopeId**
* `default` as instance Id. 

So to create a default instance of the fileManager you have to create the file
 * `mod_fileManager/config_default.properties`

```
rootDir=<path_to_base_dir>
```

If you want to create custom instances of the file manager you have to create a file like
 * `mod_fileManager/config_<my_instance_name>`
 
and write the rootDir property for it as above. The fileManager requests will need also the paramater 
* `instanceId=<my_instance_name>`


