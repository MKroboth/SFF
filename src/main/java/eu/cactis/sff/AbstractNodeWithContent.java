package eu.cactis.sff;


import java.util.Optional;

public abstract class AbstractNodeWithContent implements Node, NodeWithContent {
    /**
     * The content of the node.
     */
    private String content;


    /**
     * Returns the content of the node.
     *
     * @return the content of the node.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Sets the content of the node to a new value.
     *
     * @param content the new value of the content.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
