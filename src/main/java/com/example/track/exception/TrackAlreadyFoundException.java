package com.example.track.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "Track is already Present")
public class TrackAlreadyFoundException extends Exception {
}
