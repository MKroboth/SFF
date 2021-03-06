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
 * Represents a processing instruction.
 *
 * @author Maximilian Kroboth
 * @version 1.0
 * @since 1.0
 */
public class ProcessingInstructionNode extends AbstractNodeWithContent implements Node, NamedNode {
    /**
     * Constant <code>IDENTIFIER="processing-instruction"</code>
     */
    public static final String IDENTIFIER = "processing-instruction";

    /**
     * The processing instructions name
     */
    private String name = null;


    /**
     * Creates a new processing instruction
     *
     * @param name    the name of the processing instruction
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
     *
     * @return the processing instructions name.
     * @throws java.lang.IllegalStateException if any.
     */
    public String getName() throws IllegalStateException {
        if (name == null) throw new IllegalStateException("name was not initialized before.");
        return name;
    }

    /**
     * Sets the processing instructions name.
     *
     * @param name the processing instructions new name.
     * @throws java.lang.IllegalArgumentException if any.
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name.isEmpty()) throw new IllegalArgumentException("name should not be empty");
        this.name = name;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessingInstructionNode that = (ProcessingInstructionNode) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getContent(), that.getContent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
