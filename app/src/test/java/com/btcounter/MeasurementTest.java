package com.btcounter;

import com.btcounter.bikelogic.Measurement;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by daba on 2016-07-05.
 */
public class MeasurementTest {

    @Test
    public void speed_calculation() throws Exception {
        assertEquals(2, Measurement.speed(2000, 1000), 0);
    }

    @Test
    public void speed_to_kmH() throws Exception {
        assertEquals(7.2f, Measurement.speedToKmH(2), 0);
    }

    @Test
    public void cadence_calculation() throws Exception {
        assertEquals(120, Measurement.cadence(500));
    }

    @Test
    public void distance_to_km() throws Exception {
        assertEquals(1, Measurement.distanceToKilometers(1000000), 0);
    }

    @Test
    public void float_modulo() throws Exception {
        assertEquals(5, Measurement.floatModulo(2.523f), 0);
    }
}