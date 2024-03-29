package com.gymfox.bcwc5.statics.passportclass;

import com.gymfox.bcwc5.date.*;
import java.util.Arrays;

public class Passport {
    private static char[] newSeries = {'A','A'};
    private static int newSerialNumber = 1;
    private final static int MAX_SERIAL = 999999;
    private final static int FIRST_LETTER = 0;
    private final static int SECOND_LETTER = 1;

    private char[] series;
    private int serialNumber;
    private String name;
    private String surname;
    private Date birthDate;

    public Passport(String name, String surname, int bDay, int bMonth, int bYear)
            throws InvalidDateException, InvalidSerialException {
        this.name = name;
        this.surname = surname;
        this.birthDate = new Date(bDay, bMonth, bYear);

        series = Arrays.copyOf(newSeries, newSeries.length);

        serialNumber = newSerialNumber;

        seriesGeneration();
    }

    private void seriesGeneration() throws InvalidSerialException {
        if ( newSerialNumber < MAX_SERIAL ) {
            newSerialNumber += 1;
            return;
        }
        newSerialNumber = 1;

        if ( newSeries[SECOND_LETTER] < 'Z' ) {
            newSeries[SECOND_LETTER] += 1;
            return;
        }

        if ( newSeries[FIRST_LETTER] < 'Z') {
            newSeries[SECOND_LETTER] = 'A';
            newSeries[FIRST_LETTER] += 1;
            return;
        }

        throw new InvalidSerialException();
    }

    public static void setSeries(String series) throws InvalidSerialException {
        char[] buffer = series.toCharArray();

        validateSeries(buffer);

        newSeries = Arrays.copyOf(buffer, newSeries.length);

        newSerialNumber = 1;
    }

    public static void setSeries(int serialNumber) throws InvalidSerialNumberException {

        validateSerialNumber(serialNumber);

        newSerialNumber = serialNumber;
    }

    public static void setSeries(String series, int serialNumber) throws InvalidSerialException,
            InvalidSerialNumberException {
        char[] buffer = series.toCharArray();

        validate(buffer, serialNumber);

        newSeries = Arrays.copyOf(buffer, newSeries.length);

        newSerialNumber = serialNumber;
    }

    public String toString() {
        StringBuffer out = new StringBuffer();

        out.append("Passport: " + getSeries() + getSerialNumber() + "\n")
                .append("Name: " + getName() + "\n")
                .append("Surname: " + getSurname() +"\n")
                .append("Birthday: " + getbDate());

        return out.toString();
    }

    public String getSeries() {
        String buffer = String.valueOf(this.series);

        return buffer;
    }

    public String getSerialNumber() {
        String formatted = String.format("%06d", serialNumber);

        return formatted;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getbDate() {
        return birthDate;
    }

    private static void validateSeries(char[] series) throws InvalidSerialException {
        for ( int i = 0; i < newSeries.length; i++ ) {
            series[i] = Character.toUpperCase(series[i]);
        }

        if ( series.length > newSeries.length ) {
            throw new InvalidSerialException();
        }

        for ( int i = 0; i < newSeries.length; i++ ) {
            if ( !Character.isUpperCase(series[i]) ) {
                throw new InvalidSerialException();
            }
        }

        for ( int i = 0; i < newSeries.length; i++ ) {
            if ( newSeries[i] > series[i] ){
                throw new InvalidSerialException();
            }
        }
    }

    private static void validateSerialNumber(int serialNumber)
            throws InvalidSerialNumberException {
        if ( newSerialNumber >= serialNumber ) {
            throw new InvalidSerialNumberException();
        }
        if ( serialNumber < 1 || serialNumber > MAX_SERIAL ) {
            throw new InvalidSerialNumberException();
        }
    }

    private static void validate(char[] series, int serialNumber)
            throws InvalidSerialException, InvalidSerialNumberException {

        validateSeries(series);
        validateSerialNumber(serialNumber);
    }

    static void resetSeriesAndNumber() {
        newSeries[FIRST_LETTER] = 'A';
        newSeries[SECOND_LETTER] = 'A';
        newSerialNumber = 1;
    }
}