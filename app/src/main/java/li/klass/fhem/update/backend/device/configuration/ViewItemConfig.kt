/*
 * AndFHEM - Open Source Android application to control a FHEM home automation
 * server.
 *
 * Copyright (c) 2011, Matthias Klass or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLIC LICENSE, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU GENERAL PUBLIC LICENSE
 * for more details.
 *
 * You should have received a copy of the GNU GENERAL PUBLIC LICENSE
 * along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */

package li.klass.fhem.update.backend.device.configuration

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class ViewItemConfig(
        @SerialName("key")
        val key: String,

        @SerialName("desc")
        val desc: String,

        @SerialName("showAfter")
        @Optional
        val showAfter: String? = null,

        @SerialName("showDelayNotificationOnSwitch")
        @Optional
        val isShowDelayNotificationOnSwitch: Boolean = false,

        @SerialName("showInOverview")
        @Optional
        val isShowInOverview: Boolean = false,

        @SerialName("showInDetail")
        @Optional
        val isShowInDetail: Boolean = false,

        @SerialName("markers")
        @Optional
        val markers: Set<String> = emptySet(),

        @SerialName("beforeCommandReplace")
        @Optional
        val beforeCommandReplacement: Set<Replacement> = emptySet()
) : Serializable
