package fi.fmi.avi.model.taf.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.fmi.avi.model.PartialOrCompleteTimePeriod;
import fi.fmi.avi.model.impl.PartialOrCompleteTimePeriodImpl;
import fi.fmi.avi.model.taf.TAFChangeForecast;

/**
 * Created by rinne on 30/01/15.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TAFChangeForecastImpl extends TAFForecastImpl implements TAFChangeForecast {

    private static final long serialVersionUID = 4204829487109076764L;
    private static final Pattern VALIDITY_PERIOD_PATTERN = Pattern.compile("^(([0-9]{2})([0-9]{2}))|(([0-9]{2})([0-9]{2})/([0-9]{2})([0-9]{2}))$");
    private static final Pattern VALIDITY_START_PATTERN = Pattern.compile("^(FM)?([0-9]{2})?([0-9]{2})([0-9]{2})$");

    private final ChangeForecastValidityTime validityTime;
    private TAFChangeIndicator changeIndicator;

    public TAFChangeForecastImpl() {
        this.validityTime = new ChangeForecastValidityTime();
    }

    public TAFChangeForecastImpl(final TAFChangeForecast input) {
        super(input);
        this.validityTime = new ChangeForecastValidityTime();
        if (input != null) {
            this.setChangeIndicator(input.getChangeIndicator());
            if (input.getValidityStartTime() != null && input.getValidityEndTime() != null) {
                this.setValidityStartTime(input.getValidityStartTime());
                this.setValidityEndTime(input.getValidityEndTime());
            } else if (input.getPartialValidityTimePeriod() != null) {
                this.setPartialValidityTimePeriod(input.getPartialValidityTimePeriod());
            } else {
                this.setPartialValidityStartTime(input.getPartialValidityStartTime());
            }
        }
    }

    @Override
    public TAFChangeIndicator getChangeIndicator() {
        return changeIndicator;
    }

    @Override
    public void setChangeIndicator(final TAFChangeIndicator changeIndicator) {
        this.changeIndicator = changeIndicator;
    }

    @Override
    @JsonIgnore
    public int getValidityStartDayOfMonth() {
        return this.validityTime.getStartTimeDay();
    }

    @Override
    @JsonIgnore
    public int getValidityStartHour() {
        return this.validityTime.getStartTimeHour();
    }

    @Override
    @JsonIgnore
    public int getValidityStartMinute() {
        return this.validityTime.getStartTimeMinute();
    }

    @Override
    @JsonIgnore
    public int getValidityEndDayOfMonth() {
        return this.validityTime.getEndTimeDay();
    }

    @Override
    @JsonIgnore
    public int getValidityEndHour() {
        return this.validityTime.getEndTimeHour();
    }

    @Override
    @JsonProperty("partialValidityStartTime")
    public String getPartialValidityStartTime() {
        if (this.validityTime.getEndTimeHour() == -1 && this.validityTime.getStartTimeHour() > -1 && this.validityTime.getStartTimeMinute() > -1) {
            final StringBuilder sb = new StringBuilder();
            if (this.validityTime.getStartTimeDay() > -1) {
                sb.append(String.format("%02d", this.validityTime.getStartTimeDay()));
            }
            sb.append(String.format("%02d%02d", this.validityTime.getStartTimeHour(), this.validityTime.getStartTimeMinute()));

            return sb.toString();
        } else {
            return null;
        }
    }

    @Override
    @JsonProperty("partialValidityStartTime")
    public void setPartialValidityStartTime(final String time) {
        if (time == null) {
            this.setPartialValidityStartTime(-1, -1, -1);
        } else {
            final Matcher m = VALIDITY_START_PATTERN.matcher(time);
            if (m.matches()) {
                int day = -1;
                if (m.group(2) != null) {
                    day = Integer.parseInt(m.group(2));
                }
                final int hour = Integer.parseInt(m.group(3));
                final int minute = Integer.parseInt(m.group(4));
                this.setPartialValidityStartTime(day, hour, minute);
            } else {
                throw new IllegalArgumentException("Time '" + time + "' is not in format '(FM)HHmm' or '(FM)ddHHmm'");
            }
        }
    }

    @Override
    public void setPartialValidityStartTime(final int hour, final int minute) {
        this.setPartialValidityStartTime(-1, hour, minute);
    }

    @Override
    public void setPartialValidityStartTime(final int day, final int hour, final int minute) {
        this.validityTime.setPartialStartTime(day, hour, minute);
    }

    @Override
    public void setPartialValidityEndTime(final int day, final int hour) {
        this.validityTime.setPartialEndTime(day, hour, 0);
    }

    @Override
    public String getPartialValidityTimePeriod() {
        if (this.validityTime.getStartTimeHour() > -1 && this.validityTime.getEndTimeHour() > -1) {
            final StringBuilder sb = new StringBuilder();
            if (this.validityTime.getStartTimeDay() > -1 && this.validityTime.getEndTimeDay() > -1) {
                sb.append(String.format("%02d%02d", this.validityTime.getStartTimeDay(), this.validityTime.getStartTimeHour()));
                sb.append('/');
                sb.append(String.format("%02d%02d", this.validityTime.getEndTimeDay(), this.validityTime.getEndTimeHour()));
            } else {
                sb.append(String.format("%02d%02d", this.validityTime.getStartTimeHour(), this.validityTime.getEndTimeHour()));
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    @Override
    @JsonProperty("partialValidityTimePeriod")
    public void setPartialValidityTimePeriod(final String time) {
        if (time == null) {
            this.setPartialValidityTimePeriod(-1, -1, -1, -1);
        } else {
            final Matcher m = VALIDITY_PERIOD_PATTERN.matcher(time);
            if (m.matches()) {
                if (m.group(1) != null) {
                    //old 24h TAF: HHHH
                    final int fromHour = Integer.parseInt(m.group(2));
                    final int toHour = Integer.parseInt(m.group(3));
                    this.setPartialValidityTimePeriod(fromHour, toHour);

                } else {
                    //30h TAF: ddHH/ddHH
                    final int fromDay = Integer.parseInt(m.group(5));
                    final int fromHour = Integer.parseInt(m.group(6));
                    final int toDay = Integer.parseInt(m.group(7));
                    final int toHour = Integer.parseInt(m.group(8));
                    this.setPartialValidityTimePeriod(fromDay, toDay, fromHour, toHour);
                }
            } else {
                throw new IllegalArgumentException("Time period is not either 'ddHHHH' or 'ddHH/ddHH'");
            }

        }
    }

    @Override
    public void setPartialValidityTimePeriod(final int startHour, final int endHour) {
        this.setPartialValidityTimePeriod(-1, -1, startHour, endHour);
    }

    @Override
    public void setPartialValidityTimePeriod(final int startDay, final int endDay, final int startHour, final int endHour) {
        if (ChangeForecastValidityTime.timeOk(startDay, startHour, 0) && ChangeForecastValidityTime.timeOk(endDay, endHour, -1)) {
            this.validityTime.setPartialStartTime(startDay, startHour, 0);
            this.validityTime.setPartialEndTime(endDay, endHour, 0);
        } else {
            throw new IllegalArgumentException("Start '" + startDay + "/" + startHour + "' and/or end time '" + endDay + "/" + endHour + "' is not allowed");
        }
    }

    @JsonProperty("validityStartTime")
    public String getValidityStartTimeISO() {
        if (this.validityTime.getCompleteStartTime() != null) {
            return this.validityTime.getCompleteStartTimeAsISOString();
        } else {
            return null;
        }
    }

    @JsonProperty("validityStartTime")
    public void setValidityStartTimeISO(final String time) {
        this.validityTime.setCompleteStartTimeAsISOString(time);
    }

    @Override
    @JsonIgnore
    public ZonedDateTime getValidityStartTime() {
        return this.validityTime.getCompleteStartTime();
    }

    @Override
    public void setValidityStartTime(final ZonedDateTime time) {
        this.validityTime.setCompleteStartTime(time);
    }

    @Override
    public void setValidityStartTime(final int year, final int monthOfYear, final int dayOfMonth, final int hour, final int minute, final ZoneId timeZone) {
        this.setValidityStartTime(ZonedDateTime.of(LocalDateTime.of(year, monthOfYear, dayOfMonth, hour, minute), timeZone));
    }

    @JsonProperty("validityEndTime")
    public String getValidityEndTimeISO() {
        return this.validityTime.getCompleteEndTimeAsISOString();
    }

    @JsonProperty("validityEndTime")
    public void setValidityEndTimeISO(final String time) {
        this.setValidityEndTime(ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(time)));
    }

    @Override
    @JsonIgnore
    public ZonedDateTime getValidityEndTime() {
        return this.validityTime.getCompleteEndTime();
    }

    @Override
    public void setValidityEndTime(final ZonedDateTime time) {
        this.validityTime.setCompleteEndTime(time);
    }

    @Override
    public void setValidityEndTime(final int year, final int monthOfYear, final int dayOfMonth, final int hour, final int minute, final ZoneId timeZone) {
        this.setValidityEndTime(ZonedDateTime.of(LocalDateTime.of(year, monthOfYear, dayOfMonth, hour, minute), timeZone));
    }

    PartialOrCompleteTimePeriod getValidityTimeInternal() {
        return this.validityTime;
    }

    private static class ChangeForecastValidityTime extends PartialOrCompleteTimePeriodImpl {

        private static final long serialVersionUID = 9121690608709851093L;

        @Override
        public String getPartialStartTime() {
            throw new UnsupportedOperationException("getPartialStartTime(...) not implemented");
        }

        @Override
        public String getPartialEndTime() {
            throw new UnsupportedOperationException("getPartialEndTime(...) not implemented");
        }

        @Override
        protected boolean matchesPartialTimePattern(final String partialString) {
            throw new UnsupportedOperationException("matchesPartialTimePattern(...) not implemented");
        }

        @Override
        protected Pattern getPartialTimePattern() {
            throw new UnsupportedOperationException("getPartialTimePattern(...) not implemented");
        }

        @Override
        protected int extractDayFromPartial(final String partialString) {
            throw new UnsupportedOperationException("extractDayFromPartial(...) not implemented");
        }

        @Override
        protected int extractHourFromPartial(final String partialString) {
            throw new UnsupportedOperationException("extractHourFromPartial(...) not implemented");
        }

        @Override
        protected int extractMinuteFromPartial(final String partialString) {
            throw new UnsupportedOperationException("extractMinuteFromPartial(...) not implemented");
        }

        @Override
        public boolean hasStartTime() {
            return this.getStartTimeHour() > -1;
        }

        @Override
        public boolean hasEndTime() {
            return this.getEndTimeHour() > -1;
        }
    }

}