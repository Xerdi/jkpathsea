# Java source directory
JAVA_SRC_DIR := src/java/com/xerdi/jkpathsea

# Output directory for Java classes
JAVA_OUTPUT_DIR := out/production/jkpathsea

# Native source directory
NATIVE_SRC_DIR := src/native

# Directory for generated JNI header
JNI_HEADER_DIR := $(NATIVE_SRC_DIR)

# Target for compiling Java source files
$(JAVA_OUTPUT_DIR)/com/xerdi/jkpathsea/KPathSeaNative.class: $(JAVA_SRC_DIR)/KPathSeaNative.java
	mkdir -p $(JAVA_OUTPUT_DIR)
	javac -h $(JAVA_OUTPUT_DIR) -d $(JAVA_OUTPUT_DIR) $(JAVA_SRC_DIR)/*.java

# Target for generating JNI header
$(JNI_HEADER_DIR)/jkpathsea.h: $(JAVA_OUTPUT_DIR)/com/xerdi/jkpathsea/KPathSeaNative.class
	mv $(JAVA_OUTPUT_DIR)/com_xerdi_jkpathsea_KPathSeaNative.h $(JNI_HEADER_DIR)/jkpathsea.h

jkpathsea: clean $(JNI_HEADER_DIR)/jkpathsea.h
	$(MAKE) -C src/lib kpathsea
	$(MAKE) -C src/native libjkpathsea.so

clean:
	$(MAKE) -C src/lib clean
	$(MAKE) -C src/native clean
	rm -rf out/production
	rm -f $(JNI_HEADER_DIR)/jkpathsea.h
