Facade Module
=============
This is an http proxy that intercept requests to a defined path to another web service, 
and allow to apply security and methods around the http request phases. 

Internally uses the http_proxy service 

defines a bean `urlFacade` that can be overridden to add multiple service mappings.



`urlFacade.urlsWrapped` is a Map that allow to map a path to an url 
```
<map>
<!-- map /facade/geostore/ path requests to http://localhost/geostore -->
<entry key="geostore" value="http://localhost/geostore" />
</map>
```

Can be used also to apply security rules to services protected by firewalls or not reachable from user's network.

Allows to define also customized proxies with custom callbacks mapped to the module
