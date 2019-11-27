.PHONY: test clean kill

export LIBDIR  = $(CURDIR)/lib
export TARGDIR = $(CURDIR)/target

build:
	mvn compile

test:
	mvn package && cd $(LIBDIR) && $(MAKE) run

clean:
	clear && mvn clean && killall java
