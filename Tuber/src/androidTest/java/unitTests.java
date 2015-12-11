import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.TreeIterables;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.tuber.Landing;
import com.parse.tuber.Profile;
import com.parse.tuber.R;
import com.parse.tuber.Register;
import com.parse.tuber.Search;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Predicates.not;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

public class unitTests
        extends ActivityInstrumentationTestCase2<Landing> {

    private Landing mActivity;

    public unitTests() {
        super(Landing.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ParseUser.logOut();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


//       GIVEN: I am not signed in to any account (above), and I am on the login page
//       WHEN: I enter a combination of correct username/passwords
//       THEN: I will be granted access to the app and I will be taken to the search page


    @Test
    public void testLoginUser() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       GIVEN: I am on the login page (from landing)
        onView(withId(R.id.bLogin)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       WHEN: I enter a combination of correct username/passwords
        onView(withId(R.id.etUsername))
                .check(matches(isDisplayed()))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.bLoginUser)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ParseUser.logOut();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginUserIncorrect() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.bLogin))
                .check(matches(isDisplayed()))
                .perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etUsername))
                .check(matches(isDisplayed()))
                .perform(typeText("incorrecttest"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("incorrecttest"), closeSoftKeyboard());
        onView(withId(R.id.bLoginUser)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Incorrect user details")).check(matches(isDisplayed()));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ParseUser.logOut();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    //       GIVEN: I am on the register screen, and not logged in (above @ before statement)
//       WHEN: I fill in all fields in the register screen, but don't use a @ucsd.edu email
//       THEN: The display will return an error at that field with text: "UCSD email is required"


    @Test
    public void testRegisterNewUserWithNoEmail() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.bRegister)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etFirstName))
                .perform(typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.etUsername))
                .perform(typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.etEmail))
                .perform(typeText(" "), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("testy"), closeSoftKeyboard());
        onView(withId(R.id.etPhone))
                .perform(typeText("8057203900"), closeSoftKeyboard());
        onView(withId(R.id.cbTutor)).perform(click());
        onView(withId(R.id.etPrice))
                .perform(typeText("20.00"), closeSoftKeyboard());
        onView(withId(R.id.bRegisterPage)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etEmail)).check(matches(hasErrorText("Email is required!")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ParseUser.logOut();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterNewUserWithNoPassword() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.bRegister)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etFirstName))
                .perform(typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.etUsername))
                .perform(typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.etEmail))
                .perform(typeText("testy@ucsd.edu"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText(" "), closeSoftKeyboard());
        onView(withId(R.id.etPhone))
                .perform(typeText("8057203900"), closeSoftKeyboard());
        onView(withId(R.id.cbTutor)).perform(click());
        onView(withId(R.id.etPrice))
                .perform(typeText("20.00"), closeSoftKeyboard());
        onView(withId(R.id.bRegisterPage)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etPassword)).check(matches(hasErrorText("Password is required!")));
        ParseUser.logOut();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testRegisterNewUserWithNoUsername() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.bRegister)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etFirstName))
                .perform(typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.etUsername))
                .perform(typeText(" "), closeSoftKeyboard());
        onView(withId(R.id.etEmail))
                .perform(typeText("testy@ucsd.edu"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("testy"), closeSoftKeyboard());
        onView(withId(R.id.etPhone))
                .perform(typeText("8057203900"), closeSoftKeyboard());
        onView(withId(R.id.cbTutor)).perform(click());
        onView(withId(R.id.etPrice))
                .perform(typeText("20.00"), closeSoftKeyboard());
        onView(withId(R.id.bRegisterPage)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etUsername)).check(matches(hasErrorText("Username is required!")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterNewUserWithNonUCSDEmail() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.bRegister)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etFirstName))
                .perform(typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.etUsername))
                .perform(typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.etEmail))
                .perform(typeText("Testy@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("testy"), closeSoftKeyboard());
        onView(withId(R.id.etPhone))
                .perform(typeText("8057203900"), closeSoftKeyboard());
        onView(withId(R.id.cbTutor)).perform(click());
        onView(withId(R.id.etPrice))
                .perform(typeText("20.00"), closeSoftKeyboard());
        onView(withId(R.id.bRegisterPage)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etEmail)).check(matches(hasErrorText("UCSD email is required")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}