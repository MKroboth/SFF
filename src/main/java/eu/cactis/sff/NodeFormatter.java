package eu.cactis.sff;

public interface NodeFormatter {
    Class<? extends Node> getNodeType();
    String formatUnknownNode(Node node, int depth);
}
