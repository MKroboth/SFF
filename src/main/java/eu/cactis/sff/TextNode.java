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

import java.util.Objects;

/**
 * Represents a text node.
 *
 * @author Maximilian Kroboth
 * @since 1.0
 * @version 1.0
 */
public class TextNode implements Node {
    /**
     * The content of the text node.
     */
    private String content = "";

    /**
     * Default constructor.
     */
    public TextNode() {

    }

    /**
     * Creates a new text node with the given content.
     * @param content the given content.
     */
    public TextNode(String content) {
        setContent(content);
    }

    /**
     * Gets the content of the node.
     * @return the content of the node.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Sets the content of the node.
     * @param content the new content of the node.
     */
    public void setContent(String content) {
        if(content.contains("<") || content.contains(">")) throw new IllegalArgumentException("content should neither contain '<' nor '>'");
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextNode textNode = (TextNode) o;
        return Objects.equals(getContent(), textNode.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent());
    }
}
