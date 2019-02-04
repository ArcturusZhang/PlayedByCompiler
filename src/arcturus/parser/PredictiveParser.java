package arcturus.parser;

import arcturus.lexical.Token;
import arcturus.lexical.TokenType;

import java.util.*;

public class PredictiveParser implements Parser {
    private Map<TokenType, Map<String, List<Element>>> parsingTable;
    private Stack<Element> stack;

    public PredictiveParser() {
        buildParsingTable();
        stack = new Stack<>();
    }

    private void buildParsingTable() {
        parsingTable = new HashMap<>();
        parsingTable.put(TokenType.INTEGER, new HashMap<>());
        parsingTable.get(TokenType.INTEGER)
                .put(Element.E.nonTerminal, List.of(Element.T, Element.X));
        parsingTable.get(TokenType.INTEGER)
                .put(Element.T.nonTerminal, List.of(new Element(TokenType.INTEGER), Element.Y));
        parsingTable.put(TokenType.TIMES_OPERATOR, new HashMap<>());
        parsingTable.get(TokenType.TIMES_OPERATOR)
                .put(Element.Y.nonTerminal, List.of(new Element(TokenType.TIMES_OPERATOR), Element.T));
        parsingTable.put(TokenType.PLUS_OPERATOR, new HashMap<>());
        parsingTable.get(TokenType.PLUS_OPERATOR)
                .put(Element.X.nonTerminal, List.of(new Element(TokenType.PLUS_OPERATOR), Element.E));
        parsingTable.get(TokenType.PLUS_OPERATOR)
                .put(Element.Y.nonTerminal, List.of(Element.epsilon));
        parsingTable.put(TokenType.OPEN_PAREN, new HashMap<>());
        parsingTable.get(TokenType.OPEN_PAREN)
                .put(Element.E.nonTerminal, List.of(Element.T, Element.X));
        parsingTable.get(TokenType.OPEN_PAREN)
                .put(Element.T.nonTerminal, List.of(new Element(TokenType.OPEN_PAREN), Element.E, new Element(TokenType.CLOSE_PAREN)));
        parsingTable.put(TokenType.CLOSE_PAREN, new HashMap<>());
        parsingTable.get(TokenType.CLOSE_PAREN)
                .put(Element.X.nonTerminal, List.of(Element.epsilon));
        parsingTable.get(TokenType.CLOSE_PAREN)
                .put(Element.Y.nonTerminal, List.of(Element.epsilon));
        parsingTable.put(TokenType.EOF, new HashMap<>());
        parsingTable.get(TokenType.EOF)
                .put(Element.X.nonTerminal, List.of(Element.epsilon));
        parsingTable.get(TokenType.EOF)
                .put(Element.Y.nonTerminal, List.of(Element.epsilon));
    }

    @Override
    public boolean parse(List<Token> tokenList) {
        List<Token> tokens = new ArrayList<>(tokenList);
        if (!tokens.get(tokens.size() - 1).isType(TokenType.EOF))
            tokens.add(new Token(TokenType.EOF));
        stack.push(Element.EOF);
        stack.push(Element.E);
        int current = 0;
        while (!stack.isEmpty()) {
            Element top = stack.pop();
            if (top == Element.epsilon) continue;
            if (top.isTerminal()) {
                Token next = tokens.get(current++);
                if (!next.isType(top.getTerminal())) return false;
            } else if (top.isNonTerminal()) {
                Token next = tokens.get(current);
                Map<String, List<Element>> map = parsingTable.get(next.getType());
                if (!map.containsKey(top.getNonTerminal())) return false;
                List<Element> elements = map.get(top.getNonTerminal());
                for (int i = elements.size() - 1; i >= 0; i--) {
                    stack.push(elements.get(i));
                }
            } else {
                throw new IllegalStateException();
            }
        }
        System.out.println(current);
        return current >= tokens.size();
    }

    private static class Element {
        static Element epsilon = new Element();
        static Element EOF = new Element(TokenType.EOF);
        static Element E = new Element("E");
        static Element X = new Element("X");
        static Element T = new Element("T");
        static Element Y = new Element("Y");
        String nonTerminal;
        TokenType type;

        private Element() {
            this.nonTerminal = null;
            this.type = null;
        }

        Element(String nonTerminal) {
            this.nonTerminal = nonTerminal;
            this.type = null;
        }

        Element(TokenType type) {
            this.nonTerminal = null;
            this.type = type;
        }

        boolean isTerminal() {
            return nonTerminal == null && type != null;
        }

        boolean isNonTerminal() {
            return nonTerminal != null && type == null;
        }

        String getNonTerminal() {
            if (nonTerminal == null) throw new IllegalStateException("This is not a non-terminal");
            return nonTerminal;
        }

        TokenType getTerminal() {
            if (type == null) throw new IllegalStateException("This is not terminal");
            return type;
        }

        @Override
        public String toString() {
            if (isTerminal()) return getTerminal().toString();
            if (isNonTerminal()) return getNonTerminal();
            return "EPSILON";
        }
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
        Parser parser = new PredictiveParser();
        System.out.println(parser.parse(list));
    }
}
