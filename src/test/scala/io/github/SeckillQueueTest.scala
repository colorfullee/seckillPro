package io.github

import io.github.dreamy.seckill.disruptor.SeckillMessageQueueServer

/**
 *
 * @author 梦境迷离
 * @time 2019-08-15
 * @version v2.0
 */
object SeckillQueueTest extends BaseTest with App {

  (1 to 2).foreach {
    //秒杀所有商品id=1,2,失败回滚库存
    x => SeckillMessageQueueServer.getSeckillProducer().seckill(x, mockSeckillUser)
  }

}
