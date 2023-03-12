package com.example.mybath;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapsFragmentTest extends MapsFragment{

    @Test
    public void estimtimet() throws Exception {
        String inp = "61.1";
        String expected = "Estimated Time - 1 m : 1 s";
        String actual = estimtime(inp);

        assertEquals(expected, actual);
    }

    @Test
    public void timest()  throws Exception {
        String in1 = "23.2";
        String in2 = "10.2";
        String expected = "Estimated Time - 33 seconds";

        String actual = times(in1, in2);

        assertEquals(expected, actual);
    }
}