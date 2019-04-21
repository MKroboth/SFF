package eu.cactis.sff;

import java.util.*;

public class PropertyNode extends Node {
    private String name;
    private final List<String> properties = new LinkedList<String>();
    private final Map<String, String> attributes = new Hashtable<String, String>();
    private String content;

    public PropertyNode() {
    }

    public PropertyNode(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public PropertyNode(String name, List<String> properties, Map<String, String> attributes, String content) {
        this.name = name;
        this.properties.addAll(properties);
        this.attributes.putAll(attributes);
        this.content = content;
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

    public void setProperties(List<String> properties) {
        synchronized (this.properties) {
            this.properties.clear();
            this.properties.addAll(properties);
        }
    }


    public void setProperties(String properties) {
        setProperties(Arrays.asList(properties.split("\\s+")));
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
