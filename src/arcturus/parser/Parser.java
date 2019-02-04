package arcturus.parser;

import arcturus.lexical.Token;

import java.util.List;

public interface Parser {
    boolean parse(List<Token> tokenList);
}
