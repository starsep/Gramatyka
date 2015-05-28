//Filip Czaplicki 359081
package gramatyka;

public abstract class Symbol implements Comparable<Symbol> {
    protected final char symbol;

    public Symbol(char symbol) {
        this.symbol = symbol;
    }

    public abstract Symbol normalize(Nonterminal[] nonterminals) throws Exception;

    public abstract boolean isTerminal();

    public abstract int number();

    @Override
    public String toString() {
        return "" + symbol;
    }

    @Override
    public int compareTo(Symbol s) {
        return new Character(this.symbol).compareTo(s.symbol);
    }
}
