runTests: runBDTests runFDTests

runBDTests: BackendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar BackendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c BackendDeveloperTests

clean:
	rm -f *.class

runApp: App.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar App.java
	java --module-path ../javafx/lib --add-modules javafx.controls App

runFDTests:
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests
