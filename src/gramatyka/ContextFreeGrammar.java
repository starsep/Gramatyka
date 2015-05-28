//Filip Czaplicki 359081
package gramatyka;

import java.util.*;

public class ContextFreeGrammar {
    protected final Terminal[] terminals;
    protected final Nonterminal[] nonterminals;
    protected final Production[] productions;

    public ContextFreeGrammar(Terminal[] terminals, Nonterminal[] nonterminals, Production[] productions) throws Exception {
        this.terminals = terminals;
        this.nonterminals = nonterminals;
        this.productions = productions;

        Arrays.sort(terminals);
        Arrays.sort(nonterminals);
        Arrays.sort(productions);

        for (int i = 0; i < terminals.length - 1; i++)
            if (terminals[i].compareTo(terminals[i + 1]) == 0)
                throw new Exception("Terminale się powtarzają");
        for (int i = 0; i < nonterminals.length - 1; i++)
            if (nonterminals[i].compareTo(nonterminals[i + 1]) == 0)
                throw new Exception("Nieterminale się powtarzają");
        for (int i = 0; i < productions.length - 1; i++)
            if (productions[i].compareTo(productions[i + 1]) == 0)
                throw new Exception("Produkcje się powtarzają");
    }

    public ContextFreeGrammar(String terminals, String nonterminals, String[][] productions) throws Exception {
        this(Terminal.makeTerminals(terminals), Nonterminal.makeNonterminals(nonterminals), Production.makeProductions(Nonterminal.makeNonterminals(nonterminals), productions));
    }

    public String toString() {
        String result = "";

        String type = ifRegular() ? "regularna" : "bezkontekstowa";
        String form = "";
        if (ifChomsky())
            form += "Chomsky";
        if (ifGreibach())
            form += "Greibach";
        result += "Gramatyka: " + type + "/" + form + "\n";

        result += "Terminale: ";
        for (Terminal t : terminals)
            result += t;
        result += "\n";

        result += "Nieterminale: ";
        for (Nonterminal n : nonterminals)
            result += n;
        result += "\n";

        result += "Produkcje:\n";
        for (Production p : productions)
            result += p + "\n";

        return result;
    }

    public boolean ifRegular() {
        boolean type1 = true;
        for (Production p : productions)
            type1 &= p.ifRegular(true);
        boolean type2 = true;
        for (Production p : productions)
            type2 &= p.ifRegular(false);
        return type1 || type2;
    }

    public boolean ifChomsky() {
        for (Production p : productions)
            if (!p.ifChomsky())
                return false;
        return true;
    }

    public boolean ifGreibach() {
        for (Production p : productions)
            if (!p.ifGreibach())
                return false;
        return true;
    }

    public NormalGreibachGrammar toGreibach() throws Exception {
        if (!ifChomsky())
            throw new Exception("Gramatyka nie jest w postaci Chomsky'ego");
        Terminal[] newTerminals = terminals;
        Nonterminal[] newNonterminals = Nonterminal.adjust(nonterminals);
        Production[] newProductions = new Production[productions.length];
        for (int i = 0; i < productions.length; i++)
            newProductions[i] = productions[i].normalize(nonterminals);
        ArrayList<ArrayList<Production>> productionsForNonterminal = new ArrayList<>();
        for (Nonterminal n : newNonterminals)
            productionsForNonterminal.add(new ArrayList<Production>());
        for (Production p : newProductions)
            productionsForNonterminal.get(p.lhs().number()).add(p);
        //pierwszy krok algorytmu
        for (ArrayList<Production> list : productionsForNonterminal)
            for (int i = 0; i < list.size(); i++) {
                Production p = list.get(i);
                if (p.rhs()[0].compareTo(p.lhs()) < 0) {
                    for (Production q : productionsForNonterminal.get(p.rhs()[0].number())) {
                        Symbol[] rhs = new Symbol[q.rhs().length + p.rhs().length - 1];
                        for (int j = 0; j < q.rhs().length; j++)
                            rhs[j] = q.rhs()[j];
                        for (int j = 0; j < p.rhs().length - 1; j++)
                            rhs[q.rhs().length + j] = p.rhs()[j + 1];
                        list.add(new Production(p.lhs(), rhs));
                    }
                    list.remove(p);
                }
            }
        //usuwanie rekursywnych produkcji
        for (ArrayList<Production> list : productionsForNonterminal) {
            ArrayList<Production> recursive = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Production p = list.get(i);
                if (p.rhs()[0].compareTo(p.lhs()) == 0) {
                    recursive.add(p);
                    list.remove(i);
                    i--;
                }
            }
            for (Production p : recursive) {
                Nonterminal pi = new Nonterminal((char) ((newNonterminals.length - 1 - p.lhs().number()) + 'A'));
                if (productionsForNonterminal.get(p.lhs().number()).size() == 0)
                    throw new Exception("Dla nieterminala " + p.lhs() + " nie ma reguły nierekurencyjnej");
                Symbol[] rhs = new Symbol[p.rhs().length - 1];
                for (int i = 0; i < rhs.length; i++)
                    rhs[i] = p.rhs()[i + 1];
                productionsForNonterminal.get(pi.number()).add(new Production(pi, rhs));
                rhs = new Symbol[p.rhs().length];
                for (int i = 0; i < rhs.length - 1; i++)
                    rhs[i] = p.rhs()[i + 1];
                rhs[rhs.length - 1] = pi;
                productionsForNonterminal.get(pi.number()).add(new Production(pi, rhs));
            }
        }
        //drugi krok algorytmu
        for (int i = productionsForNonterminal.size() - 1; i >= 0; i--)
            for (int j = 0; j < productionsForNonterminal.get(i).size(); j++) {
                Production p = productionsForNonterminal.get(i).get(j);
                if (!p.rhs()[0].isTerminal()) {
                    for (int ii = 0; ii < productionsForNonterminal.get(p.rhs()[0].number()).size(); ii++) {
                        Production q = productionsForNonterminal.get(p.rhs()[0].number()).get(ii);
                        Symbol[] rhs = new Symbol[p.rhs().length + q.rhs().length - 1];
                        for (int k = 0; k < q.rhs().length; k++)
                            rhs[k] = q.rhs()[k];
                        for (int k = 0; k < p.rhs().length - 1; k++)
                            rhs[q.rhs().length + k] = p.rhs()[k + 1];
                        productionsForNonterminal.get(i).add(new Production(p.lhs(), rhs));
                    }
                    productionsForNonterminal.get(i).remove(j);
                    j--;
                }
            }
        //Usuwanie powtórzeń produkcji
        for (List<Production> list : productionsForNonterminal) {
            Collections.sort(list);
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).compareTo(list.get(i + 1)) == 0) {
                    list.remove(i);
                    i--;
                }
            }
        }
        //stworzenie wyniku
        List<Production> resultProductions = new ArrayList<>();
        for (List<Production> list : productionsForNonterminal)
            for (Production p : list)
                resultProductions.add(p);
        newProductions = new Production[resultProductions.size()];
        newProductions = resultProductions.toArray(newProductions);
        ContextFreeGrammar result = new ContextFreeGrammar(newTerminals, newNonterminals, newProductions);
        if (!result.ifGreibach())
            throw new Exception("Konwersja nie przebiegła poprawnie");
        return new NormalGreibachGrammar(result);
    }
}
