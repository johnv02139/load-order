# load-order

See the order in which your source files are loaded by Clojure.

## Implementation

This is just a few functions on top of
[clojure.tools.namespace](https://github.com/clojure/tools.namespace).

It provides all the real functionality.  I just wanted some slightly
different output.

## Usage

* Use `(loaded-namespaces)` to get an ordered list of namespaces
* Use `(loaded-files)` to get an ordered list of strings giving
  the relative path from the working directory to the source file
* Call `(print-loaded-files)` from the REPL to see the file strings
  printed neatly, one per line
