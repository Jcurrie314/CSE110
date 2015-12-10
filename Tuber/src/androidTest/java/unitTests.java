import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.parse.tuber.MainActivity;
import com.parse.tuber.R;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class unitTests
        extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public unitTests() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        testRegisterNewUserWithNonUCSDEmail();
    }
    @Test
    public void testRegisterNewUserWithNonUCSDEmail(){
        onView(withId(R.id.bRegisterUser)).perform(click());
        onView(withId(R.id.etFirstName))
                .perform(typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.etFirstName)).check(matches(withText("Testy")));
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
        onView(withId(R.id.bRegister)).perform(click());
        onView(withId(R.id.etEmail)).check(matches(hasErrorText("UCSD email is required")));



    }
}
