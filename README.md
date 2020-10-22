# diffing-history

A reusable Java implementation of Undo/Redo functionality using State Diffs.

It uses the following Open Source libraries:

- Protostuff for object graph serialization using runtime schema
- JavaxDelta for binary diffing and patching

It provides the following features:

- Unlimited Undo and Redo
- Can handle any type of Java objects
- Straightforward type-safe API
- Supports stack size listeners
- Gzip compression for the serialized current state

It is Open Source under the Unlicense.

## Usage 

The main API is the History interface:
See src/main/java/net/doepner/hist/History.java

Create an instance of DiffingHistory to get started.
