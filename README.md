# JShell via RMI

This project provides a custom JShell execution engine that can connect to an already-running remote JVM (host)
and execute code snippets in the target JVM using the Java RMI technology.

The client and the host must both have project's compiled library `chartsy-jshell.jar` on its classpaths.
### Technologies used
* [JShell](https://en.wikipedia.org/wiki/JShell)
* [Java RMI](https://en.wikipedia.org/wiki/Java_remote_method_invocation)
* Some cryptography for password hashing.

## Example usage
1. Start the host and run the following code somewhere from your codebase. The intention is to create an instance
of the remotely accessible [`JShellLink`](https://github.com/chartsyone/chartsy-jshell/blob/master/src/main/java/one/chartsy/jshell/JShellLink.java) object and binding it to the RMI registry.
```java
int port = 52099;
JShellLink.register(new JShellLinkFacade(port), LocateRegistry.createRegistry(port), false);
```

2. Launch JShell client with custom execution engine using the following command:
```
jshell -J-cp -J./lib/chartsy-jshell.jar --execution rmi:host(localhost),port(52099)
```
In the above command you may need to alter the `host` and `port` parameters to match the target JVM hostname and the port number
on which the RMI registry was created.

## (Un)security

It should be obvious that disastrous things can happen when bad people gain access to your JVM instance using JShell. To prevent bad things to happen
the RMI port should not be publicly accessible.

This project takes some additional security measures to prevent unauthorised access by incorporating a secret access key
that must be passed from the client to the host before JShell session can be established. The host verifies if the
hash of the access key provided by the client matches the hash stored on the host.

To enable this feature:
1. On the host side start the JVM with system property `chartsy.jshell.secret.hash` set to the hash of the access key
computed using the algorithm implemented in the [`PBKDF2`](https://github.com/chartsyone/chartsy-jshell/blob/master/src/main/java/one/chartsy/jshell/host/PBKDF2.java) class. This system property can be set either on the command line or programatically, for example, by executing:
```java
System.setProperty("chartsy.jshell.secret.hash", "22lEL0T36LRs313UEzoHg5iqwvV5rRqTzL4JfG4tbdc=");
```
2. On the client side launch the JShell passing an additional `secret` parameter to the execution engine:
```
jshell -J-cp -J./lib/chartsy-jshell.jar --execution rmi:host(localhost),port(52099),secret(password123)
```

## Known limitations

* The host I/O is not redirected to the JShell console, thus when executing methods like `System.out.println` or `e.printStackTrace` the results are visible only in the host VM logs and not on your screen.
* The remote runtime system flags (_`-Rflag`_) of the JShell command-line are ignored.

## Disclaimer

This project comes with absolutely no warranty. Use at your own risk. 
