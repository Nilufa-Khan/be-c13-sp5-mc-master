package com.example.track.repository;

import com.example.track.domain.Artist;
import com.example.track.domain.Track;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class TrackRepositoryTest {

    @Autowired
    private TrackRepository trackRepository;
    private Artist artist;
    private Track track;

    @BeforeEach
    void setUp() {
        artist = new Artist(12,"Arijit");
        track = new Track(12,"ranjha",4,artist);
    }

    @AfterEach
    void tearDown() {
        artist = null;
        track = null;
       //trackRepository.deleteAll();
    }

    @Test
    public void addTrack() {
        trackRepository.save(track);
        Track track1 = trackRepository.findById(track.getTrackId()).get();
        assertNotNull(track1);
        assertEquals(track.getTrackId(),track1.getTrackId());
    }

    @Test
    public void findAllTracks(){
        List<Track> trackList = trackRepository.findAll();
        assertEquals(1,trackList.size());
        assertEquals("ranjha",trackList.get(0).getTrackName());
    }

    @Test
    public void deleteTrack(){
        trackRepository.delete(track);
        assertEquals(Optional.empty(),trackRepository.findById(track.getTrackId()));
    }


    @Test
    public void getTrackByArtistName(){

        List<Track> artistTrackList = trackRepository.findAllTrackByArtistName(track.getTrackArtist().getArtistName());
        assertEquals(1,artistTrackList.size());
        assertEquals("Arijit",artistTrackList.get(0).getTrackArtist().getArtistName());
    }
}
