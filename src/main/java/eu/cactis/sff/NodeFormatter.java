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

/**
 * Provides methods for formatting a SFF-DOM node its textual representation.
 *
 * @author mkr
 * @version $Id: $Id
 */
public interface NodeFormatter {
    /**
     * <p>getNodeType.</p>
     *
     * @return a {@link java.lang.Class} object.
     */
    Class<? extends Node> getNodeType();
    /**
     * <p>formatUnknownNode.</p>
     *
     * @param node a {@link eu.cactis.sff.Node} object.
     * @param depth a int.
     * @return a {@link java.lang.String} object.
     */
    String formatUnknownNode(Node node, int depth);
}
