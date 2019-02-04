package arcturus.lexical;

public class Token {
    private TokenType type;
    private String content;

    public Token(TokenType type) {
        this.type = type;
        this.content = null;
    }

    public Token(TokenType type, String content) {
        this.type = type;
        this.content = content;
    }

    public Token(TokenType type, Object content) {
        this.type = type;
        this.content = content.toString();
    }

    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public boolean isType(TokenType type) {
        return this.type == type;
    }

    @Override
    public String toString() {
        if (content == null) return type.toString();
        return type.toString() + ": " + content;
    }
}
