package com.zbh.tce.exception;

import com.zbh.tce.common.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorResponse {

    private String timestamp;
    private String status;
    private String message;
    private String description;
    private String path;

    /**
     * Instantiates a new Error response.
     *
     * @param timestamp the timestamp
     * @param status    the status
     * @param message   the message
     * @param description   the details
     */
    public ErrorResponse(Date timestamp, String status, String message, String description, String path) {
        this.timestamp = DateUtils.format(timestamp, "yyyy-MM-dd hh:mm:ss");
        this.status = status;
        this.message = message;
        this.description = description;
        this.path = path;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = DateUtils.format(timestamp, "yyyy-MM-dd hh:mm:ss");
    }

}