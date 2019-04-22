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
 * Represents an exception during document parsing.
 *
 * @author Maximilian Kroboth
 * @version 1.0
 * @since 1.0
 */
public class SFFDocumentParsingException extends Exception {
    public SFFDocumentParsingException() {
        super("Error during document parsing.");
    }

    public SFFDocumentParsingException(String s) {
        super(s);
    }

    public SFFDocumentParsingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SFFDocumentParsingException(Throwable throwable) {
        super(throwable);
    }

    public SFFDocumentParsingException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
