/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.car.messenger;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothMapClient;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Represents a message obtained via MAP service from a connected Bluetooth device.
 */
class MapMessage {
    private BluetoothDevice mDevice;
    private String mHandle;
    private long mReceivedTimeMs;
    private String mText;
    @Nullable
    private String mSenderContactUri;
    @Nullable
    private String mSenderName;

    /**
     * Constructs Message from {@code intent} that was received from MAP service via
     * {@link BluetoothMapClient#ACTION_MESSAGE_RECEIVED} broadcast.
     *
     * @param intent Intent received from MAP service.
     * @return Message constructed from extras in {@code intent}.
     * @throws IllegalArgumentException If {@code intent} is missing any required fields.
     */
    public static MapMessage parseFrom(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        String handle = intent.getStringExtra(BluetoothMapClient.EXTRA_MESSAGE_HANDLE);
        String senderContactUri = intent.getStringExtra(
                BluetoothMapClient.EXTRA_SENDER_CONTACT_URI);
        String senderContactName = intent.getStringExtra(
                BluetoothMapClient.EXTRA_SENDER_CONTACT_NAME);
        String text = intent.getStringExtra(android.content.Intent.EXTRA_TEXT);
        return new MapMessage(device, handle, System.currentTimeMillis(), text,
                senderContactUri, senderContactName);
    }

    private MapMessage(BluetoothDevice device,
            String handle,
            long receivedTimeMs,
            String text,
            @Nullable String senderContactUri,
            @Nullable String senderName) {
        boolean missingDevice = (device == null);
        boolean missingHandle = (handle == null);
        boolean missingText = (text == null);
        if (missingDevice || missingHandle || missingText) {
            StringBuilder builder = new StringBuilder("Missing required fields:");
            if (missingDevice) {
                builder.append(" device");
            }
            if (missingHandle) {
                builder.append(" handle");
            }
            if (missingText) {
                builder.append(" text");
            }
            throw new IllegalArgumentException(builder.toString());
        }
        mDevice = device;
        mHandle = handle;
        mReceivedTimeMs = receivedTimeMs;
        mText = text;
        mSenderContactUri = senderContactUri;
        mSenderName = senderName;
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public String getHandle() {
        return mHandle;
    }

    public long getReceivedTimeMs() {
        return mReceivedTimeMs;
    }

    public String getText() {
        return mText;
    }

    @Nullable
    public String getSenderContactUri() {
        return mSenderContactUri;
    }

    @Nullable
    public String getSenderName() {
        return mSenderName;
    }

    @Override
    public String toString() {
        return "MapMessage{" +
                "mDevice=" + mDevice +
                ", mHandle='" + mHandle + '\'' +
                ", mReceivedTimeMs=" + mReceivedTimeMs +
                ", mText='" + mText + '\'' +
                ", mSenderContactUri='" + mSenderContactUri + '\'' +
                ", mSenderName='" + mSenderName + '\'' +
                '}';
    }
}
