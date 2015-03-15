SOURCE  = *.java players/*.java poker/*.java
CLASSES = *.class players/*.class poker/*.class
JARFILE = poker.jar

all: classes

classes:
	javac $(SOURCE)

jar: classes
	jar -cvfm $(JARFILE) MANIFEST.MF $(CLASSES) images/*.png && mv $(JARFILE) ..

clean:
	rm -f $(CLASSES)

# vim:noet
