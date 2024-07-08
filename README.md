# JKPathSea
An experimental project using JNI to bring KPathSea to Java.

## Usage
To setup the project, run:
```sh
make jkpathsea
```
This will
1. download TeX Live source and compile `libkpathsea.so`
2. generate a new C header file, based on `KPathSea.java`
3. compile the `libjkpathsea.so` used by `KPathSea.java`

After that is sorted out, you can run the IntelliJ run configuration.
If you need to set it up manually, don't forget to add the VM option `-Djava.library.path=/path/to/jkpathsea/src/native/`

## License
This project is licensed under the LPPL version 1.3c and maintained by Erik Nijenhuis. See [LICENSE.txt](LICENSE.txt) for more information.
