package dev.features;
import java.util.List;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
 
/**
 *
 * @author ptremblett
 */
public class ListScriptingEngines {
 
    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> engines = manager.getEngineFactories();
        if (engines.isEmpty()) {
            System.out.println("No scripting engines were found");
            return;
        }
        System.out.println("The following " + engines.size() +
            " scripting engines were found");
        System.out.println();
        for (ScriptEngineFactory engine : engines) {
            System.out.println("Engine name: " + engine.getEngineName());
            System.out.println("\tVersion: " + engine.getEngineVersion());
            System.out.println("\tLanguage: " + engine.getLanguageName());
            List<String> extensions = engine.getExtensions();
            if (extensions.size() > 0) {
                System.out.println("\tEngine supports the following extensions:");
                for (String e : extensions) {
                    System.out.println("\t\t" + e);
                }
            }
            List<String> shortNames = engine.getNames();
            if (shortNames.size() > 0) {
                System.out.println("\tEngine has the following short names:");
                for (String n : engine.getNames()) {
                    System.out.println("\t\t" + n);
                }
            }
            System.out.println("=========================");
        }
    }
}