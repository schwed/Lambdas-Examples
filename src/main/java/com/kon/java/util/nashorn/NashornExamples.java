package com.kon.java.util.nashorn;

import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;

public class NashornExamples {

    public static NashornScriptEngine createEngine() {
        return (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
    }

}
