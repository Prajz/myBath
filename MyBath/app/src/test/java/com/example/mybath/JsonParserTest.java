package com.example.mybath;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class JsonParserTest {

    @Test
    public void canParseEmpty() throws IOException {
        JsonParser parser;

        parser = JsonParser.fromString("[]");
        assertEquals(new ArrayList<>(), parser.parse());

        parser = JsonParser.fromString("{}");
        assertEquals(new HashMap<String, Object>(), parser.parse());
    }

    @Test
    public void canParseList() throws IOException {
        JsonParser parser;

        parser = JsonParser.fromString("[1, 2, 3]");
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), parser.parse());

        parser = JsonParser.fromString("[\"alpha\", \"beta\", null, \"delta\"]");
        assertEquals(Arrays.asList("alpha", "beta", null, "delta"), parser.parse());

        parser = JsonParser.fromString("[null]");
        assertEquals(Arrays.asList(new Object[] { null }), parser.parse());

        parser = JsonParser.fromString("[true, null, 0.0, \"\"]");
        assertEquals(Arrays.asList(true, null, 0.0, ""), parser.parse());
    }

    @Test
    public void canParseObject() throws IOException {
        JsonParser parser;

        parser = JsonParser.fromString("{\"a\": \"a\", \"foo\": -6.28e4, \"bar\": false}");
        HashMap<String, Object> expected = new HashMap<>();
        expected.put("a", "a");
        expected.put("foo", -6.28e4);
        expected.put("bar", false);

        assertEquals(expected, parser.parse());
    }

    @Test
    public void canParseNested() throws IOException {
        JsonParser parser;

        parser = JsonParser.fromString("{"
        + "    \"foo\": ["
        + "        [1, 0, 4],"
        + "        null,"
        + "        [6, \"yes\", 5]"
        + "    ],"
        + "    \"bar\": {"
        + "        \"Fizz\": 3,"
        + "        \"Buzz\": 5,"
        + "        \"derivatives\": {"
        + "            \"FizzBuzz\": 15"
        + "        }"
        + "    }"
        + "}");
        HashMap<String, Object> expected = new HashMap<>();
        expected.put("foo", Arrays.asList(
                Arrays.asList(1.0, 0.0, 4.0),
                null,
                Arrays.asList(6.0, "yes", 5.0)
        ));
        HashMap<String, Object> tmpInner = new HashMap<>();
        tmpInner.put("FizzBuzz", 15.0);
        HashMap<String, Object> tmpOuter = new HashMap<>();
        tmpOuter.put("Fizz", 3.0);
        tmpOuter.put("Buzz", 5.0);
        tmpOuter.put("derivatives", tmpInner);
        expected.put("bar", tmpOuter);

        assertEquals(expected, parser.parse());
    }
}
