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

package li.klass.fhem.util.device

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.widget.EditText
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import li.klass.fhem.R
import li.klass.fhem.constants.Actions
import li.klass.fhem.devices.backend.DeviceService
import li.klass.fhem.devices.list.favorites.backend.FavoritesService
import li.klass.fhem.domain.core.FhemDevice
import li.klass.fhem.service.NotificationService
import org.jetbrains.anko.coroutines.experimental.bg
import javax.inject.Inject

class DeviceActionUIService @Inject constructor(
        private val deviceService: DeviceService,
        private val notificationService: NotificationService,
        private val favoritesService: FavoritesService

) {

    fun renameDevice(context: Context, device: FhemDevice) {
        val input = EditText(context)
        input.setText(device.name)
        dialogBuilder(context)
                .setTitle(R.string.context_rename)
                .setView(input)
                .setPositiveButton(R.string.okButton) { _, _ ->
                    val newName = input.text.toString()
                    async(UI) {
                        bg {
                            deviceService.renameDevice(device, newName)
                            notificationService.rename(device.name, newName, context)
                            favoritesService.rename(device.name, newName)
                        }.await()
                        invokeUpdate(context)
                    }
                }.setNegativeButton(R.string.cancelButton) { _, _ -> }.show()
    }

    fun deleteDevice(context: Context, device: FhemDevice) {
        showConfirmation(context, DialogInterface.OnClickListener { _, _ ->
            async(UI) {
                bg {
                    deviceService.deleteDevice(device)
                }.await()
                invokeUpdate(context)
            }
        }, context.getString(R.string.deleteConfirmation))
    }

    fun moveDevice(context: Context, device: FhemDevice) {
        val input = EditText(context)
        input.setText(device.roomConcatenated)
        dialogBuilder(context)
                .setTitle(R.string.context_move)
                .setView(input)
                .setPositiveButton(R.string.okButton) { _, _ ->
                    val newRoom = input.text.toString()
                    async(UI) {
                        bg {
                            deviceService.moveDevice(device, newRoom, context)
                        }.await()
                        invokeUpdate(context)
                    }
                }.setNegativeButton(R.string.cancelButton) { _, _ -> }.show()
    }

    fun setAlias(context: Context, device: FhemDevice) {
        val input = EditText(context)
        input.setText(device.alias)
        dialogBuilder(context)
                .setTitle(R.string.context_alias)
                .setView(input)
                .setPositiveButton(R.string.okButton) { _, _ ->
                    val newAlias = input.text.toString()
                    async(UI) {
                        bg {
                            deviceService.setAlias(device, newAlias)
                        }.await()
                        invokeUpdate(context)
                    }
                }.setNegativeButton(R.string.cancelButton) { _, _ -> }.show()
    }

    private fun showConfirmation(context: Context, positiveOnClickListener: DialogInterface.OnClickListener, text: String) {
        dialogBuilder(context)
                .setTitle(R.string.areYouSure)
                .setMessage(text)
                .setPositiveButton(R.string.okButton, positiveOnClickListener)
                .setNegativeButton(R.string.cancelButton) { _, _ -> context.sendBroadcast(Intent(Actions.DO_UPDATE)) }.show()
    }

    private fun dialogBuilder(context: Context): AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.alertDialog)

    private fun invokeUpdate(context: Context) {
        context.sendBroadcast(Intent(Actions.DO_UPDATE))
    }
}
