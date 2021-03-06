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

package li.klass.fhem.domain;

import org.junit.Test;

import li.klass.fhem.domain.core.DeviceXMLParsingBase;
import li.klass.fhem.domain.core.FhemDevice;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenWeatherMapDeviceTest extends DeviceXMLParsingBase {
    @Test
    public void testForCorrectlySetAttributes() {
        FhemDevice device = getDefaultDevice();

        assertThat(device.getName()).isEqualTo(DEFAULT_TEST_DEVICE_NAME);
        assertThat(device.getRoomConcatenated()).isEqualTo(DEFAULT_TEST_ROOM_NAME);

        assertThat(stateValueFor(device, "c_humidity")).isEqualTo("94.0 (%)");
        assertThat(stateValueFor(device, "c_temperature")).isEqualTo("18.1 (°C)");
        assertThat(stateValueFor(device, "c_sunrise")).isEqualTo("2013-09-11 05:06:19");
        assertThat(stateValueFor(device, "c_sunset")).isEqualTo("2013-09-11 17:58:36");
        assertThat(stateValueFor(device, "c_tempMax")).isEqualTo("20.6 (°C)");
        assertThat(stateValueFor(device, "c_tempMin")).isEqualTo("16.7 (°C)");
        assertThat(stateValueFor(device, "c_windDir")).isEqualTo("326.5 (°)");
        assertThat(stateValueFor(device, "c_windSpeed")).isEqualTo("5.31 (km/h)");
    }

    @Override
    protected String getFileName() {
        return "openweathermap.xml";
    }
}
