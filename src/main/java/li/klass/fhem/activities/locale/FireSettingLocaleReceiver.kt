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

package li.klass.fhem.activities.locale

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import li.klass.fhem.AndFHEMApplication
import li.klass.fhem.constants.Actions
import li.klass.fhem.constants.BundleExtraKeys
import li.klass.fhem.service.intent.ConnectionsIntentService
import li.klass.fhem.service.intent.SendCommandService
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class FireSettingLocaleReceiver : BroadcastReceiver() {

    @Inject
    lateinit var sendCommandService: SendCommandService

    init {
        AndFHEMApplication.getApplication().daggerComponent.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.getStringExtra(BundleExtraKeys.ACTION)
        val command = intent.getStringExtra(BundleExtraKeys.COMMAND)
        val connectionId = intent.getStringExtra(BundleExtraKeys.CONNECTION_ID)

        Log.i(TAG, "action=$action,command=$command,connectionId=$connectionId")


        if (Actions.EXECUTE_COMMAND == action) {
            doAsync {
                sendCommandService.executeCommand(command, connectionId, context)
            }
        } else if (Actions.CONNECTION_UPDATE == action) {
            context.startService(Intent(Actions.CONNECTION_SET_SELECTED)
                    .setClass(context, ConnectionsIntentService::class.java)
                    .putExtra(BundleExtraKeys.CONNECTION_ID, connectionId))
        }
    }

    companion object {
        val TAG = FireSettingLocaleReceiver::class.java.name!!
    }
}
