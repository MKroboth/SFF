package eu.cactis.sff.tests;

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

import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import com.mifmif.common.regex.Generex;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class Generators {
    private final String ALPHA = "\u0041-\u005a\u005f\u0061-\u007a\u00c0-\u00d6\u00d8-\u00f6\u00f8-\u00ff\u0100-\u1fff\u3040-\u318f\u3300-\u337f\u3400-\u3d2d\u4e00-\u9fff\uf900-\ufaff";
    private final String NUMERIC = "0-9";
    private final String ALPHANUM = ALPHA+NUMERIC;
    private final String NONPRINTABLE = "\\p{Cc}\\p{Cf}\\p{Zl}\\p{Zp}\ufff0-\uffff\udbd9\udb9d\udbbd\udb80-\udbff\ud8b8\udc00-\udfff\ud800-\udb7f";

    private final Generex identifierRegex = new Generex("[-" + ALPHA + "][-"+ ALPHANUM + "]*");
    private final Generex textContentRegex = new Generex("([^\\\\\\[\\]\n\r<>"+NONPRINTABLE+"] ?)*");
    private final Generex textBlockContentGenerator = new Generex("([^\\\\<>"+NONPRINTABLE+"] ?)*");
    private final Generex propertyRegex = new Generex("([^)\\[\\]"+NONPRINTABLE+"][^)"+NONPRINTABLE+"] ?)+");
    private final Generex amapEntryRegex = new Generex("(-|"+ALPHANUM + ")([^\\[\\],:\n"+NONPRINTABLE+"][^\n\\[\\],:"+NONPRINTABLE+"\\P{Graph}] ?)+");

    public Stream<String> identifierGenerator() {


        return Stream.generate(identifierRegex::random);
    }

    public Stream<String> textContentGenerator() {


        return Stream.generate(textContentRegex::random).map(String::trim);
    }

    public Stream<String> textBlockContentGenerator() {


        return Stream.generate(textBlockContentGenerator::random).map(String::trim);
    }

    public Stream<List<String>> propertyGenerator() {


        return Stream.generate(propertyRegex::random).map(x -> x.split(" ")).map(Arrays::asList);
    }

    public Stream<Map<String, String>> attributeGenerator() {


        return Stream.generate(() -> {
           int amount = (new Random().nextInt(10)+1);
           return Streams.zip(Stream.generate(amapEntryRegex::random),
                    Stream.generate(amapEntryRegex::random), (x,y) -> Collections.singletonMap(x.trim(), y.trim()))
                    .limit(amount)
                    .reduce(Collections.emptyMap(), (stringStringMap, stringStringMap2) -> {
                        Map<String, String> ret = new Hashtable<>();
                        ret.putAll(stringStringMap);
                        ret.putAll(stringStringMap2);
                        return ret;
                    });

        });
    }
}
