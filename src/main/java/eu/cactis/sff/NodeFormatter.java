package eu.cactis.sff;

public interface NodeFormatter {
    public Class<? extends Node> getNodeType();
    public String formatUnknownNode(Node node, int depth);
}
