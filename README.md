- Currently gets to the end and scrapes everything 
- Can't handle the one date that has a range like 7-8 and one with the year included (It was 2024 - the last availabe event)
- Make sure to include date filters in the actual application- user might not want to see that far 
- Figure out different locations eventually

- Fix the thread issue (I think it's chromedriver not closing):
[WARNING] thread Thread[AsyncHttpClient-1-1,5,com.caseybrugna.nyc_events.Main] was interrupted but is still alive after waiting at least 14999msecs
[WARNING] thread Thread[AsyncHttpClient-1-1,5,com.caseybrugna.nyc_events.Main] will linger despite being asked to die via interruption
[WARNING] thread Thread[AsyncHttpClient-1-2,5,com.caseybrugna.nyc_events.Main] will linger despite being asked to die via interruption
[WARNING] thread Thread[AsyncHttpClient-1-3,5,com.caseybrugna.nyc_events.Main] will linger despite being asked to die via interruption
[WARNING] thread Thread[AsyncHttpClient-1-4,5,com.caseybrugna.nyc_events.Main] will linger despite being asked to die via interruption
[WARNING] NOTE: 4 thread(s) did not finish despite being asked to via interruption. This is not a problem with exec:java, it is a problem with the running code. Although not serious, it should be remedied.
[WARNING] Couldn't destroy threadgroup org.codehaus.mojo.exec.ExecJavaMojo$IsolatedThreadGroup[name=com.caseybrugna.nyc_events.Main,maxpri=10]
java.lang.IllegalThreadStateException
    at java.lang.ThreadGroup.destroy (ThreadGroup.java:776)
    at org.codehaus.mojo.exec.ExecJavaMojo.execute (ExecJavaMojo.java:319)
    at org.apache.maven.plugin.DefaultBuildPluginManager.executeMojo (DefaultBuildPluginManager.java:126)
    at org.apache.maven.lifecycle.internal.MojoExecutor.doExecute2 (MojoExecutor.java:342)
    at org.apache.maven.lifecycle.internal.MojoExecutor.doExecute (MojoExecutor.java:330)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:213)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:175)
    at org.apache.maven.lifecycle.internal.MojoExecutor.access$000 (MojoExecutor.java:76)
    at org.apache.maven.lifecycle.internal.MojoExecutor$1.run (MojoExecutor.java:163)
    at org.apache.maven.plugin.DefaultMojosExecutionStrategy.execute (DefaultMojosExecutionStrategy.java:39)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:160)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:105)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:73)
    at org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build (SingleThreadedBuilder.java:53)
    at org.apache.maven.lifecycle.internal.LifecycleStarter.execute (LifecycleStarter.java:118)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:261)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:173)
    at org.apache.maven.DefaultMaven.execute (DefaultMaven.java:101)
    at org.apache.maven.cli.MavenCli.execute (MavenCli.java:910)
    at org.apache.maven.cli.MavenCli.doMain (MavenCli.java:283)
    at org.apache.maven.cli.MavenCli.main (MavenCli.java:206)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:62)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke (Method.java:566)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced (Launcher.java:283)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launch (Launcher.java:226)
    at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode (Launcher.java:407)
    at org.codehaus.plexus.classworlds.launcher.Launcher.main (Launcher.java:348)




