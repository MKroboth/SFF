package eu.cactis.sff;

public class ProcessingInstructionNode extends Node {
    private String name;
    private String content;

    public ProcessingInstructionNode(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public ProcessingInstructionNode() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
