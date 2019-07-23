/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.architecture.multihdfs.error;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;

public enum EnumError {
    REQUEST_METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED.value(), "request_method_not_supported", ErrorLevel.INFO, "Request method not supported.", ""),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "bad_request", ErrorLevel.INFO, "Request is malformed or there are missing mandatory parameters.", ""),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND.value(), "not_found", ErrorLevel.INFO, "Request not found.", ""),
    ALREADY_EXISTS_ERROR(HttpStatus.CONFLICT.value(), "registry_already_exists", ErrorLevel.WARNING, "Registry already exists.", ""),
    PRECONDITION_ERROR(HttpStatus.PRECONDITION_FAILED.value(), "precondition_failed", ErrorLevel.INFO, "Precondition failed.", ""),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal_server_error", ErrorLevel.ERROR, "Internal server error. Server logs may have additional information.", "");

    private final int code;
    private final String message;
    private final ErrorLevel level;
    private final String description;
    private final String moreInfo;

    EnumError(int code, String message, ErrorLevel level, String description, String moreInfo) {
        this.code = code;
        this.message = message;
        this.level = level;
        this.description = description;
        this.moreInfo = moreInfo;
    }

    /**
     * Returns the integer code of the error.
     *
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the message of the error.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the level of the error.
     *
     * @return level
     */
    public ErrorLevel getLevel() {
        return level;
    }

    /**
     * Returns the error description
     *
     * @return description
     */
    public String getDescription() {return description;}

    /**
     * Returns more info
     *
     * @return moreInfo
     */
    public String getMoreInfo() {return moreInfo;}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("message", message)
                .append("level", level)
                .append("description", description)
                .append("moreInfo", moreInfo)
                .toString();
    }
}
