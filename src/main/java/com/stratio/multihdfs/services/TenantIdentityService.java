/**
 * © 2019 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 * <p>
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal
 * en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or
 * sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written
 * authorization from Stratio Big Data Inc., Sucursal en España.
 */

package com.stratio.multihdfs.services;

import com.stratio.multihdfs.dto.TenantResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TenantIdentityService {


    public List<TenantResponse> getAll(){
        log.debug("Getting tenants");

        TenantResponse tenantResponse = new TenantResponse("a","b", new ArrayList<>(Arrays.asList("1","2")), new ArrayList<>(Arrays.asList("1","2")));

        List<String> a = new ArrayList<>(Arrays.asList("1","2"));
        List<String> b = new ArrayList<>(Arrays.asList("1","2"));

        return new ArrayList<TenantResponse>(Arrays.asList(new TenantResponse("a","b", a, b)));
    }


}
