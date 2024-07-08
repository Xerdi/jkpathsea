MIRROR ?= https://mirrors.ctan.org
TL_VERSION ?= 20240311

TL_TARBALL = texlive-$(TL_VERSION)-source.tar.xz
TL_SRC_DIR = texlive-$(TL_VERSION)-source
TL_TARGET_DIR = texlive-source
KPATHSEA_DIR = $(TL_TARGET_DIR)/texk/kpathsea

# Define a generic pattern rule for downloading the source tarball
$(TL_TARBALL):
	wget $(MIRROR)/systems/texlive/Source/$(TL_TARBALL)

# Define a generic pattern rule for extracting the source tarball
$(TL_SRC_DIR): $(TL_TARBALL)
	tar -xvf $(TL_TARBALL)

# Define a rule for configuring and making the Kpathsea library
.PHONY: kpathsea
kpathsea: $(TL_TARGET_DIR)
	cd $(KPATHSEA_DIR) && ./configure && make

# Define the main target
.PHONY: $(TL_TARGET_DIR)
$(TL_TARGET_DIR): $(TL_SRC_DIR)
	mv $(TL_SRC_DIR) $(TL_TARGET_DIR)

# Define the clean target
.PHONY: clean
clean:
	rm -rf $(TL_TARGET_DIR) $(TL_TARBALL)
