package com.github.nikolaymakhonin.android_app_example;

import android.content.Context;
import android.net.Uri;

import com.github.nikolaymakhonin.android_app_example.di.components.ServiceComponent;
import com.github.nikolaymakhonin.android_app_example.di.factories.ComponentsFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

//docs: https://developer.android.com/training/testing/unit-testing/local-unit-tests.html
@RunWith(MockitoJUnitRunner.class)
public abstract class TestBase {

    //region Init

    private static final String FAKE_APP_NAME = "AppName";

    /**
     * Countdown latch
     */
    protected CountDownLatch _lock = new CountDownLatch(1);

    @Mock
    public Context _mockContext;

    protected void initMockContext() {
        // Given a mocked Context injected into the object under test...
        when(_mockContext.getString(R.string.app_name)).thenReturn(FAKE_APP_NAME);
    }

    @Before
    public void setup() {
        initMockContext();
    }

    //endregion

    //region Base tests

    @Test
    public void testMockContext() {
        // ...when the string is returned from the object under test...
        String result = _mockContext.getString(R.string.app_name);

        // ...then the result should be the expected one.
        assertThat(result, is(FAKE_APP_NAME));
    }

    //endregion

}