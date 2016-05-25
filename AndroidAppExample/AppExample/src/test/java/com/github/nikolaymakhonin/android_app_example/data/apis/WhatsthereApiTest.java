package com.github.nikolaymakhonin.android_app_example.data.apis;

import com.github.nikolaymakhonin.android_app_example.TestBase;
import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.WhatsThereApi;
import com.github.nikolaymakhonin.android_app_example.di.components.ServiceComponent;
import com.github.nikolaymakhonin.android_app_example.di.factories.ComponentsFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

//docs: https://developer.android.com/training/testing/unit-testing/local-unit-tests.html
@RunWith(MockitoJUnitRunner.class)
public class WhatsThereApiTest extends TestBase {

    //region Init

    private ServiceComponent _serviceComponent;

    @Before
    @Override
    public void setup() {
        super.setup();
        _serviceComponent = ComponentsFactory.buildServiceComponent(_mockContext);
    }

    //endregion

    @Test
    public void loadInstagramByGeo() throws Exception {
        WhatsThereApi api = _serviceComponent.getWhatsThereApi();
        api.getInstagramPostsByGeo(22.277872, 114.1762067, 1000)
            .first()
            .subscribe(responseInstagram -> {
                assertThat("Response code != 200", responseInstagram.meta.code, is(200));
                assertThat("Posts count <= 10", responseInstagram.data.length, is(new GreaterThan(10)));
                _lock.countDown();
            });

        assertTrue("Request timeout", _lock.await(10, TimeUnit.SECONDS));
    }

}