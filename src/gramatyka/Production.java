//Filip Czaplicki 359081
package gramatyka;

import java.util.ArrayList;

public class Production implements Comparable<Production> {
    protected final Nonterminal lhs;
    protected final Symbol[] rhs;

    public Production(Nonterminal lhs, Symbol[] rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public static Production[] makeProductions(Nonterminal[] nonterminals, String[][] productions) throws Exception {
        ArrayList<Production> result = new ArrayList<>();
        if (nonterminals.length != productions.length)
            throw new Exception("Błędna liczba nieterminali");
        for (int i = 0; i < nonterminals.length; i++)
            for (int j = 0; j < productions[i].length; j++) {
                Symbol[] rhs = new Symbol[productions[i][j].length()];
                for (int k = 0; k < rhs.length; k++)
                    rhs[k] = Character.isLowerCase(productions[i][j].charAt(k)) ? new Terminal(productions[i][j].charAt(k)) : new Nonterminal(productions[i][j].charAt(k));
                result.add(new Production(nonterminals[i], rhs));
            }
        Production[] res = new Production[result.size()];
        res = result.toArray(res);
        return res;
    }

    public Production normalize(Nonterminal[] nonterminals) throws Exception {
        Nonterminal lhs = (Nonterminal) this.lhs.normalize(nonterminals);
        Symbol[] rhs = new Symbol[this.rhs.length];
        for (int i = 0; i < rhs.length; i++)
            rhs[i] = this.rhs[i].normalize(nonterminals);
        return new Production(lhs, rhs);
    }

    public Nonterminal lhs() {
        return lhs;
    }

    public Symbol[] rhs() {
        return rhs;
    }

    @Override
    public String toString() {
        String result = lhs + " -> ";
        for (Symbol s : rhs)
            result += s;
        if (rhs.length == 0)
            result += "&";
        return result;
    }

    @Override
    public int compareTo(Production p) {
        if (lhs.compareTo(p.lhs) != 0)
            return lhs.compareTo(p.lhs);
        for (int i = 0; i < Math.min(rhs.length, p.rhs.length); i++)
            if (rhs[i].compareTo(p.rhs[i]) != 0)
                return rhs[i].compareTo(p.rhs[i]);
        return new Integer(rhs.length).compareTo(p.rhs.length);
    }

    public boolean ifGreibach() {
        if (rhs.length == 0 || !rhs[0].isTerminal())
            return false;
        for (int i = 1; i < rhs.length; i++)
            if (rhs[i].isTerminal())
                return false;
        return true;
    }

    public boolean ifChomsky() {
        switch (rhs.length) {
            case 1:
                return rhs[0].isTerminal();
            case 2:
                return !rhs[0].isTerminal() && !rhs[1].isTerminal();
            default:
                return false;
        }
    }

    public boolean ifRegular(boolean firstType) {
        switch (rhs.length) {
            case 0:
                return true;
            case 1:
                return rhs[0].isTerminal();
            case 2:
                return firstType
                        ? rhs[0].isTerminal() && !rhs[1].isTerminal()
                        : !rhs[0].isTerminal() && rhs[1].isTerminal();
            default:
                return false;
        }
    }
}
