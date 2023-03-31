package com.example.mybath;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.espresso.action.ViewActions;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.api.IMapView;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapsFragmentInstruTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void typeInvalidLocations()
    {
        onView(ViewMatchers.withId(R.id.maps)).perform(ViewActions.click());
        onView(withId(R.id.outpTime)).check(matches(withText(" ")));
        onView(ViewMatchers.withId(R.id.autoCompleteTextTwo)).perform(ViewActions.typeText("Limers"));
        onView(ViewMatchers.withId(R.id.autoCompleteTextOne)).perform(ViewActions.typeText("Libreary"));
        closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.simpleButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.outpTime)).check(matches(isCompletelyDisplayed()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.outpTime)).check(matches(withText("Enter a valid Location/Destination.")));
    }

    @Test
    public void typeCorrectLocations()
    {
        onView(ViewMatchers.withId(R.id.maps)).perform(ViewActions.click());
        onView(withId(R.id.outpTime)).check(matches(withText(" ")));
        onView(ViewMatchers.withId(R.id.autoCompleteTextTwo)).perform(ViewActions.typeText("Lime Tree"));
        onView(ViewMatchers.withId(R.id.autoCompleteTextOne)).perform(ViewActions.typeText("Library"));
        closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.simpleButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.outpTime)).check(matches(isCompletelyDisplayed()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.outpTime)).check(matches(withText("Estimated Time - 3 m : 20 s")));
    }

    @Test
    public void usingCurrentLocation()
    {
        //before u run this go to extended controls on the emulator and change your location to 51.3798, -2.3266 (if u cant search for it on the maps just go near the back entrance of the su on the map and it should be very close to that)
        onView(ViewMatchers.withId(R.id.maps)).perform(ViewActions.click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.outpTime)).check(matches(withText(" ")));
        onView(ViewMatchers.withId(R.id.autoCompleteTextTwo)).perform(ViewActions.typeText("Lime Tree"));
        closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.clocstart)).perform(ViewActions.click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.outpTime)).check(matches(withText("Estimated Time - 2 m : 12 s")));

    }
}
