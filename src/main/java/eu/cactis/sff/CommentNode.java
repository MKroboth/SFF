package eu.cactis.sff;

public class CommentNode extends Node {
    private String content;

    public CommentNode() {
    }

    public CommentNode(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
