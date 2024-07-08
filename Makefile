# Java source directory
JAVA_SRC_DIR := src/java/com/xerdi/jkpathsea

# Output directory for Java classes
JAVA_OUTPUT_DIR := out/production/jkpathsea

# Native source directory
NATIVE_SRC_DIR := src/native

# Directory for generated JNI header
JNI_HEADER_DIR := $(NATIVE_SRC_DIR)

# Target for compiling Java source files
$(JAVA_OUTPUT_DIR)/com/xerdi/jkpathsea/KPathSea.class: $(JAVA_SRC_DIR)/KPathSea.java
	mkdir -p $(JAVA_OUTPUT_DIR)
	javac -d $(JAVA_OUTPUT_DIR) $(JAVA_SRC_DIR)/KPathSea.java

# Target for generating JNI header
$(JNI_HEADER_DIR)/jkpathsea.h: $(JAVA_OUTPUT_DIR)/com/xerdi/jkpathsea/KPathSea.class
	javah -d $(JNI_HEADER_DIR) -classpath $(JAVA_OUTPUT_DIR) com.xerdi.jkpathsea.KPathSea
	mv $(JNI_HEADER_DIR)/com_xerdi_jkpathsea_KPathSea.h $(JNI_HEADER_DIR)/jkpathsea.h

jkpathsea: clean $(JNI_HEADER_DIR)/jkpathsea.h
	$(MAKE) -C src/lib kpathsea
	$(MAKE) -C src/native libjkpathsea.so

clean:
	$(MAKE) -C src/lib clean
	$(MAKE) -C src/native clean
	rm -r out/production
	rm -f $(JNI_HEADER_DIR)/jkpathsea.h

# Target to clean up generated files
clean-all:
	$(MAKE) -C src/lib clean
	$(MAKE) -C src/native clean
	rm -r out/production
	rm -rf $(JAVA_OUTPUT_DIR)/com/xerdi/jkpathsea/KPathSea.class
	rm -f $(JNI_HEADER_DIR)/jkpathsea.h
