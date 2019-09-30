package eu.cactis.sff.test_utility;

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

import org.jetbrains.annotations.NotNull;

public class Pair<Left, Right> {
    private final Left left;
    private final Right right;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    private Pair(@NotNull Left left, @NotNull Right right) {
        this.left = left;
        this.right = right;
    }

    public @NotNull Left getLeft() {
        return left;
    }

    public @NotNull Right getRight() {
        return right;
    }
}
