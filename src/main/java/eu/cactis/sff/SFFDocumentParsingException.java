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
    /**
     * Constructs a new exception with "Error during document parsing." as its detail message.
     */
    public SFFDocumentParsingException() {
        this("Error during document parsing.");
    }

    /**
     * Constructs a new exception with the specified detail message.
     * @param message a message
     */
    public SFFDocumentParsingException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * @param message a message
     * @param cause a cause
     * @see Exception#Exception(String, Throwable)
     */
    public SFFDocumentParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     * @param cause a cause
     */
    public SFFDocumentParsingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
     * @param message a message
     * @param cause a cause
     * @param enableSuppression enable suppression
     * @param writableStackTrace writable stack trace
     */
    public SFFDocumentParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
