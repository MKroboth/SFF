package eu.cactis.sff;

import java.util.*;

public class GroupNode extends Node {
    private String name;
    private final List<String> properties = new LinkedList<String>();
    private final Map<String, String> attributes = new Hashtable<String, String>();
    private List<Node> children = new LinkedList<>();

    public GroupNode() {
    }

    public GroupNode(String name, List<String> properties, Map<String, String> attributes, List<Node> children) {
        this.name = name;
        this.properties.addAll(properties);
        this.attributes.putAll(attributes);
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<String> getProperties() {
        synchronized (this.properties) {
            return new LinkedList<String>(properties);
        }
    }

    public void setProperties(String properties) {
        setProperties(Arrays.asList(properties.split("\\s+")));
    }
    public void setProperties(List<String> properties) {
        synchronized (this.properties) {
            this.properties.clear();
            this.properties.addAll(properties);
        }
    }

    public Map<String, String> getAttributes() {
        synchronized (this.attributes) {
            return new HashMap<String, String>(this.attributes);
        }
    }

    public void setAttributes(Map<String, String> attributes) {
        synchronized (this.attributes) {
            this.attributes.clear();
            this.attributes.putAll(attributes);
        }
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children.clear();
        this.children.addAll(children);
    }

    public void appendChild(Node node) {
        children.add(node);
    }
}
