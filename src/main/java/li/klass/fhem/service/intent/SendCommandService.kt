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

package li.klass.fhem.service.intent

import android.content.Context
import android.content.SharedPreferences
import com.google.common.base.Optional
import com.google.common.base.Preconditions.checkArgument
import li.klass.fhem.service.Command
import li.klass.fhem.service.CommandExecutionService
import li.klass.fhem.service.connection.ConnectionService
import li.klass.fhem.util.preferences.SharedPreferencesService
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

class SendCommandService @Inject constructor(
        val connectionService: ConnectionService,
        val commandExecutionService: CommandExecutionService,
        val sharedPreferencesService: SharedPreferencesService
) {
    fun executeCommand(command: String, connectionId: String? = null, context: Context): String? {
        val result = commandExecutionService.executeSync(Command(command, Optional.fromNullable(connectionId)), context)
        storeRecentCommand(command, context)
        return result
    }

    fun editCommand(oldCommand: String, newCommand: String, context: Context) {
        val commands = getRecentCommands(context)
        val index = commands.indexOf(oldCommand)
        checkArgument(index != -1)
        commands.add(index, newCommand)
        commands.remove(oldCommand)
        storeRecentCommands(commands, context)
    }

    fun deleteCommand(command: String, context: Context) {
        val commands = getRecentCommands(context)
        commands.remove(command)
        storeRecentCommands(commands, context)
    }

    fun getRecentCommands(context: Context): ArrayList<String> {
        val recentCommandsValue = getRecentCommandsPreferences(context).getString(COMMANDS_PROPERTY, null)
        val commandsResult = ArrayList<String>()
        if (recentCommandsValue == null) {
            return commandsResult
        }
        return try {
            val commandsJson = JSONObject(recentCommandsValue).optJSONArray(COMMANDS_JSON_PROPERTY)
            if (commandsJson != null) {
                (0 until commandsJson.length())
                        .mapTo(commandsResult) { commandsJson.get(it).toString() }
            }
            commandsResult
        } catch (e: JSONException) {
            ArrayList()
        }

    }

    private fun storeRecentCommand(command: String, context: Context) {
        val commands = getRecentCommands(context)
        if (commands.contains(command)) {
            commands.remove(command)
        }
        commands.add(0, command)

        storeRecentCommands(commands, context)
    }

    private fun storeRecentCommands(commands: List<String>, context: Context) {
        var mutableCommands = commands
        try {
            if (mutableCommands.size > MAX_NUMBER_OF_COMMANDS) {
                mutableCommands = mutableCommands.subList(0, MAX_NUMBER_OF_COMMANDS)
            }
            val json = JSONObject()
            val commandsJsonArray = JSONArray(mutableCommands)
            json.put(COMMANDS_JSON_PROPERTY, commandsJsonArray)

            getRecentCommandsPreferences(context).edit().putString(COMMANDS_PROPERTY, json.toString()).apply()
        } catch (e: JSONException) {
            LOG.error("cannot store " + mutableCommands, e)
        }

    }

    private fun getRecentCommandsPreferences(context: Context): SharedPreferences =
            sharedPreferencesService.getPreferences(PREFERENCES_NAME, context)

    companion object {
        val PREFERENCES_NAME = "SendCommandStorage"
        val COMMANDS_JSON_PROPERTY = "commands"
        val COMMANDS_PROPERTY = "RECENT_COMMANDS"
        private val MAX_NUMBER_OF_COMMANDS = 10

        private val LOG = LoggerFactory.getLogger(SendCommandService::class.java)
    }
}
