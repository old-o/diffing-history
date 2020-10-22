# diffing-history

A reusable Java implementation of Undo/Redo functionality using [State Diffs](https://odoepner.wordpress.com/2020/10/22/undo-redo-in-java-using-protostuff-serialization-and-binary-diffs/).

It uses these Open Source libraries:

- [Protostuff](https://github.com/protostuff/protostuff) for object graph serialization using runtime schema
- [JavaxDelta](https://github.com/NitorCreations/javaxdelta) for binary diffing and patching

It provides these features:

- Unlimited Undo and Redo
- Can handle any type of Java object graphs
- Straightforward type-safe API
- Supports stack size listeners
- Gzip compression for the serialized current state

It is Open Source under the Unlicense.

## Usage 

Currently this project is meant for cloning on github, or copy/pasting the code, not yet as a library on Maven Central.

The main API is the [History](src/main/java/net/doepner/hist/History.java) interface.

Create an instance of [DiffingHistory](src/main/java/net/doepner/hist/DiffingHistory.java) to get started.

The [DiffingHistoryTest](src/test/java/net/doepner/hist/DiffingHistoryTest.java) calls all History methods and illustrates the API.
