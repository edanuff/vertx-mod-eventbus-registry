package org.usergrid.vx.eventbus_registry;

/**
 * @author zznate
 */
public class RegistryUtils {


  public static   boolean isExpired(Long age, long expiration_age) {
      if ((expiration_age > 0) && (age != null)) {
        // verify age + expiration age is still greater than now
        return System.currentTimeMillis() < (age.longValue() + expiration_age);
      }
      // on no expiration, simpy verify we are registered
      return age != null;
    }

}
