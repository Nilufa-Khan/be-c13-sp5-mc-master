package com.example.track.controller;


import com.example.track.domain.Artist;
import com.example.track.domain.Track;
import com.example.track.exception.TrackAlreadyFoundException;
import com.example.track.exception.TrackNotFoundException;
import com.example.track.service.TrackServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TrackServiceImpl trackService;

    @InjectMocks
    private TrackController trackController;
    private Track track;
    private Artist artist;

    @BeforeEach
    void setUp() {
        artist = new Artist(41,"Artist41");
        track = new Track(41,"Track41",5,artist);
        mockMvc = MockMvcBuilders.standaloneSetup(trackController).build();
    }

    @AfterEach
    void tearDown() {
        track = null;
    }

    @Test
    public void saveTrack() throws Exception {
        when(trackService.saveTrack(any())).thenReturn(track);
        mockMvc.perform(post("/trackDetail/api/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(track)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).saveTrack(any());

    }

    @Test
    public void saveTrackFailure() throws Exception {
        when(trackService.saveTrack(any())).thenThrow(TrackAlreadyFoundException.class);
        mockMvc.perform(post("/trackDetail/api/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(track)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).saveTrack(any());

    }

    @Test
    public void deleteTrackFailure() throws Exception {
        when(trackService.deleteTrack(anyInt())).thenReturn(true);
        mockMvc.perform(delete("/trackDetail/api/track2/41")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(track)))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).deleteTrack(anyInt());
    }


    private static String jsonToString(final Object ob) throws JsonProcessingException {
        String result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(ob);
            result = jsonContent;
        } catch(JsonProcessingException e) {
            result = "JSON processing error";
        }

        return result;
    }
}
