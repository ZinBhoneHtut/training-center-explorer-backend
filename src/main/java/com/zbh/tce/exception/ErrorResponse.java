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
    private String details;

    /**
     * Instantiates a new Error response.
     *
     * @param timestamp the timestamp
     * @param status    the status
     * @param message   the message
     * @param details   the details
     */
    public ErrorResponse(Date timestamp, String status, String message, String details) {
        this.timestamp = DateUtils.format(timestamp, "yyyy-MM-dd hh:mm:ss");
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = DateUtils.format(timestamp, "yyyy-MM-dd hh:mm:ss");
    }

}