//Filip Czaplicki 359081
package gramatyka;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<ContextFreeGrammar> examples = new ArrayList<>();

    public static void addExample(String T, String N, String[][] P) {
        ContextFreeGrammar result = null;
        try {
            result = new ContextFreeGrammar(T, N, P);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (result != null) {
            System.out.print(result);
            examples.add(result);
        }
    }

    public static void example1() {
        String T = "aab";
        String N = "SPQR";
        String[][] P = {{"P", "Q", "R"}, {"a", "aP", "aPb"}, {"b", "Qb", "aQb"}, {""}};
        addExample(T, N, P);
    }

    public static void example2() {
        String T = "ab";
        String N = "SPQR";
        String[][] P = {{"aP", "aQ", "aR"}, {"a", "aP", "aQ"}, {"b", "bQ", "aQ"}, {"b"}};
        addExample(T, N, P);
    }

    public static void example3() {
        String T = "ab";
        String N = "SPQR";
        String[][] P = {{"aP", "aQ", "aR"}, {"a", "aP", "aQ"}, {"b", "bQP", "aQ"}, {"b"}};
        addExample(T, N, P);
    }

    public static void example4() {
        String T = "ab";
        String N = "SPQR";
        String[][] P = {{"a", "b", "RR"}, {"a", "b", "QQ"}, {"QQ", "b", "PS"}, {"a"}};
        addExample(T, N, P);
    }

    public static void example5() {
        String T = "ab";
        String N = "SPQR";
        String[][] P = {{"a", "b"}, {"a", "b"}, {"b", "a"}, {"a"}};
        addExample(T, N, P);
    }

    public static void example6() {
        String T = "";
        String N = "A";
        String[][] P = {{"AA"}};
        addExample(T, N, P);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("--------OPISY GRAMATYK--------");

        example1();
        example2();
        example3();
        example4();
        example5();
        example6();

        System.out.println("--------OPISY GRAMATYK PRZEKSZTAŁCONYCH DO POSTACI GREIBACH--------");
        for (ContextFreeGrammar example : examples)
            try {
                System.out.print(example.toGreibach());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
    }
}