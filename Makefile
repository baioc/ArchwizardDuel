.PHONY: run clean kill

export BUILDER = mvn

export LIBDIR  = $(CURDIR)/lib
export TARGDIR = $(CURDIR)/target

all:
	$(MAKE) kill ; $(MAKE) clean
	$(BUILDER) package
	cd $(LIBDIR) && $(MAKE) run

kill:
	@killall java

clean:
	$(MAKE) kill ; $(BUILDER) clean
	@rm -f netgames.log
