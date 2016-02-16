/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.iosched.session;

import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.samples.apps.iosched.Config;
import com.google.samples.apps.iosched.R;
import com.google.samples.apps.iosched.injection.ModelProvider;
import com.google.samples.apps.iosched.mockdata.SessionsMockCursor;
import com.google.samples.apps.iosched.mockdata.SpeakersMockCursor;
import com.google.samples.apps.iosched.mockdata.TagMetadataMockCursor;
import com.google.samples.apps.iosched.provider.ScheduleContract;
import com.google.samples.apps.iosched.settings.SettingsUtils;
import com.google.samples.apps.iosched.util.TimeUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Tests for {@link SessionDetailActivity} when showing a session that is not the keynote and that
 * is in user schedule, and that is ready for feedback submission.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SessionDetailActivityTestSessionInScheduleTest {

    public static final String SESSION_ID = "5b7836c8-82bf-e311-b297-00155d5066d7";

    private Uri mSessionUri;

    @Rule
    public ActivityTestRule<SessionDetailActivity> mActivityRule =
            new ActivityTestRule<SessionDetailActivity>(SessionDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    // Make sure the EULA screen is not shown.
                    SettingsUtils.markTosAccepted(InstrumentationRegistry.getTargetContext(), true);

                    // Create a stub model to simulate a session in schedule
                    ModelProvider.setStubSessionDetailModel(new StubSessionDetailModel(
                            InstrumentationRegistry.getTargetContext(),
                            SessionsMockCursor.getCursorForSessionInSchedule(),
                            SpeakersMockCursor.getCursorForSingleSpeaker(),
                            TagMetadataMockCursor.getCursorForSingleTagMetadata()));

                    // Create intent to load the keynote session.
                    mSessionUri = ScheduleContract.Sessions.buildSessionUri(SESSION_ID);
                    Intent intent = new Intent(Intent.ACTION_VIEW, mSessionUri);

                    return intent;
                }
            };

    @Before
    public void setTime() {
        // Set up time to 5 minutes after end of session
        long timeDiff = SessionsMockCursor.END_SESSION - Config.CONFERENCE_START_MILLIS
                + 5 * TimeUtils.MINUTE;
        TimeUtils.setCurrentTimeRelativeToStartOfConference(
                InstrumentationRegistry.getTargetContext(), timeDiff);
    }

    @Test
    public void sessionTitle_ShowsCorrectTitle() {
        onView(withId(R.id.session_title)).check(matches(
                allOf(withText(SessionsMockCursor.FAKE_TITLE), isDisplayed())));
    }

    @Test
    public void speakersSection_IsVisible() {
        onView(withId(R.id.session_detail_frag)).perform(swipeUp());
        onView(withId(R.id.session_speakers_block)).check(matches(isDisplayed()));
    }

    @Test
    public void tagSection_IsVisible() {
        onView(withId(R.id.session_tags_container)).perform(scrollTo()).
                check(matches(isDisplayed()));
    }

    @Test
    public void feedbackCard_IsVisible() {
        onView(withId(R.id.give_feedback_card)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore("Will be written with Intento")
    public void submitFeedback_WhenClicked_IntentFired() {
    }

    @Test
    @Ignore("Will be written with Intento")
    public void showMap_WhenClicked_IntentFired() {
        onView(withId(R.id.menu_map_room)).perform(click());
    }

    @Test
    @Ignore("Will be written with Intento")
    public void showShare_WhenClicked_IntentFired() {
        onView(withId(R.id.menu_share)).perform(click());
    }

    @Test
    @Ignore("Will be written with Intento")
    public void tag_OnClick_IntentFired() {
    }

    @Test
    @Ignore("Will be written with Intento")
    public void youTubeVideo_WhenClicked_IntentFired() {
    }

    @Test
    @Ignore("Will be written with Intento")
    public void feedbackCard_OnClick_IntentFired() {
        onView(withId(R.id.give_feedback_card)).perform(click());
    }

}