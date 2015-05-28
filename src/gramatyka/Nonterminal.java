//Filip Czaplicki 359081
package gramatyka;

import java.util.Arrays;

public class Nonterminal extends Symbol {
    public Nonterminal(char symbol) throws Exception {
        super(symbol);
        if (!Character.isUpperCase(symbol))
            throw new Exception("Nieterminal nie jest wielką literą");
    }

    public static Nonterminal[] makeNonterminals(String s) throws Exception {
        Nonterminal[] result = new Nonterminal[s.length()];
        for (int i = 0; i < s.length(); i++)
            result[i] = new Nonterminal(s.charAt(i));
        return result;
    }

    public static Nonterminal[] adjust(Nonterminal[] nonterminals) throws Exception {
        if (nonterminals.length * 2 > 26)
            throw new Exception("Zabrakło wielkich liter");
        Nonterminal[] result = new Nonterminal[nonterminals.length * 2];
        for (int i = 0; i < result.length; i++)
            result[i] = new Nonterminal((char) ('A' + i));
        return result;
    }

    @Override
    public Symbol normalize(Nonterminal[] nonterminals) throws Exception {
        int k = nonterminals.length + Arrays.binarySearch(nonterminals, this);
        return new Nonterminal((char) ('A' + k));
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int number() {
        return (int) (symbol - 'A');
    }
}
