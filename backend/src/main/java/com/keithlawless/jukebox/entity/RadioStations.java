package com.keithlawless.jukebox.entity;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class RadioStations {
    @ArraySchema(
        arraySchema = @Schema(description = "Array of radio stations"),
        schema = @Schema(implementation = RadioStation.class)
    )
    private RadioStation[] stations;

    public RadioStations() {}

    public RadioStation[] getStations() {
        return stations;
    }

    public void setStations(RadioStation[] stations) {
        this.stations = stations;
    }
}
