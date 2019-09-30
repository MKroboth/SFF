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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class NodeWithMetaData extends AbstractNodeWithContent {
      /**
     * The properties of the group.
     */
    private final List<String> properties = new LinkedList<>();

    /**
     * The attributes of the group.
     */
    private final Map<String, String> attributes = new Hashtable<>();

    private final ReentrantReadWriteLock propertiesRWL = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock attributesRWL = new ReentrantReadWriteLock();
    private final Lock propertiesRLock = propertiesRWL.readLock();
    private final Lock propertiesWLock = propertiesRWL.writeLock();
    private final Lock attributesRLock = attributesRWL.readLock();
    private final Lock attributesWLock = attributesRWL.writeLock();

    /**
     * Gets the nodes properties.
     * @return the nodes properties.
     */
    public List<String> getProperties() {
        propertiesRLock.lock();
        try {
            return Collections.unmodifiableList(properties);
        } finally {
            propertiesRLock.unlock();
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
        propertiesWLock.lock();
        try {
            this.properties.clear();
            this.properties.addAll(properties);
        } finally {
            propertiesWLock.unlock();
        }
    }

    /**
     * Gets the nodes attributes.
     * @return the nodes attributes.
     */
    public Map<String, String> getAttributes() {
        attributesRLock.lock();
        try {
            return Collections.unmodifiableMap(this.attributes);
        } finally {
            attributesRLock.unlock();
        }
    }

    /**
     * Sets the nodes attributes
     * @param attributes the nodes attributes
     */
    public void setAttributes(Map<String, String> attributes) {
        attributesWLock.lock();
        try {
            this.attributes.clear();
            for (Map.Entry<String, String> attr : attributes.entrySet()) {
                this.attributes.put(attr.getKey().trim(), attr.getValue().trim());
            }
        } finally {
            attributesWLock.unlock();
        }
    }
}
