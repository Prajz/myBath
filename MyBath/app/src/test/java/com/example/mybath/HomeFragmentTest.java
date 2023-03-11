package com.example.mybath;

import static org.junit.Assert.*;

import org.junit.Test;

public class HomeFragmentTest extends HomeFragment {

    @Test
    public void temptoct() throws Exception{
        String inpu = "274.32";
        String outp = temptoc(inpu);
        String expected = "1°C";

        assertEquals(expected, outp);
    }
}