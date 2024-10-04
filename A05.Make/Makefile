run: App.class
	java App

Mercury:
	@echo "Hello Mercury"

App.class: App.java
	javac App.java

testCheck: App.class TestApp.java
	javac -cp ../junit5.jar:. TestApp.java

test: testCheck
	java -jar ../junit5.jar --class-path=. --select-class=TestApp
