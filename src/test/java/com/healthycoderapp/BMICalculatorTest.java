package com.healthycoderapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest {

    private String environment = "dev";

    @BeforeAll // must be STATIC
    static void beforeAll() {
        System.out.println("Before all unit tests.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all unit tests");
    }

    @Nested
    class IsDietRecommendedTests {
        @ParameterizedTest(name = "weight={0}, height-{1}")
//    @CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110., 1.78"})
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void should_ReturnTrue_When_DietRecommended(double coderWeight, double coderHeight) {
            //given
            double weight = coderWeight;
            double height = coderHeight;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight,height);
            //then
            assertTrue(recommended);
//        assertTrue(BMICalculator.isDietRecommended(81.2, 1.65));
        }

        @Test
        void should_ReturnFalse_When_DietNOTRecommended() {
            //given
            double weight = 50.0;
            double height = 1.92;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight,height);
            //then
            assertFalse(recommended);
        }

        @Test
        void should_ThrowArithmeticException_When_HeightZero() {
            //given
            double weight = 50.0;
            double height = 0.0;
            //when
            Executable exec = () -> BMICalculator.isDietRecommended(weight, height);
            //then
            assertThrows(ArithmeticException.class, exec);
        }
    }

    @Nested
    class FindCoderWithWorstBMITests {
        @Test
        @DisplayName(">>>> sample method")
//        @Disabled
//        @DisabledOnOs(OS.MAC)
        void should_ReturnCoderWithWorstBMI_When_CoderListNOTEMPTY() {
            //given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0)); // WORST BMI
            coders.add(new Coder(1.82, 64.7));

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertAll(
                    () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                    () -> assertEquals(98.0, coderWorstBMI.getWeight())
            );
        }

        @Test
        void should_ReturnCoderWithWorstBMI_In10MS_When_CoderListHas10000Elements() {
            //given
            assumeTrue(BMICalculatorTest.this.environment.equals("prod"));
            List<Coder> coders = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }
            //when
            Executable exec = () -> BMICalculator.findCoderWithWorstBMI(coders);
            //then
            assertTimeout(Duration.ofMillis(10), exec);
        }

        @Test
        void should_ReturnNULLWorstBMICoder_WhenListIsEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertNull(coderWorstBMI);
        }
    }

    @Nested
    class GetBMIScoresTest {
        @Test
        void should_ReturnCorrectBMIScoreArray_When_CoderListNOTEMPTY() {
            //given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0)); // WORST BMI
            coders.add(new Coder(1.82, 64.7));
            double[] expected = {18.52, 29.59, 19.53};
            //when
            double[] bmiScores = BMICalculator.getBMIScores(coders);
            //then
            assertArrayEquals(expected, bmiScores);
        }
    }

}