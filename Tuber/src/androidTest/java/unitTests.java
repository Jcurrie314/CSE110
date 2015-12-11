import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
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
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
        ParseUser.logOut();
    }


//       GIVEN: I am not signed in to any account (above), and I am on the login page
//       WHEN: I enter a combination of correct username/passwords
//       THEN: I will be granted access to the app and I will be taken to the search page


    @Test
    public void testLoginUser() {
//       GIVEN: I am on the login page (from landing)
        onView(withId(R.id.bLogin)).perform(click());
//       WHEN: I enter a combination of correct username/passwords
        onView(withId(R.id.etUsername))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.bLoginUser)).perform(click());

    }


    //       GIVEN: I am on the register screen, and not logged in (above @ before statement)
//       WHEN: I fill in all fields in the register screen, but don't use a @ucsd.edu email
//       THEN: The display will return an error at that field with text: "UCSD email is required"
    @Test
    public void testRegisterNewUserWithNonUCSDEmail() {

        onView(withId(R.id.bRegister)).perform(click());
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
        onView(withId(R.id.etEmail)).check(matches(hasErrorText("UCSD email is required")));
    }

//    @Test
//    public void testSearchResultClick() {
//        ParseUser.logOut();
//        onView(withId(R.id.bLogin)).perform(click());
//        onView(withId(R.id.etUsername))
//                .perform(typeText("testuser"), closeSoftKeyboard());
//        onView(withId(R.id.etPassword))
//                .perform(typeText("test"), closeSoftKeyboard());
//        onView(withId(R.id.bLoginUser)).perform(click());
//        onView(withId(R.id.my_recycler_view))
//                .check(matches(isDisplayed()))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//
//
//    }

}
