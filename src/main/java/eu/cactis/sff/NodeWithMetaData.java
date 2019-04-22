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

public abstract class NodeWithMetaData {
      /**
     * The properties of the group.
     */
    private final List<String> properties = new LinkedList<String>();

    /**
     * The attributes of the group.
     */
    private final Map<String, String> attributes = new Hashtable<String, String>();

    /**
     * Gets the nodes properties.
     * @return the nodes properties.
     */
    public List<String> getProperties() {
        synchronized (this.properties) {
            return new LinkedList<String>(properties);
        }
    }

    /**
     * Sets the nodes properties from a whitespace delimited string.
     * @param properties The property string.
     */
    public void setPropertiesFromString(String properties) {
        setProperties(Arrays.asList(properties.split("\\s+")));
    }

    /**
     * Sets the nodes properties.
     * @param properties the source of the nodes new properties.
     */
    public void setProperties(Collection<String> properties) {
        synchronized (this.properties) {
            this.properties.clear();
            this.properties.addAll(properties);
        }
    }

    /**
     * Gets the nodes attributes.
     * @return the nodes attributes.
     */
    public Map<String, String> getAttributes() {
        synchronized (this.attributes) {
            return new HashMap<String, String>(this.attributes);
        }
    }

    /**
     * Sets the nodes attributes
     * @param attributes the nodes attributes
     */
    public void setAttributes(Map<String, String> attributes) {
        synchronized (this.attributes) {
            this.attributes.clear();
            this.attributes.putAll(attributes);
        }
    }
}
