/**
 * © 2019 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 * <p>
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal
 * en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or
 * sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written
 * authorization from Stratio Big Data Inc., Sucursal en España.
 */

package com.stratio.architecture.multihdfs.error;

public class HDFSNotFoundException extends RuntimeException{
    public HDFSNotFoundException(String s) {
        super( s );
    }


}
