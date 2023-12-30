package com.example.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackDTO {
    private Long id;
    private String title;
    private String artistsName;
    private String coverFilename;
    private String genre;
    private String year;
    private String lyric;
}
