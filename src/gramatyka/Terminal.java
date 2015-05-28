//Filip Czaplicki 359081
package gramatyka;

public class Terminal extends Symbol {
    public Terminal(char symbol) throws Exception {
        super(symbol);
        if (!Character.isLowerCase(symbol))
            throw new Exception("Terminal nie jest małą literą");
    }

    public static Terminal[] makeTerminals(String s) throws Exception {
        Terminal[] result = new Terminal[s.length()];
        for (int i = 0; i < s.length(); i++)
            result[i] = new Terminal(s.charAt(i));
        return result;
    }

    @Override
    public Symbol normalize(Nonterminal[] nonterminals) {
        return this;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public int number() {
        return (int) (symbol - 'a');
    }
}
