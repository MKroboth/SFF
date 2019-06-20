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

/**
 * Represents a group of nodes.
 *
 * @author Maximilian Kroboth
 * @version 1.0
 * @since 1.0
 */
public class GroupNode extends NodeWithMetaData implements Node, NamedNode {
    public static final String IDENTIFIER = "group";
    /**
     * The name of the group.
     */
    private String name = null;

    /**
     * The nodes in the group.
     */
    private final List<Node> children = new LinkedList<>();

    /**
     * Default constructor.
     */
    public GroupNode() {
    }

    public GroupNode(String name, Collection<Node> children) {
        this(name, Collections.emptyList(), Collections.emptyMap(), children);
    }
    /**
     * Creates a new GroupNode.
     * @param name The groups name.
     * @param properties The groups properties.
     * @param attributes The groups attributes.
     * @param children The groups children.
     */
    public GroupNode(String name, List<String> properties, Map<String, String> attributes, Collection<Node> children) {
        setName(name);
        setProperties(properties);
        setAttributes(attributes);
        setChildren(children);
    }

    /**
     * Gets the groups name.
     * @return the groups name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the groups name.
     * @param name the groups name.
     * @throws IllegalArgumentException if name is empty.
     */
    public void setName(String name) throws IllegalArgumentException {
        if(name.isEmpty()) throw new IllegalArgumentException("name should not be empty");
        this.name = name.trim();
    }

    /**
     * Gets the nodes children
     * @return the nodes children
     */
    public List<Node> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    /**
     * Sets the nodes children.
     * @param children the nodes children.
     */
    public void setChildren(Collection<Node> children) {
        this.children.clear();
        this.children.addAll(children);
    }

    /**
     * Appends a new child to the node.
     * @param node the new child to append.
     */
    public void appendChild(Node node) {
        children.add(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupNode groupNode = (GroupNode) o;
        return Objects.equals(getName(), groupNode.getName()) &&
                getChildren().equals(groupNode.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getChildren());
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    @Deprecated
    public String getContent() {
        return getTextContent();
    }

    @Override
    @Deprecated
    public void setContent(String content) {
        children.removeIf(node -> node instanceof TextNode);
        children.add(new TextNode(content));
    }

    public String getTextContent() {
        return children.stream()
                .filter(node -> node instanceof TextNode)
                .map(node -> ((TextNode) node).getContent())
                .reduce(String::concat)
                .orElse("");
    }
}
