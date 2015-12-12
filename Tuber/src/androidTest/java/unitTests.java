import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.parse.ParseUser;
import com.parse.tuber.Landing;
import com.parse.tuber.R;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
                .perform(typeText("audreylunde"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("corgis"), closeSoftKeyboard());
        onView(withId(R.id.bLoginUser)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       THEN: I will be granted access to the app and I will be taken to the search page
        onView(withId(R.id.etSearchIn))
                .check(matches(isDisplayed()));
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

//       GIVEN: I am on the login screen, and not logged in (above @ before statement)
//       WHEN: I type in an incorrect username and password and click login
//       THEN: There will be a pop up that says "incorrect user details"
    @Test
    public void testLoginUserIncorrect() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       GIVEN: I am on the login screen, and not logged in (above @ before statement)

        onView(withId(R.id.bLogin))
                .check(matches(isDisplayed()))
                .perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       WHEN: I type in an incorrect username and password and click login
        onView(withId(R.id.etUsername))
                .check(matches(isDisplayed()))
                .perform(typeText("incorrecttest"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("incorrecttest"), closeSoftKeyboard());
        onView(withId(R.id.bLoginUser)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       THEN: There will be a pop up that says "incorrect user details"
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
//       WHEN: I fill in all fields in the register screen, but don't add an email
//       THEN: The display will return an error at that field with text: "Email is required"


    @Test
    public void testRegisterNewUserWithNoEmail() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       GIVEN: I am on the register screen
        onView(withId(R.id.bRegister)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       WHEN: I fill in all fields in the register screen, but don't add an email
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
//       THEN: The display will return an error at that field with text: "Email is required"
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

//       GIVEN: I am on the register screen, and not logged in (above @ before statement)
//       WHEN: I fill in all fields in the register screen, except a password
//       THEN: The display will return an error at that field with text: "Password is required!"


    @Test
    public void testRegisterNewUserWithNoPassword() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       GIVEN: I am on the register screen
        onView(withId(R.id.bRegister)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       WHEN: I fill in all fields in the register screen, except a password
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
//       THEN: The display will return an error at that field with text: "Password is required!"
        onView(withId(R.id.etPassword)).check(matches(hasErrorText("Password is required!")));
        ParseUser.logOut();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//       GIVEN: I am on the register screen, and not logged in (above @ before statement)
//       WHEN: I fill in all fields in the register screen, except the username
//       THEN: The display will return an error at that field with text: "Username is required!"

    @Test
    public void testRegisterNewUserWithNoUsername() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       GIVEN: I am on the register screen
        onView(withId(R.id.bRegister)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       WHEN: I fill in all fields in the register screen, except the username
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
//       THEN: The display will return an error at that field with text: "Username is required!"
        onView(withId(R.id.etUsername)).check(matches(hasErrorText("Username is required!")));
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
    public void testRegisterNewUserWithNonUCSDEmail() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       GIVEN: I am on the register screen
        onView(withId(R.id.bRegister)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       WHEN: I fill in all fields in the register screen, but don't use a @ucsd.edu email
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
//       THEN: The display will return an error at that field with text: "UCSD email is required"
        onView(withId(R.id.etEmail)).check(matches(hasErrorText("UCSD email is required")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
}