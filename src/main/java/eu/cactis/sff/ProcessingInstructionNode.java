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
 * Represents a processing instruction.
 *
 * @author Maximilian Kroboth
 * @version 1.0
 * @since 1.0
 */
public class ProcessingInstructionNode implements Node {
    /**
     * The processing instructions name
     */
    private String name;

    /**
     * The processing instructions content
     */
    private String content;

    /**
     * Creates a new processing instruction
     * @param name the name of the processing instruction
     * @param content the content of the processing instruction
     */
    public ProcessingInstructionNode(String name, String content) {
        setName(name);
        setContent(content);
    }

    /**
     * Default constructor.
     */
    public ProcessingInstructionNode() {
    }

    /**
     * Gets the processing instructions name.
     * @return the processing instructions name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the processing instructions name.
     * @param name the processing instructions new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the processing instructions content.
     * @return the processing instructions content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the processing instructions content.
     * @param content the processing instructions content.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
