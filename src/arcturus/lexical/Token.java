package arcturus.lexical;

public class Token {
    public static final int WHITESPACE = 0;
    public static final int PARENTESIS_LEFT = 1; // (
    public static final int PARENTESIS_RIGHT = 2; // )
    public static final int CURL_BRACE_LEFT = 3; // {
    public static final int CURL_BRACE_RIGHT = 4; // }
    public static final int SEMICOLON = 5;
    public static final int COMMA = 6;
    public static final int INTEGER = 7;
    public static final int FLOAT = 8;
    public static final int QUOTATION = 9;
    public static final int SINGLE_QUOTATION = 10;
    public static final int OPERATOR = 11;
    public static final int KEYWORD = 100;
    public static final int IDENTIFIER = 200;
    private int type;
    private String content;

    public Token(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
