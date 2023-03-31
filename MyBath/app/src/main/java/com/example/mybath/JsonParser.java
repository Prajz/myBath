package com.example.mybath;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonParser {

    private final JsonReader reader;
    private boolean dataRead = false;

    public static JsonParser fromString(String str) {
        JsonReader reader = new JsonReader(new StringReader(str));
        return new JsonParser(reader);
    }

    public JsonParser(JsonReader reader) {
        this.reader = reader;
    }

    /**
     * Parses the entire JSON object, deducing the type at runtime.
     * @return a HashMap or ArrayList representing the JSON data.
     */
    public Object parse() throws IOException {
        if (dataRead) {
            throw new IOException("Reader has been partially read");
        }
        dataRead = true;
        return parseMember();
    }

    /**
     * Parses the entire JSON object under the assumption that the root
     * takes the form of a JavaScript object.
     * @return A HashMap representing the JSON data.
     */
    public HashMap<String, Object> parseAsHashmap() throws IOException {
        if (dataRead) {
            throw new IOException("Reader has been partially read.");
        }
        dataRead = true;
        return parseObject();
    }

    /**
     * Parses the entire JSON object under the assumption that the root
     * takes the form of a list.
     * @return An ArrayList representing the JSON data.
     */
    public ArrayList<Object> parseAsList() throws IOException {
        if (dataRead) {
            throw new IOException("Reader has been partially read.");
        }
        dataRead = true;
        return parseList();
    }

    private Object parseMember() throws IOException {
        JsonToken next = this.reader.peek();
        switch (next) {
            case BEGIN_ARRAY:
                return parseList();
            case BEGIN_OBJECT:
                return parseObject();
            case NUMBER:
                return parseNumber();
            case STRING:
                return parseString();
            case NULL:
                return parseNull();
            case BOOLEAN:
                return parseBoolean();
            default:
                throw new IOException("Reader is either empty or partially read.");
        }
    }

    private HashMap<String, Object> parseObject() throws IOException {
        reader.beginObject();
        HashMap<String, Object> result = new HashMap<>();
        while (reader.peek() != JsonToken.END_OBJECT) {
            String name = reader.nextName();
            Object field = parseMember();
            result.put(name, field);
        }
        reader.endObject();
        return result;
    }

    private ArrayList<Object> parseList() throws IOException {
        reader.beginArray();
        ArrayList<Object> result = new ArrayList<>();
        while (reader.peek() != JsonToken.END_ARRAY) {
            Object element = parseMember();
            result.add(element);
        }
        reader.endArray();
        return result;
    }

    private Double parseNumber() throws IOException {
        return reader.nextDouble();
    }

    private String parseString() throws IOException {
        return reader.nextString();
    }

    private Boolean parseBoolean() throws IOException {
        return reader.nextBoolean();
    }

    private Object parseNull() throws IOException {
        reader.nextNull();
        return null;
    }
}
