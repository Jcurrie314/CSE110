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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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

        //testRegisterNewUserWithNonUCSDEmail();
    }

//
//       GIVEN: I am not signed in to any account (above), and I am on the login page
//       WHEN: I enter a combination of correct username/passwords
//       THEN: I will be granted access to the app and I will be taken to the search page

    @Test
    public void testLoginUser(){
//       GIVEN: I am on the login page (from landing)
        onView(withId(R.id.bLogin)).perform(click());
//       WHEN: I enter a combination of correct username/passwords
        onView(withId(R.id.etUsername))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.bLoginUser)).perform(click());
        //onView(withId(R.id.my_recycler_view))
        //      .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
//       GIVEN: I am on the register screen, and not logged in (above @ before statement)
//       WHEN: I fill in all fields in the register screen, but don't use a @ucsd.edu email
//       THEN: The display will return an error at that field with text: "UCSD email is required"
//    @Test
//    public void testRegisterNewUserWithNonUCSDEmail(){
//        //ParseUser.logOut();
//
//        onView(withId(R.id.bRegister)).perform(click());
//        onView(withId(R.id.etFirstName))
//                .perform(typeText("Testy"), closeSoftKeyboard());
//        onView(withId(R.id.etUsername))
//                .perform(typeText("Testy"), closeSoftKeyboard());
//        onView(withId(R.id.etEmail))
//                .perform(typeText("Testy@gmail.com"), closeSoftKeyboard());
//        onView(withId(R.id.etPassword))
//                .perform(typeText("testy"), closeSoftKeyboard());
//        onView(withId(R.id.etPhone))
//                .perform(typeText("8057203900"), closeSoftKeyboard());
//        onView(withId(R.id.cbTutor)).perform(click());
//        onView(withId(R.id.etPrice))
//                .perform(typeText("20.00"), closeSoftKeyboard());
//        onView(withId(R.id.bRegisterPage)).perform(click());
//        onView(withId(R.id.etEmail)).check(matches(hasErrorText("UCSD email is required")));
//    }
}
