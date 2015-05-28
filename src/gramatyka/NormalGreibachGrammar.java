//Filip Czaplicki 359081
package gramatyka;

public class NormalGreibachGrammar extends ContextFreeGrammar {
    public NormalGreibachGrammar(Terminal[] terminals, Nonterminal[] nonterminals, Production[] productions) throws Exception {
        super(terminals, nonterminals, productions);
    }

    public NormalGreibachGrammar(String terminals, String nonterminals, String[][] productions) throws Exception {
        super(Terminal.makeTerminals(terminals), Nonterminal.makeNonterminals(nonterminals), Production.makeProductions(Nonterminal.makeNonterminals(nonterminals), productions));
    }

    public NormalGreibachGrammar(ContextFreeGrammar grammar) throws Exception {
        super(grammar.terminals, grammar.nonterminals, grammar.productions);
    }

    @Override
    public boolean ifGreibach() {
        return true;
    }
}
