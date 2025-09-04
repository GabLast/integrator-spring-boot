package com.integrator.application.utils;

import com.integrator.application.models.module.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class UtilitiesTest {

    @Test
    void testCapitilizeWords() {
        String string = "this is a nice sentence to capitalize.";

        Assertions.assertEquals("This Is A Nice Sentence To Capitalize.", Utilities.capitalizeEachWord(string));
    }

    @Test
    void testGetIdField() throws Exception {
        Field field = TestData.class.getSuperclass().getDeclaredField("id");
        Assertions.assertEquals(field, Utilities.getIdField(TestData.class));
    }

    @Test
    void testGetIdFieldName() throws Exception {
        Field field = TestData.class.getSuperclass().getDeclaredField("id");
        Assertions.assertEquals(field.getName(), Utilities.getIdName(TestData.class));
    }

    @Test
    void testSetFieldValue() throws Exception {
        TestData test = TestData.builder().build();
        Utilities.setFieldValue(test, "word", "word");
        Assertions.assertEquals("word", test.getWord());
    }
}
