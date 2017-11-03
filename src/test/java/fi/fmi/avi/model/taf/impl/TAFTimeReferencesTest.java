package fi.fmi.avi.model.taf.impl;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.fmi.avi.model.taf.TAF;
import fi.fmi.avi.model.taf.TAFAirTemperatureForecast;
import fi.fmi.avi.model.taf.TAFBaseForecast;
import fi.fmi.avi.model.taf.TAFChangeForecast;

public class TAFTimeReferencesTest {

	@Test
	public void testCompleteValidTimeReferences() {
		TAF msg = new TAFImpl();
		TAFBaseForecast baseForecast = new TAFBaseForecastImpl();
		TAFAirTemperatureForecast tempFct = new TAFAirTemperatureForecastImpl();
		List<TAFAirTemperatureForecast> temperatures = new ArrayList<>();
		temperatures.add(tempFct);
		baseForecast.setTemperatures(temperatures);
		msg.setBaseForecast(baseForecast);
		List<TAFChangeForecast> changeForecasts = new ArrayList<>();
		TAFChangeForecast changeFct1 = new TAFChangeForecastImpl();
		TAFChangeForecast changeFct2 = new TAFChangeForecastImpl();
		TAFChangeForecast changeFct3 = new TAFChangeForecastImpl();
		changeForecasts.add(changeFct1);
		changeForecasts.add(changeFct2);
		changeForecasts.add(changeFct3);
		msg.setChangeForecasts(changeForecasts);

		assertNull(msg.getIssueTime());
		assertNull(msg.getPartialIssueTime());

		msg.setPartialValidityTimePeriod("3112/0106");
		tempFct.setPartialMaxTemperatureTime("3118");
		tempFct.setPartialMinTemperatureTime("0104");
		changeFct1.setPartialValidityTimePeriod("3119/3124");
		changeFct2.setPartialValidityTimePeriod("0100/0106");
		changeFct3.setPartialValidityTimePeriod("0102/0112");

		assertNull(msg.getValidityStartTime());
		assertNull(msg.getValidityEndTime());
		assertNull(msg.getBaseForecast().getTemperatures().get(0).getMaxTemperatureTime());
		assertNull(msg.getBaseForecast().getTemperatures().get(0).getMinTemperatureTime());
		assertNull(msg.getChangeForecasts().get(0).getValidityStartTime());
		assertNull(msg.getChangeForecasts().get(0).getValidityEndTime());
		assertNull(msg.getChangeForecasts().get(1).getValidityStartTime());
		assertNull(msg.getChangeForecasts().get(1).getValidityEndTime());
		assertNull(msg.getChangeForecasts().get(2).getValidityStartTime());
		assertNull(msg.getChangeForecasts().get(2).getValidityEndTime());
		assertFalse(msg.areForecastTimeReferencesComplete());
		
		msg.completeForecastTimeReferences(2017, 12, 31, 0, ZoneId.of("Z"));

		assertNull(msg.getIssueTime());
		assertEquals(ZonedDateTime.of(2017, 12, 31, 12, 0, 0, 0, ZoneId.of("Z")), msg.getValidityStartTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 6, 0, 0, 0, ZoneId.of("Z")), msg.getValidityEndTime());
		assertEquals(ZonedDateTime.of(2017, 12, 31, 18, 0, 0, 0, ZoneId.of("Z")), msg.getBaseForecast().getTemperatures().get(0).getMaxTemperatureTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 4, 0, 0, 0, ZoneId.of("Z")), msg.getBaseForecast().getTemperatures().get(0).getMinTemperatureTime());
		assertEquals(ZonedDateTime.of(2017, 12, 31, 19, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(0).getValidityStartTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(0).getValidityEndTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(1).getValidityStartTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 6, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(1).getValidityEndTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 2, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(2).getValidityStartTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 12, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(2).getValidityEndTime());
		assertTrue(msg.areForecastTimeReferencesComplete());
		
		
	}

	@Test
	public void testCompleteValidTimeReferencesWithoutDayOfMonths() {
		TAF msg = new TAFImpl();
		List<TAFChangeForecast> changeForecasts = new ArrayList<>();
		TAFChangeForecast changeFct1 = new TAFChangeForecastImpl();
		TAFChangeForecast changeFct2 = new TAFChangeForecastImpl();
		TAFChangeForecast changeFct3 = new TAFChangeForecastImpl();
		changeForecasts.add(changeFct1);
		changeForecasts.add(changeFct2);
		changeForecasts.add(changeFct3);
		msg.setChangeForecasts(changeForecasts);

		assertNull(msg.getIssueTime());
		assertNull(msg.getPartialIssueTime());

		msg.setPartialValidityTimePeriod("311206");
		changeFct1.setPartialValidityTimePeriod("1924");
		changeFct2.setPartialValidityTimePeriod("0006");
		changeFct3.setPartialValidityTimePeriod("0212");

		assertNull(msg.getValidityStartTime());
		assertNull(msg.getValidityEndTime());
		assertNull(msg.getChangeForecasts().get(0).getValidityStartTime());
		assertNull(msg.getChangeForecasts().get(0).getValidityEndTime());
		assertNull(msg.getChangeForecasts().get(1).getValidityStartTime());
		assertNull(msg.getChangeForecasts().get(1).getValidityEndTime());
		assertNull(msg.getChangeForecasts().get(2).getValidityStartTime());
		assertNull(msg.getChangeForecasts().get(2).getValidityEndTime());
		assertFalse(msg.areForecastTimeReferencesComplete());

		msg.completeForecastTimeReferences(2017, 12, 31, 0, ZoneId.of("Z"));

		assertNull(msg.getIssueTime());
		assertEquals(ZonedDateTime.of(2017, 12, 31, 12, 0, 0, 0, ZoneId.of("Z")), msg.getValidityStartTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 6, 0, 0, 0, ZoneId.of("Z")), msg.getValidityEndTime());
		assertEquals(ZonedDateTime.of(2017, 12, 31, 19, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(0).getValidityStartTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(0).getValidityEndTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(1).getValidityStartTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 6, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(1).getValidityEndTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 2, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(2).getValidityStartTime());
		assertEquals(ZonedDateTime.of(2018, 1, 1, 12, 0, 0, 0, ZoneId.of("Z")), msg.getChangeForecasts().get(2).getValidityEndTime());
		assertTrue(msg.areForecastTimeReferencesComplete());


		msg.resetForecastTimeReferences();
		assertNull(msg.getValidityStartTime());
		assertNull(msg.getValidityEndTime());
		assertFalse(msg.areForecastTimeReferencesComplete());

		msg.setPartialValidityTimePeriod("091212");
		msg.completeForecastTimeReferences(2017, 12, 9, 0, ZoneId.of("Z"));

		assertEquals(ZonedDateTime.of(2017, 12, 9, 12, 0, 0, 0, ZoneId.of("Z")), msg.getValidityStartTime());
		assertEquals(ZonedDateTime.of(2017, 12, 10, 12, 0, 0, 0, ZoneId.of("Z")), msg.getValidityEndTime());
		assertTrue(msg.areForecastTimeReferencesComplete());

	}



}
