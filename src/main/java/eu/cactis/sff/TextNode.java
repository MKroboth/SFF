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
public class TextNode extends AbstractNodeWithContent {
    /** Constant <code>IDENTIFIER="text"</code> */
    public static final String IDENTIFIER = "text";


    /**
     * Default constructor.
     */
    public TextNode() {

    }

    /**
     * Creates a new text node with the given content.
     *
     * @param content the given content.
     */
    public TextNode(String content) {
        setContent(content);
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextNode textNode = (TextNode) o;
        return Objects.equals(getContent(), textNode.getContent());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(getContent());
    }

    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
