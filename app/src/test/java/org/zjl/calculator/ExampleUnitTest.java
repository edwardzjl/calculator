package org.zjl.calculator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testCal() {
        List<String> input = Arrays.asList("1", "*", "2", "+", "2");
        Deque<String> rev = MainActivity.shuntingYard(input);
        double calculate = MainActivity.calculate(rev);
        assertEquals(4.0, calculate, 0.00001);
    }

}