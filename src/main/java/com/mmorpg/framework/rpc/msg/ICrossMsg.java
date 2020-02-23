package com.mmorpg.framework.rpc.msg;

import com.mmorpg.framework.rpc.msg.repair.TempRepairMsg;
import com.mmorpg.framework.rpc.msg.sender.ICrossMsgSender;

/**
 * @author Ariescat
 * @version 2020/2/23 10:38
 */
public abstract class ICrossMsg extends TempRepairMsg implements ICrossBaseMsg<ICrossMsgSender> {
}
