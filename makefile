.PHONY: run clean kill

BUILDER = mvn
EXECUTE = java -jar $(jar)&

LIBDIR = $(CURDIR)/lib
TARDIR = $(CURDIR)/target

TARGETS = $(LIBDIR)/servidor.jar $(TARDIR)/archwizardduel-1.0-jar-with-dependencies.jar \
		  $(TARDIR)/archwizardduel-1.0-jar-with-dependencies.jar

all:
	$(MAKE) kill ; $(MAKE) clean
	$(BUILDER) package
	$(MAKE) run

run:
	$(foreach jar, $(TARGETS), $(EXECUTE))

kill:
	@killall java

clean:
	$(MAKE) kill ; $(BUILDER) clean
	@rm -f netgames.log