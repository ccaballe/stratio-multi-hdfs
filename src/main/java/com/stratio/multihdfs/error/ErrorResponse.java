/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.multihdfs.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel
@Data
@ToString
public class ErrorResponse {

    @ApiModelProperty(value = "HTTP status code")
    private int code;

    @ApiModelProperty(value = "HTTP status message")
    private String message;

    @ApiModelProperty(value = "Level of error message")
    private ErrorLevel level;

    @ApiModelProperty(value = "Error description")
    private String description;

    @ApiModelProperty(value = "More info")
    private String moreInfo;

    ErrorResponse(EnumError error) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.level = error.getLevel();
        this.description = error.getDescription();
        this.moreInfo = error.getMoreInfo();
    }
}