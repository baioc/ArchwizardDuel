.PHONY: run

EXECUTE = java -jar $(jar)&

TARGETS = servidor.jar \
          $(TARGDIR)/archwizardduel-1.0-jar-with-dependencies.jar \
          $(TARGDIR)/archwizardduel-1.0-jar-with-dependencies.jar

run:
	$(foreach jar, $(TARGETS), $(EXECUTE))
