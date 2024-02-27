package com.healthycoderapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class ActivityCalculatorTest {
    @Test
    void should_ReturnBad_When_AVGBelow20() {
        //given
        int weeklyCardio = 40;
        int weeklyWorkouts = 1;

        //when
        String actual = ActivityCalculator.rateActivityLevel(weeklyCardio, weeklyWorkouts);

        //then
        assertEquals("bad", actual);
    }

    @Test
    void should_ReturnAvg_When_20and40() {
        //given
        int weeklyCardio = 40;
        int weeklyWorkouts = 3;

        //when
        String actual = ActivityCalculator.rateActivityLevel(weeklyCardio, weeklyWorkouts);

        //then
        assertEquals("average", actual);
    }

    @Test
    void should_ReturnGood_When_Above40() {
        //given
        int weeklyCardio = 40;
        int weeklyWorkouts = 10;

        //when
        String actual = ActivityCalculator.rateActivityLevel(weeklyCardio, weeklyWorkouts);

        //then
        assertEquals("good", actual);
    }

    @Test
    void should_ThrowException_When_InputBelowZero() {
        //given
        int weeklyCardio = -40;
        int weeklyWorkouts = 1;

        //when
        Executable exec = () -> ActivityCalculator.rateActivityLevel(weeklyCardio, weeklyWorkouts);

        //then
        assertThrows(RuntimeException.class, exec);
    }
}