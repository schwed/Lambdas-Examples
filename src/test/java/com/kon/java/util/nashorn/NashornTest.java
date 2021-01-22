package com.kon.java.util.nashorn;

import com.kon.java.util.lambdas.LambdaExamples;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.script.*;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.kon.java.util.nashorn.NashornExamples.createEngine;

@ExtendWith(MockitoExtension.class)
public class NashornTest {

    private static final Logger logger = Logger.getLogger(NashornTest.class.getName());

    @BeforeAll
    static void beforeAll() {
        System.out.println("A static method in your class that is called before all of its tests run");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("A static method in your test class that is after all od its test run");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("A method is called before each individual test rubs");
    }

    @AfterEach
    void afterEach() {
        System.out.println("A method is called after each individual test rubs");
    }

    /**
     * Calling javascript functions from java with nashorn.
     * @throws Exception
     */
    @Test
    @DisplayName("Nashorn1 test")
    void testNashorn1() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader("res/nashorn1.js"));

        Invocable invocable = (Invocable) engine;
        Object result = invocable.invokeFunction("fun1", "Peter Parker");
        System.out.println(result);
        System.out.println(result.getClass());

        invocable.invokeFunction("fun2", new Date());
        invocable.invokeFunction("fun2", LocalDateTime.now());
        invocable.invokeFunction("fun2", new LambdaExamples.Person());
    }

    @Test
    @DisplayName("Nashorn2 fun")
    void testNashorn2Fun(String name) throws Exception {
        System.out.format("Hi there from Java, %s", name);
        System.out.println( "greetings from java");
    }

    @Test
    @DisplayName("Nashorn2 fun2")
    void testNashorn2Fun2(Object object) throws Exception {
        System.out.println(object.getClass());
    }

    @Test
    @DisplayName("Nashorn2 fun3")
    void testNashorn2Fun3(ScriptObjectMirror mirror) throws Exception {
        System.out.println(mirror.getClassName() + ": " + Arrays.toString(mirror.getOwnKeys(true)));
    }

    @Test
    @DisplayName("Nashorn2 fun4")
    void testNashorn2Fun4(ScriptObjectMirror person) throws Exception {
        System.out.println("Full Name is: " + person.callMember("getFullName"));
    }

    @Test
    @DisplayName(" 2 test main")
    void testNashorn2Main() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader("res/nashorn2.js"));
    }

    /**
     * Working with java types from javascript.
     * @throws Exception
     */
    @Test
    @DisplayName("Nashorn 3 test main")
    void testNashorn3Main() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("load('res/nashorn3.js')");
    }

    /**
     * Bind java object to custom javascript objects
     * @throws Exception
     */
    @Test
    @DisplayName("Nashorn 5 test main")
    void testNashorn5Main() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("load('res/nashorn5.js')");

        Invocable invocable = (Invocable) engine;

        Product product = new Product();
        product.setName("Rubber");
        product.setPrice(1.99);
        product.setStock(1037);

        Object result = invocable.invokeFunction("getValueOfGoods", product);
        System.out.println(result);
    }

    /**
     * Using backbone models from nashorn
     * @throws Exception
     */

    public static void getProduct(ScriptObjectMirror result) {
        System.out.println(result.get("name") + ": " + result.get("valueOfGoods"));
    }

    @Test
    @DisplayName("Nashorn 6 test main")
    void testNashorn6Main() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("load('res/nashorn6.js')");

        Invocable invocable = (Invocable) engine;

        Product product = new Product();
        product.setName("Rubber");
        product.setPrice(1.99);
        product.setStock(1337);

        ScriptObjectMirror result = (ScriptObjectMirror)
                invocable.invokeFunction("calculate", product);
        System.out.println(result.get("name") + ": " + result.get("valueOfGoods"));
    }

    @Test
    @DisplayName("Nashorn 7 test main")
    void testNashorn7Main() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("function foo(predicate, obj) { return !!(eval(predicate)); };");

        Invocable invocable = (Invocable) engine;

        Person person = new Person();
        person.setName("Hans");

        String predicate = "obj.getLengthOfName() >= 4";
        Object result = invocable.invokeFunction("foo", predicate, person);
        System.out.println(result);
    }

    @Test
    @DisplayName("Nashorn 8 test main")
    void testNashorn8Main() throws Exception {
        NashornScriptEngine engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("load('res/nashorn8.js')");

        engine.invokeFunction("evaluate1");                             // [object global]
        engine.invokeFunction("evaluate2");                             // [object Object]
        engine.invokeFunction("evaluate3", "Foobar");                   // Foobar
        engine.invokeFunction("evaluate3", new Person("John", "Doe"));  // [object global] <- ???????
    }

    @Test
    @DisplayName("Nashorn 9 test main")
    void testNashorn9Main() throws Exception {
        NashornScriptEngine engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("load('res/nashorn9.js')");

        long t0 = System.nanoTime();

        double result = 0;
        for (int i = 0; i < 1000; i++) {
            double num = (double) engine.invokeFunction("testPerf");
            result += num;
        }

        System.out.println(result > 0);

        long took = System.nanoTime() - t0;
        System.out.format("Elapsed time: %d ms", TimeUnit.NANOSECONDS.toMillis(took));
    }

    @Test
    @DisplayName("Nashorn 10 test main")
    void testNashorn10Main() throws Exception {
        NashornScriptEngine engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("load('res/nashorn10.js')");

        long t0 = System.nanoTime();

        for (int i = 0; i < 100000; i++) {
            engine.invokeFunction("testPerf");
        }

        long took = System.nanoTime() - t0;
        System.out.format("Elapsed time: %d ms", TimeUnit.NANOSECONDS.toMillis(took));
    }

    @Test
    @DisplayName("Nashorn 11 test 1")
    void testNashorn11Test1() throws Exception {
        NashornScriptEngine engine = createEngine(); // defined in NashornExample class
        engine.eval("function foo() { print('bar') };");
        engine.eval("foo();");
    }

    @Test
    @DisplayName("Nashorn 11 test 2")
    void testNashorn11Test2() throws Exception {
        NashornScriptEngine engine = createEngine();
        engine.eval("function foo() { print('bar') };");

        SimpleScriptContext context = new SimpleScriptContext();
        engine.eval("print(Function);", context);
        engine.eval("foo();", context);
    }

    @Test
    @DisplayName("Nashorn 11 test 3")
    void testNashorn11Test3() throws Exception {
        NashornScriptEngine engine = createEngine();

        ScriptContext defaultContext = engine.getContext();
        Bindings defaultBindings = defaultContext.getBindings(ScriptContext.ENGINE_SCOPE);

        SimpleScriptContext context = new SimpleScriptContext();
        context.setBindings(defaultBindings, ScriptContext.ENGINE_SCOPE);

        engine.eval("function foo() { print('bar') };", context);
        engine.eval("foo();", context);

        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        System.out.println(bindings.get("foo"));
        System.out.println(context.getAttribute("foo"));
    }

    @Test
    @DisplayName("Nashorn 11 test 4")
    void testNashorn11Test4() throws Exception {
        NashornScriptEngine engine = createEngine();

        engine.eval("function foo() { print('bar') };");

        ScriptContext defaultContext = engine.getContext();
        Bindings defaultBindings = defaultContext.getBindings(ScriptContext.ENGINE_SCOPE);

        SimpleScriptContext context = new SimpleScriptContext();
        context.setBindings(defaultBindings, ScriptContext.ENGINE_SCOPE);

        engine.eval("foo();", context);
        System.out.println(context.getAttribute("foo"));
    }

    @Test
    @DisplayName("Nashorn 11 test 5")
    void testNashorn11Test5() throws Exception {
        NashornScriptEngine engine = createEngine();

        engine.eval("var obj = { foo: 'foo' };");
        engine.eval("function printFoo() { print(obj.foo) };");

        ScriptContext defaultContext = engine.getContext();
        Bindings defaultBindings = defaultContext.getBindings(ScriptContext.ENGINE_SCOPE);

        SimpleScriptContext context1 = new SimpleScriptContext();
        context1.setBindings(defaultBindings, ScriptContext.ENGINE_SCOPE);

        SimpleScriptContext context2 = new SimpleScriptContext();
        context2.setBindings(defaultBindings, ScriptContext.ENGINE_SCOPE);

        engine.eval("obj.foo = 'bar';", context1);
        engine.eval("printFoo();", context1);
        engine.eval("printFoo();", context2);
    }

    @Test
    @DisplayName("Nashorn 11 test 6")
    void testNashorn11Tes6() throws Exception {
        NashornScriptEngine engine = createEngine();

        ScriptContext defaultContext = engine.getContext();
        defaultContext.getBindings(ScriptContext.GLOBAL_SCOPE).put("foo", "hello");

        ScriptContext customContext = new SimpleScriptContext();
        customContext.setBindings(defaultContext.getBindings(ScriptContext.ENGINE_SCOPE), ScriptContext.ENGINE_SCOPE);

        Bindings bindings = new SimpleBindings();
        bindings.put("foo", "world");
        customContext.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);

//        engine.eval("foo = 23;");                 // overrides foo in all contexts, why???

        engine.eval("print(foo)");                  // hello
        engine.eval("print(foo)", customContext);   // world
        engine.eval("print(foo)", defaultContext);  // hello
    }

    @Test
    @DisplayName("Nashorn 11 test 7")
    void testNashorn11Test7() throws Exception {
        NashornScriptEngine engine = createEngine();

        engine.eval("var foo = 23;");

        ScriptContext defaultContext = engine.getContext();
        Bindings defaultBindings = defaultContext.getBindings(ScriptContext.ENGINE_SCOPE);

        SimpleScriptContext context1 = new SimpleScriptContext();
        context1.setBindings(defaultBindings, ScriptContext.ENGINE_SCOPE);

        SimpleScriptContext context2 = new SimpleScriptContext();
        context2.getBindings(ScriptContext.ENGINE_SCOPE).put("foo", defaultBindings.get("foo"));

        engine.eval("foo = 44;", context1);
        engine.eval("print(foo);", context1);
        engine.eval("print(foo);", context2);
    }

    @Test
    @DisplayName("Nashorn 11 test 8")
    void testNashorn11Test8() throws Exception {
        NashornScriptEngine engine = createEngine();

        engine.eval("var obj = { foo: 23 };");

        ScriptContext defaultContext = engine.getContext();
        Bindings defaultBindings = defaultContext.getBindings(ScriptContext.ENGINE_SCOPE);

        SimpleScriptContext context1 = new SimpleScriptContext();
        context1.setBindings(defaultBindings, ScriptContext.ENGINE_SCOPE);

        SimpleScriptContext context2 = new SimpleScriptContext();
        context2.getBindings(ScriptContext.ENGINE_SCOPE).put("obj", defaultBindings.get("obj"));

        engine.eval("obj.foo = 44;", context1);
        engine.eval("print(obj.foo);", context1);
        engine.eval("print(obj.foo);", context2);
    }

}
