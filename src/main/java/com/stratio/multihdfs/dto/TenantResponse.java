package com.stratio.multihdfs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TenantResponse {

    private String tid;

    private String name;


    private List<String> uids;

    private List<String> gids;
}
