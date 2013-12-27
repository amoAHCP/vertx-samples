vertx-samples
=============

Samples Repository for JacpFX and vertx in Java.
------------------------------------------------

### JacpFX, Vert.x, Java 8 Remote Drawing example.
 This example uses JacpFX 2.0 alpha! This version should be quite stable, but not all UnitTests are finished an not all controls are included. The user API is final, so you should not expect any changes here.

**How to run the example**
- install Java 8
- start the RemoteDrawingServer Verticle (see: [Vert.x documentation ](http://vertx.io/core_manual_java.html) )
- if you want to start the RemoteDrawingServer directly from Eclipse, IntelliJ, NetBeans do following:
    - Create an Application config with the Main class: **org.vertx.java.platform.impl.cli.Starter**
    - add Programm arguments:  **run org.jacpfx.server.RemoteDrawingServer**
    - use Java 8
    - for IntelliJ useres see: http://codepitbull.wordpress.com/2013/07/18/vert-x-in-intellij-idea/
- when the DrawingServier is running you can start some ApplicationMain class(es), connect to server and draw.



