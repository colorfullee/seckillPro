package io.github.seckillPro.util

import java.util.UUID

/**
 * UUID工具
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
object UUIDUtil {

  val uuid = () => UUID.randomUUID().toString.replace("-", "")

}