package com.example.track.service;

import com.example.track.domain.Artist;
import com.example.track.domain.Track;
import com.example.track.exception.TrackAlreadyFoundException;
import com.example.track.exception.TrackNotFoundException;
import com.example.track.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackServiceImplTest {

    @Mock
    private TrackRepository trackRepository;
    @InjectMocks
    private TrackServiceImpl trackService;
    private Track track;
    private Artist artist;
    List<Track> trackList;

    @BeforeEach
    void setUp() {
        artist = new Artist(31,"Artist31");
        track = new Track(31,"Track31",5,artist);
        trackList = Arrays.asList(track);
    }

    @Test
    public void saveTrack() throws TrackAlreadyFoundException {
        when(trackRepository.save(any())).thenReturn(track);
        when(trackRepository.findById(track.getTrackId())).thenReturn(Optional.ofNullable(null));
        assertEquals(track,trackService.saveTrack(track));
        verify(trackRepository,times(1)).save(any());
        verify(trackRepository,times(1)).findById(any());
    }

    @Test
    public void saveTrackFailure(){
       when(trackRepository.findById(track.getTrackId())).thenReturn(Optional.ofNullable(track));
       assertThrows(TrackAlreadyFoundException.class,()->trackService.saveTrack(track));
       verify(trackRepository,times(0)).save(any());
       verify(trackRepository,times(1)).findById(any());
    }

    @Test
    public void deleteTrackSuccess() throws TrackNotFoundException {
       when(trackRepository.findById(track.getTrackId())).thenReturn(Optional.ofNullable(track));
       boolean flag = trackService.deleteTrack(track.getTrackId());
       assertEquals(true,flag);

       verify(trackRepository,times(1)).deleteById(any());
       verify(trackRepository,times(1)).findById(any());
    }

    @Test
    public void deleteTrackFailure(){
        when(trackRepository.findById(track.getTrackId())).thenReturn(Optional.ofNullable(track));
        assertThrows(TrackAlreadyFoundException.class,() ->trackService.saveTrack(track));
        verify(trackRepository,times(0)).save(any());
        verify(trackRepository,times(1)).findById(any());
    }

}
