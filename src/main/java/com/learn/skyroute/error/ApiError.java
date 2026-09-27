package com.learn.skyroute.error;

public record ApiError(int status, String error, String message) {
}
