package arcturus.parser;

import arcturus.lexical.Token;
import arcturus.lexical.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * An example of the parser of
 * E -> T + E | T
 * T -> int * T | int | (E)
 */
public class RecursiveDescentParser implements Parser {
    private ArrayList<Token> tokenList;
    private int current;

    @Override
    public boolean parse(List<Token> tokens) {
        tokenList = new ArrayList<>(tokens);
        current = 0;
        return E() && current >= tokenList.size();
    }

    private boolean E() {
        int saveInE = current;
        return E1() || ((Supplier<Boolean>) () -> {
            current = saveInE;
            return E2();
        }).get();
    }

    private boolean E1() {
        return T() && terminal(TokenType.PLUS_OPERATOR) && E();
    }

    private boolean E2() {
        return T();
    }

    private boolean T() {
        int saveInT = current;
        return T1() || ((Supplier<Boolean>) () -> {
            current = saveInT;
            return T2();
        }).get() || ((Supplier<Boolean>) () -> {
            current = saveInT;
            return T3();
        }).get();
    }

    private boolean T1() {
        return terminal(TokenType.INTEGER) && terminal(TokenType.TIMES_OPERATOR) && T();
    }

    private boolean T2() {
        return terminal(TokenType.INTEGER);
    }

    private boolean T3() {
        return terminal(TokenType.OPEN_PAREN) && E() && terminal(TokenType.CLOSE_PAREN);
    }

    private boolean terminal(TokenType type) {
        Token next = getNextToken();
        if (next == null) return false;
        return next.isType(type);
    }

    private Token getNextToken() {
        if (current >= tokenList.size()) return null;
        while (tokenList.get(current).isType(TokenType.WHITESPACE)) current++;
        if (current >= tokenList.size()) return null;
        Token next = tokenList.get(current);
        current++;
        return next;
    }

    public static void main(String[] args) {
        List<Token> list = new ArrayList<>();
        list.add(new Token(TokenType.INTEGER, 3));
        list.add(new Token(TokenType.TIMES_OPERATOR));
        list.add(new Token(TokenType.OPEN_PAREN));
        list.add(new Token(TokenType.INTEGER, 1));
        list.add(new Token(TokenType.PLUS_OPERATOR));
        list.add(new Token(TokenType.INTEGER, 2));
        list.add(new Token(TokenType.CLOSE_PAREN));
        Parser parser = new RecursiveDescentParser();
        System.out.println(parser.parse(list));
    }
}
