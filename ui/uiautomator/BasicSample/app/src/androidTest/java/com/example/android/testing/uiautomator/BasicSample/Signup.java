
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Basic sample for unbundled UiAutomator.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class Signup {

    private static final String ZOOM_PACKAGE
            = "us.zoom.videomeetings";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "UiAutomator";

    private static UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(ZOOM_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(ZOOM_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }


    @Test
    public void signup() throws InterruptedException {
        // Click signup
        mDevice.findObject(By.res(ZOOM_PACKAGE, "btnSignup"))
                .click();
        //Set Email id
        mDevice.wait(Until.hasObject(By.text("Email")), 3000);
        UiObject2 input = mDevice.findObject(By.res(ZOOM_PACKAGE, "edtEmail"));
        input.setText("myUserName2@gmail.com");
        //Set First Name
        mDevice.wait(Until.hasObject(By.text("First Name")), 3000);
        UiObject2 firstName_input = mDevice.findObject(By.res(ZOOM_PACKAGE, "edtFirstName"));
        firstName_input.setText("fUserName");
        //Set Last name
        mDevice.wait(Until.hasObject(By.text("Last Name")), 3000);
        UiObject2 lastName_input = mDevice.findObject(By.res(ZOOM_PACKAGE, "edtLastName"));
        lastName_input.setText("lUserName");
        //Check terms and conditionsus.zoom.videomeetings:id/txtSuccessMsg
        mDevice.findObject(By.res(ZOOM_PACKAGE, "chkAcceptTerms"))
                .click();
        mDevice.findObject(By.res(ZOOM_PACKAGE, "btnSignup"))
                .click();
        assertTrue("Copy and search widget did not appear", successWaitUntilAppear());
    }

    /**
     * Waits until clipboard bar appears
     *
     * @return True if appeared, False otherwise
     */
    public static Boolean successWaitUntilAppear() {
        return mDevice.wait(Until
                        .hasObject(By.res(ZOOM_PACKAGE, "panelSuccess")),
                LAUNCH_TIMEOUT);
    }
}
