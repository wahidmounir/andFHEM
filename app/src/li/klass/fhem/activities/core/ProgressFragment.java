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

package li.klass.fhem.activities.core;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import li.klass.fhem.constants.BundleExtraKeys;

public class ProgressFragment extends DialogFragment {

    private int title;
    private int content;

    @SuppressWarnings("unused")
    public ProgressFragment() {
    }

    public ProgressFragment(Bundle bundle) {
        title = bundle.getInt(BundleExtraKeys.TITLE);
        content = bundle.getInt(BundleExtraKeys.CONTENT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        ProgressDialog dialog = new ProgressDialog(getActivity());

        if (title != 0) dialog.setTitle(title);
        if (content != 0) dialog.setMessage(getString(content));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        hideDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideDialog();
    }

    private void hideDialog() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
