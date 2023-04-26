package io.vepo.graalvmjs;

import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;

public class GraalVmJsBug {

    public static record Pojo(String value1, int value2) {
    }

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        System.out.println("Test");

        var script = """
                     function fn(somePojo, someHashMap) {
                         print('' + somePojo.toString()); // It works fine
                         print('' + someHashMap.toString()); // It works fine
                         print('' + somePojo); // It works fine
                         print('' + someHashMap); // Throws NullPointerException
                         return 'true';
                     }
                     """;

        var pojo = new Pojo("value-1", 2);
        var map = Map.of("pojo", new Pojo("value-3", 4));

        // Running with Nashorn
        System.out.println("\n\nRunning with Nashorn:");
        var nashornEngine = new ScriptEngineManager().getEngineByName("nashorn");
        nashornEngine.eval(script);

        // Running with GraalVM.js
        System.out.println("\n\nRunning with GraalVM.js:");
        ((Invocable) nashornEngine).invokeFunction("fn", pojo, map);

        var graalJsEngine = GraalJSScriptEngine.create(null, Context.newBuilder("js")
                                                                    .allowHostAccess(HostAccess.ALL)
                                                                    .allowHostClassLookup(s -> true)
                                                                    .allowExperimentalOptions(true)
                                                                    .option("js.nashorn-compat",
                                                                            "true"));
        graalJsEngine.eval(script);
        ((Invocable) graalJsEngine).invokeFunction("fn", pojo, map);

    }
}
