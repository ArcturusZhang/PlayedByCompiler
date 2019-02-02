package arcturus.parser;

import arcturus.lexical.Token;
import arcturus.lexical.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * An example of the parser of
 * E -> T | T + E
 * T -> int | int * T | (E)
 */
public class Parser {
    private ArrayList<Token> tokenList;
    private int current;
    public Parser() {
    }

    public boolean parse(List<Token> tokens) {
        tokenList = new ArrayList<>(tokens);
        current = 0;
        return E();
    }

    private boolean E() {
        int save = current;
        return E1(save) || E2(save);
    }

    private boolean E1(int save) {
        current = save;
        return T();
    }

    private boolean E2(int save) {
        current = save;
        return T()
                && (current < tokenList.size() && tokenList.get(current++).isType(TokenType.PLUS_OPERATOR))
                && E();
    }

    private boolean T() {
        int save = current;
        return T1(save) || T2(save) || T3(save);
    }

    private boolean T1(int save) {
        current = save;
        return current < tokenList.size() && tokenList.get(current++).isType(TokenType.INTEGER);
    }

    private boolean T2(int save) {
        current = save;
        return (current < tokenList.size() && tokenList.get(current++).isType(TokenType.INTEGER))
                && (current < tokenList.size() && tokenList.get(current++).isType(TokenType.TIMES_OPERATOR))
                && T();
    }

    private boolean T3(int save) {
        current = save;
        return (current < tokenList.size() && tokenList.get(current++).isType(TokenType.OPEN_PARENTESIS))
                && E()
                && (current < tokenList.size() && tokenList.get(current++).isType(TokenType.CLOSE_PARENTESIS));
    }

    public static void main(String[] args) {
        List<Token> list = new ArrayList<>();
        list.add(new Token(TokenType.INTEGER, 3));
        list.add(new Token(TokenType.TIMES_OPERATOR));
        list.add(new Token(TokenType.OPEN_PARENTESIS));
        list.add(new Token(TokenType.INTEGER, 1));
        list.add(new Token(TokenType.PLUS_OPERATOR));
        list.add(new Token(TokenType.INTEGER, 2));
        list.add(new Token(TokenType.CLOSE_PARENTESIS));
        Parser parser = new Parser();
        System.out.println(parser.parse(list));
    }
}
