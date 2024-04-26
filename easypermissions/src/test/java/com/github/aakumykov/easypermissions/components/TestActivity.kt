/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.aakumykov.easypermissions.components

import android.app.Activity
import com.github.aakumykov.easypermissions.EasyPermissions
import com.github.aakumykov.easypermissions.annotations.AfterPermissionGranted
import com.github.aakumykov.easypermissions.annotations.Mockable

@Mockable
class TestActivity :
    Activity(),
    EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    companion object {
        const val REQUEST_CODE = 1
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {}

    override fun onRationaleAccepted(requestCode: Int) {}

    override fun onRationaleDenied(requestCode: Int) {}

    @AfterPermissionGranted(REQUEST_CODE)
    fun afterPermissionGranted() {}
}
