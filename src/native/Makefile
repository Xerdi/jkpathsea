# Adjust these variables as needed
JAVA_HOME = $(shell dirname $$(dirname $$(readlink -f $$(which javac))))
TL_DIR = ../lib/texlive-source/texk

# JNI header location
JNI_INCLUDE = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

# Kpathsea library location and linking
KPATHSEA_LIB = -L$(TL_DIR)/kpathsea/.libs -lkpathsea

# Compiler flags
CFLAGS = -z defs -Wall -fPIC -shared

# Target for building the JNI library
libjkpathsea.so: jkpathsea.c
	gcc $(CFLAGS) $(JNI_INCLUDE) -I$(TL_DIR) -o $@ $^ $(KPATHSEA_LIB)

.PHONY: clean
clean:
	rm -f libjkpathsea.so
