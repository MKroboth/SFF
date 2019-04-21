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
