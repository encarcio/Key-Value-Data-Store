# Key-Value-Data-Store
A file-based key-value data store that supports the basic CRD(create, read and delete) operations.This data store is meant to be used as a local storage for one single process on one laptop. The data store can be exposed as a library to clients that can instantiate a class and work with the data store.

# Instructions
1. Application contains a REPL(Read,Eval,Print,Loop) based interactive commandline interface to test        various operations of data store library.
2. Application runs best on Windows OS as file path format may vary platform to platform.

# Important Features
1. Memory: Instead of loading whole file into memory which can be very large, single line is loaded for            processing purpose which makes data store operations very memory efficient.
2. Performance: Use of buffer for read and write to file is done instead of batch reading or writing.                   Using in memory data structure to verify key existence for various operations instead of                 accessing files. These two features greatly reduce response time of operations.
3. Thread-Safety: All operations of data store are synchronized so multiple threads can concurrently                       utilise it.

# Implementation Approach

