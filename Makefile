# folder name of the package of interest
PKGNAME = remote
APPLICATION_PKGNAME = application

# where are all the source files for main package and test code
SRCFILES = $(PKGNAME)/*.java
TESTFILES = test/*.java test/*/*.java
APPLICATION = $(APPLICATION_PKGNAME)/*.java

# javadoc output directory and library url
DOCDIR = doc
DOCLINK = https://docs.oracle.com/en/java/javase/21/docs/api

.PHONY: build final checkpoint all clean docs docs-test
.SILENT: build final checkpoint all clean docs docs-test

# compile all Java files.
build:
	javac $(SRCFILES) $(TESTFILES)

# run conformance tests.
final: build
	java test.Lab1FinalTests

checkpoint: build
	java test.Lab1CheckpointTests

all: build
	java test.Lab1Tests
    
# delete all class files and docs, leaving only source
clean:
	rm -rf $(SRCFILES:.java=.class) $(TESTFILES:.java=.class) $(DOCDIR) $(DOCDIR)-test

# generate documentation for the package of interest
docs:
	javadoc -private -link $(DOCLINK) -d $(DOCDIR) $(PKGNAME) $(APPLICATION_PKGNAME)
	
# generate documentation for the test suite
docs-test:
	javadoc -private -link $(DOCLINK) -d $(DOCDIR)-test test test.util test.$(PKGNAME)

build-application: build
	javac $(APPLICATION)
clean-application: clean
	rm -rf $(APPLICATION:.java=.class)
run-application: build-application
	java $(APPLICATION_PKGNAME).ExampleSimulator
    
