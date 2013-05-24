package org.usergrid.vx.eventbus_registry;

import java.util.Map;

/**
 * @author zznate
 */
public interface RegistryMBean {

  Map<String,Long> getHandlers();

  void expireHandler(String address);

}
