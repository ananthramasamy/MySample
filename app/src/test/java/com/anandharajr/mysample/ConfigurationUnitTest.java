package com.anandharajr.mysample;

import android.app.ProgressDialog;
import android.content.Context;

import com.anandharajr.mysample.utils.Configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotEquals;

/**
 * Simple unit tests for {@link Configuration} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationUnitTest {

    @Mock
    private Context mMockContext;

    @Test
    public void progressDialogConstructorNotNull() {
        ProgressDialog progressDialog = Configuration.generateProgressDialog(mMockContext, false);
        assertNotEquals(progressDialog, null);
    }


    @Test
    public void getCarsListOnSuccessEmptyList() throws Exception {

    }


}