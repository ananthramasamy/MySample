package com.anandharajr.mysample;

import android.graphics.Bitmap;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void urlToBitMap() throws Exception {
        Bitmap bitmap = Helper.getBitmapFromURL("https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg");
        assertNotEquals(null, bitmap);
    }
}