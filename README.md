# GraalVM.js Bug


The following script is throwing an exception. To validate, run [GraalVmJsBug](src\main\java\io\vepo\graalvmjs\GraalVmJsBug.java).

```javascript
function fn(somePojo, someHashMap) {
    print('' + somePojo.toString()); // It works fine
    print('' + someHashMap.toString()); // It works fine
    print('' + somePojo); // It works fine
    print('' + someHashMap); // Throws org.graalvm.polyglot.PolyglotException
    return 'true';
}
```