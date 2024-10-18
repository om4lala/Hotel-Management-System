all= 
	javac CSCI24000_Spring24_FinalProject/Main.java
	javac CSCI24000_Spring24_FinalProject/Admin.java
	javac CSCI24000_Spring24_FinalProject/Manager.java
	javac CSCI24000_Spring24_FinalProject/User.java
	javac CSCI24000_Spring24_FinalProject/Authentication.java
	javac CSCI24000_Spring24_FinalProject/Room.java

run: all
    java CSCI24000_Spring24_FinalProject/Main

clean:
    rm -f *.class
