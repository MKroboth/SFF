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
 * Represents a key-value property node.
 *
 * @author Maximilian Kroboth
 * @since 1.0
 * @version 1.0
 */
public class PropertyNode extends NodeWithMetaData implements Node, NamedNode {
    /** Constant <code>IDENTIFIER="property"</code> */
    public static final String IDENTIFIER = "property";
    /**
     * The property nodes name.
     */
    private String name = null;


    /**
     * Default constructor.
     */
    public PropertyNode() {
    }

    /**
     * Creates a new property node.
     *
     * @param name the properties name
     * @param content the properties content
     */
    public PropertyNode(String name, String content) {
        setName(name);
        setContent(content);
    }

    /**
     * Creates a new property node with all meta information.
     *
     * @param name the properties name
     * @param properties the properties of the property
     * @param attributes the attributes of the property
     * @param content the content of the property
     */
    public PropertyNode(String name, List<String> properties, Map<String, String> attributes, String content) {
        setName(name);
        setProperties(properties);
        setAttributes(attributes);
        setContent(content);
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyNode that = (PropertyNode) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getContent(), that.getContent());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContent());
    }

    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
