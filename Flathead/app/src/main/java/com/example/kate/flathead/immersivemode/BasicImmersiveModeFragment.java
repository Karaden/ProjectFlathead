/*
* Copyright (C) 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.example.kate.flathead.immersivemode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class BasicImmersiveModeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        setImmersiveMode();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int i) {
                        int height = decorView.getHeight();
                    }
                });
    }

    /**
     * Detects and toggles immersive mode.
     */
    public void setImmersiveMode() {
        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.

        int uiOptions = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        // END_INCLUDE (get_current_ui_flags)

        // BEGIN_INCLUDE (toggle_ui_flags)

        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all the flags are being toggled together.
        // This uses the "sticky" form of immersive mode, which will let the user swipe
        // the bars back in again, but will automatically make them disappear a few seconds later.

        uiOptions ^= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        uiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        uiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getActivity().getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        //END_INCLUDE (set_ui_flags)
    }


}
