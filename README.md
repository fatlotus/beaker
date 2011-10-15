Beaker: Another Automated Build System
======================================

With the plethora of dynamic scripting environments available today for the JVM, traditional build environments often have trouble keeping up with various standards and methods of project development. Beaker is designed, somewhat like [`brew`][homebrew-url], around the notion of a series of _packages_, each containing the necessary build instructions.

[homebrew-url]: http://homebrew.???.com

Setup
-----

Initial configuration of Beaker is really quite straightforward. With the [JDK][java-jdk-url] installed, you should be able to execute `javac` on the command line, like so:

[java-jdk-url]: http://java.oracle.com

	$ javac -version
	javac 1.6.0_26
	$ 

The Java system version, the number after the first period, is what denotes what release of Java you are using. Here I'm using Java 6. Various plugins will mandate different Java releases for your project.

Once you've successfully installed Java -- and are reasonably comfortable compiling Java code -- `cd` to the Beaker source directory (usually the one containing this file) and execute the following:

	$ cd path/to/beaker-release
	$ javac beaker/Build.java
	$ java beaker.Build
	+ build [java] OK
	+ build [jpython,jruby] OK
	+ symlink ./src
	$  

Beaker should now be installed!

But how do I build project X?
-----------------------------

Ah, that's the wonderful part! Building is merely a question of running, in the project directory:

	$ beaker build
	+ build [project-name]
	+ script [build-macosx-executable.sh]
	$ beaker launch
	... launches GUI program ...
	$ 

As an example, here is how to build and run Beaker with the sample JRuby project in `proj/`.

	$ javac beaker/Build.java
	$ java beaker.Build
	+ compile proj/Main.rb
	+ compile proj/HelloWorld.java
	$ java -cp path/to/jruby-complete.jar:. proj/Main
	Hello world!
	$