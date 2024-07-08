MIRROR ?= https://mirrors.ctan.org
TL_VERSION ?= 20240311

TL_TARBALL = texlive-$(TL_VERSION)-source.tar.xz
TL_SRC_DIR = texlive-$(TL_VERSION)-source
TL_TARGET_DIR = texlive-source
KPATHSEA_DIR = $(TL_TARGET_DIR)/texk/kpathsea

# Target to download the tarball if it doesn't exist
$(TL_TARBALL):
	wget $(MIRROR)/systems/texlive/Source/$(TL_TARBALL)

# Extract the tarball if the source directory doesn't exist
$(TL_SRC_DIR): $(TL_TARBALL)
	tar -xvf $(TL_TARBALL)

# Create symbolic link to TexLive source directory if not already linked
$(TL_TARGET_DIR): $(TL_SRC_DIR)
	ln -sfn $(abspath $(TL_SRC_DIR)) $(abspath $(TL_TARGET_DIR))

# Build kpathsea library if .libs directory doesn't exist
$(KPATHSEA_DIR)/.libs: $(TL_TARGET_DIR)
	cd $(KPATHSEA_DIR) && \
		if [ ! -f Makefile ]; then ./configure; fi && \
		make

# Target to build kpathsea
kpathsea: $(KPATHSEA_DIR)/.libs

# Clean target to remove target directory
.PHONY: clean
clean:
	rm -rf $(TL_TARGET_DIR)

# Clean-all target to remove source tarball and target directory
.PHONY: clean-all
clean-all: clean
	rm -f $(TL_TARBALL)
