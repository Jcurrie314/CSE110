import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.tuber.Landing;
import com.parse.tuber.Profile;
import com.parse.tuber.R;
import com.parse.tuber.Register;
import com.parse.tuber.Search;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
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



    //
//       GIVEN: I am not signed in to any account
//           WHEN: I enter any combination of incorrect email/passwords
//           THEN: I will not be granted access to the app and I will be able to try again to
//                 enter correct info

    @Test
    public void testLoginUser(){
        //onView(withContentDescription("Back")).perform(click());
        onView(withId(R.id.bLogin)).perform(click());
        onView(withId(R.id.etUsername))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.bLoginUser)).perform(click());

    }

    @Test
    public void testRegisterNewUserWithNonUCSDEmail(){

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
//    public void testSearchResultClick(){
//        ParseUser.logOut();
//        onView(withId(R.id.bLogin)).perform(click());
//        onView(withId(R.id.etUsername))
//                .perform(typeText("testuser"), closeSoftKeyboard());
//        onView(withId(R.id.etPassword))
//                .perform(typeText("test"), closeSoftKeyboard());
//        onView(withId(R.id.bLoginUser)).perform(click());
//        onView(withId(R.id.my_recycler_view))
//              .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//
//
//    }
}
