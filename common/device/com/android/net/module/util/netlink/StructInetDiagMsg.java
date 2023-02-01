/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.android.net.module.util.netlink;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.ByteBuffer;

/**
 * struct inet_diag_msg
 *
 * see &lt;linux_src&gt;/include/uapi/linux/inet_diag.h
 *
 * struct inet_diag_msg {
 *      __u8    idiag_family;
 *      __u8    idiag_state;
 *      __u8    idiag_timer;
 *      __u8    idiag_retrans;
 *      struct  inet_diag_sockid id;
 *      __u32   idiag_expires;
 *      __u32   idiag_rqueue;
 *      __u32   idiag_wqueue;
 *      __u32   idiag_uid;
 *      __u32   idiag_inode;
 * };
 *
 * @hide
 */
public class StructInetDiagMsg {
    public static final int STRUCT_SIZE = 4 + StructInetDiagSockId.STRUCT_SIZE + 20;
    private static final int IDIAG_SOCK_ID_OFFSET = StructNlMsgHdr.STRUCT_SIZE + 4;
    private static final int IDIAG_UID_OFFSET = StructNlMsgHdr.STRUCT_SIZE + 4
            + StructInetDiagSockId.STRUCT_SIZE + 12;
    public int idiag_uid;
    @NonNull
    public StructInetDiagSockId id;

    /**
     * Parse inet diag netlink message from buffer.
     */
    @Nullable
    public static StructInetDiagMsg parse(@NonNull ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() < STRUCT_SIZE) {
            return null;
        }
        StructInetDiagMsg struct = new StructInetDiagMsg();
        final byte family = byteBuffer.get();
        byteBuffer.position(IDIAG_SOCK_ID_OFFSET);
        struct.id = StructInetDiagSockId.parse(byteBuffer, family);
        if (struct.id == null) {
            return null;
        }
        struct.idiag_uid = byteBuffer.getInt(IDIAG_UID_OFFSET);
        return struct;
    }

    @Override
    public String toString() {
        return "StructInetDiagMsg{ "
                + "idiag_uid{" + idiag_uid + "}, "
                + "id{" + id + "}, "
                + "}";
    }
}
