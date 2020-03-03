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
import java.util.Optional;

/**
 * Represents a comment in the SFF Document.
 *
 * @author Maximilian Kroboth
 * @version 1.0
 * @since 1.0
 */
public class CommentNode extends AbstractNodeWithContent {
    /** Constant <code>IDENTIFER="comment"</code> */
    public static final String IDENTIFER = "comment";


    /**
     * Default constructor.
     */
    public CommentNode() {
        setContent("");
    }

    /**
     * Creates a comment node from a string.
     *
     * @param content the content of the comment.
     */
    public CommentNode(String content) {
        setContent(content);
    }


    /**
     * {@inheritDoc}
     *
     * Sets the content of the comment to a new value.
     */
    @Override
    public void setContent(String content) {
        super.setContent(Optional.ofNullable(content).map(String::trim).orElse(null));
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentNode that = (CommentNode) o;
        return Objects.equals(getContent(), that.getContent());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(getContent());
    }

    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return IDENTIFER;
    }
}
