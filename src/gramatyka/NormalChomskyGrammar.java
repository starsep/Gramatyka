//Filip Czaplicki 359081
package gramatyka;

public class NormalChomskyGrammar extends ContextFreeGrammar {
    public NormalChomskyGrammar(Terminal[] terminals, Nonterminal[] nonterminals, Production[] productions) throws Exception {
        super(terminals, nonterminals, productions);
    }

    public NormalChomskyGrammar(String terminals, String nonterminals, String[][] productions) throws Exception {
        super(Terminal.makeTerminals(terminals), Nonterminal.makeNonterminals(nonterminals), Production.makeProductions(Nonterminal.makeNonterminals(nonterminals), productions));
    }

    public NormalChomskyGrammar(ContextFreeGrammar grammar) throws Exception {
        super(grammar.terminals, grammar.nonterminals, grammar.productions);
    }

    @Override
    public boolean ifChomsky() {
        return true;
    }
}
