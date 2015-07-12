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

package li.klass.fhem.adapter.devices;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;

import javax.inject.Inject;

import li.klass.fhem.adapter.devices.core.ExplicitOverviewDetailDeviceAdapter;
import li.klass.fhem.adapter.devices.core.FieldNameAddedToDetailListener;
import li.klass.fhem.adapter.devices.genericui.ToggleActionRow;
import li.klass.fhem.adapter.uiservice.StateUiService;
import li.klass.fhem.dagger.ApplicationComponent;
import li.klass.fhem.domain.OwSwitchDevice;
import li.klass.fhem.domain.core.FhemDevice;

public class OwSwitchDeviceAdapter extends ExplicitOverviewDetailDeviceAdapter {
    @Inject
    StateUiService stateUiService;

    public OwSwitchDeviceAdapter() {
        super();
    }

    @Override
    public Class<? extends FhemDevice> getSupportedDeviceClass() {
        return OwSwitchDevice.class;
    }

    @Override
    protected void inject(ApplicationComponent daggerComponent) {
        daggerComponent.inject(this);
    }

    @Override
    protected void afterPropertiesSet() {
        super.afterPropertiesSet();

        registerFieldListener("state", new FieldNameAddedToDetailListener() {
            @Override
            protected void onFieldNameAdded(Context context, TableLayout tableLayout, String field,
                                            FhemDevice device, TableRow fieldTableRow) {
                tableLayout.addView(new OwSwitchToggleRow(getInflater()) {

                    @Override
                    protected boolean isOn(FhemDevice device) {
                        return ((OwSwitchDevice) device).isOnA();
                    }

                    @Override
                    protected int setStateFor(FhemDevice device, boolean isChecked) {
                        return ((OwSwitchDevice) device).setStateForA(isChecked);
                    }
                }.createRow(context, device, "A"));

                tableLayout.addView(new OwSwitchToggleRow(getInflater()) {

                    @Override
                    protected boolean isOn(FhemDevice device) {
                        return ((OwSwitchDevice) device).isOnB();
                    }

                    @Override
                    protected int setStateFor(FhemDevice device, boolean isChecked) {
                        return ((OwSwitchDevice) device).setStateForB(isChecked);
                    }
                }.createRow(context, device, "B"));
            }
        });
    }

    private abstract class OwSwitchToggleRow extends ToggleActionRow {

        public OwSwitchToggleRow(LayoutInflater inflater) {
            super(inflater, ToggleActionRow.LAYOUT_DETAIL);
        }

        @Override
        protected void onButtonClick(Context context, FhemDevice device, boolean isChecked) {
            stateUiService.setSubState(device, "gpio", "" + setStateFor(device, isChecked), context);
        }

        protected abstract int setStateFor(FhemDevice device, boolean isChecked);
    }
}
