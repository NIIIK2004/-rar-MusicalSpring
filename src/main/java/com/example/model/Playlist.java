package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "playlist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Playlist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String cover;
    private String description;
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private List<PlaylistContent> contents;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
}
