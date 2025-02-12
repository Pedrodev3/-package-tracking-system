package br.com.pedrovictor.sistlog.dto;

import lombok.Data;

import java.util.List;

@Data
public class HolidayDTO {
    private String date;
    private String localName;
    private String name;
    private String countryCode;
    private boolean fixed;
    private boolean global;
    private String[] counties;
    private String launchYear;
    private List<String> types;
}
