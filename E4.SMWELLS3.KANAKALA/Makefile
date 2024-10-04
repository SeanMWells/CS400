runFDTests: FrontendDeveloperTests.java
	javac -cp ../junit5.jar:. FrontendDeveloperTests.java
	java -jar ../junit5.jar --class-path=. --select-class=FrontendDeveloperTests

runApp: App.java
	javac App.java
	java App

clean:
	rmm-f *.class
