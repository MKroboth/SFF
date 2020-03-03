package eu.cactis.sff;

/*-
 * #%L
 * Cactis SFF
 * %%
 * Copyright (C) 2019 Maximilian Kroboth
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import java.util.*;
import java.util.function.Function;

/**
 * <p>CSFFFormatter class.</p>
 *
 *
 * @author mkr
 * @version $Id: $Id
 */
public class CSFFFormatter {
    /** Constant <code>DEFAULT_INDENT_SPACES=4</code> */
    public static final int DEFAULT_INDENT_SPACES = 4;
    private int indentSpacesAmount;
    private boolean useTabs;


    /**
     * <p>Getter for the field <code>indentSpacesAmount</code>.</p>
     *
     * @return a int.
     */
    public int getIndentSpacesAmount() {
        return indentSpacesAmount;
    }

    /**
     * <p>Setter for the field <code>indentSpacesAmount</code>.</p>
     *
     * @param indentSpacesAmount a int.
     */
    public void setIndentSpacesAmount(int indentSpacesAmount) {
        this.indentSpacesAmount = indentSpacesAmount;
    }

    /**
     * <p>isUseTabs.</p>
     *
     * @return a boolean.
     */
    public boolean isUseTabs() {
        return useTabs;
    }

    /**
     * <p>Setter for the field <code>useTabs</code>.</p>
     *
     * @param useTabs a boolean.
     */
    public void setUseTabs(boolean useTabs) {
        this.useTabs = useTabs;
    }


    /**
     * <p>Constructor for CSFFFormatter.</p>
     */
    public CSFFFormatter() {
        this(DEFAULT_INDENT_SPACES, false);
    }

    /**
     * <p>Constructor for CSFFFormatter.</p>
     *
     * @param indentSpacesAmount a int.
     * @param useTabs a boolean.
     */
    public CSFFFormatter(int indentSpacesAmount, boolean useTabs) {
        setIndentSpacesAmount(indentSpacesAmount);
        setUseTabs(useTabs);
    }

    /**
     * <p>formatNode.</p>
     *
     * @param node a {@link eu.cactis.sff.Node} object.
     * @return a {@link java.lang.String} object.
     */
    public String formatNode(Node node) {
        return formatNode(node, 0);
    }

    /**
     * <p>generateIndent.</p>
     *
     * @param depth a int.
     * @return a {@link java.lang.String} object.
     */
    protected String generateIndent(int depth) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < depth; ++i) {
            if (isUseTabs()) builder.append('\t');
            else {
                for (int j = 0; j < getIndentSpacesAmount(); ++j) {
                    builder.append(' ');
                }
            }
        }

        return builder.toString();
    }
    private final Map<Class<? extends Node>, Function<Node, Function<Integer, String>>> formatters =
            Collections.unmodifiableMap(new Hashtable<>() {{
                // Create an anonymous class extending Hashtable and put all formatters into it.
                // Also do some manual currying for functional stuff.

                put(GroupNode.class, n -> depth -> formatGroupNode((GroupNode) n, depth));
                put(CommentNode.class, n -> depth -> formatCommentNode((CommentNode) n, depth));
                put(ProcessingInstructionNode.class, n -> depth -> formatProcessingInstruction((ProcessingInstructionNode) n, depth));
                put(PropertyNode.class, n -> depth -> formatPropertyNode((PropertyNode) n, depth));
                put(TextNode.class, n -> depth -> formatTextNode((TextNode) n, depth));
            }});

    /**
     * <p>formatNode.</p>
     *
     * @param node a {@link eu.cactis.sff.Node} object.
     * @param depth a int.
     * @return a {@link java.lang.String} object.
     */
    public String formatNode(Node node, int depth) {
        return formatters.getOrDefault(node.getClass(), n -> d -> formatUnknownNode(n, d)).apply(node).apply(depth);
    }

    private String formatTextNode(TextNode node, int depth) {
        return generateIndent(depth) +
                "<" +
                node.getContent() +
                ">\n";
    }

    private String formatPropertyNode(PropertyNode node, int depth) {
        String escapedNodeContent = escapeContent(node.getContent());

        return generateIndent(depth) +
                node.getName() +
                formatNodeProperties(node.getProperties()) +
                formatNodeAttributes(node.getAttributes()) +
                " = " +
                escapedNodeContent +
                '\n';
    }

    private String formatProcessingInstruction(ProcessingInstructionNode node, int depth) {
        return generateIndent(depth) + "@" + node.getName() + " " + node.getContent() + "\n";
    }

    private String formatCommentNode(CommentNode node, int depth) {
        return generateIndent(depth) + "# " + node.getContent() + "\n";
    }


    private String formatGroupNode(GroupNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(generateIndent(depth)).append(escapeContent(node.getName()));
        sb.append(formatNodeProperties(node.getProperties()));
        Map<String, String> map = new Hashtable<>(node.getAttributes());

        if(node.getUUID().isPresent()) {
            map.put("-uuid", node.getUUID().get().toString());
        }

        sb.append(formatNodeAttributes(map));

        if (node.getChildren().size() == 1 && node.getChildren().get(0) instanceof TextNode) {
            sb.append(" ").append(formatNode(node.getChildren().get(0), depth + 1).trim()).append("\n");
        } else {
            sb.append(" {\n");


            for (Node child : node.getChildren()) {
                sb.append(formatNode(child, depth + 1)).append('\n');
            }
            sb.append(generateIndent(depth));
            sb.append("}\n");
        }
        return sb.toString();
    }

    private String formatUnknownNode(Node node, int depth) {
        ServiceLoader<NodeFormatter> services = java.util.ServiceLoader.load(NodeFormatter.class);
        for(NodeFormatter formatter : services) {
            if(formatter.getNodeType().equals(node.getClass())) {
                return formatter.formatUnknownNode(node, depth);
            }
        }
        throw new IllegalStateException("Unknown node type.");
    }

    private String escapeContent(String content, Character... escapedChars) {
        Map<String, String> replacements = new Hashtable<>();
        replacements.put("\n", "\\n");
        replacements.put("\r", "\\r");

        List<Character> defaultEscapedChars = Arrays.asList(
                '<', '>', ',', '"', '(', ')', '{', '}', '[', ']'
        );

        for(Character chr : defaultEscapedChars) {
            replacements.put(chr.toString(), "\\"+chr.toString());
        }
        for(Character chr : escapedChars) {
            replacements.put(chr.toString(), "\\"+chr.toString());
        }
        content = content.replace("\\","\\\\");
        for(Map.Entry<String, String> repl : replacements.entrySet()) {
            content = content.replace(repl.getKey(), repl.getValue());
        }
        return content;
    }

    private String formatNodeAttributes(Map<String, String> attributes) {
        if (attributes.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            sb.append(escapeContent(entry.getKey()));
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append(", ");
        }
        sb.reverse().delete(0, 2).reverse();
        sb.append(']');
        return sb.toString();
    }

    private String formatNodeProperties(List<String> properties) {
        if (properties.isEmpty()) return "";
        if (properties.stream().allMatch(x -> x.trim().isEmpty())) return "";

        StringBuilder sb = new StringBuilder();

        sb.append('(');
        for (String element : properties) {
            sb.append(escapeContent(element, ' ', ')')).append(" ");
        }
        sb.reverse().deleteCharAt(0).reverse();
        sb.append(")");

        return sb.toString();
    }
}
