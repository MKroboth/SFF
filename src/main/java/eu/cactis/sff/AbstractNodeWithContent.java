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


import java.util.Optional;

public abstract class AbstractNodeWithContent implements Node, NodeWithContent {
    /**
     * The content of the node.
     */
    private String content;


    /**
     * Returns the content of the node.
     *
     * @return the content of the node.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Sets the content of the node to a new value.
     *
     * @param content the new value of the content.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
