package io.github.seckillPro.disruptor

import com.lmax.disruptor.ExceptionHandler
import com.typesafe.scalalogging.LazyLogging

/**
 * Disruptor异常处理
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
object SeckillExceptionHandler extends ExceptionHandler[SeckillMessage] with LazyLogging {

  override def handleEventException(throwable: Throwable, l: Long, t: SeckillMessage): Unit = {
    logger.error(s"exception when goodsId: [${t.goodsId}], seckillUser: [${t.seckillUser.id}], message: [${throwable.getLocalizedMessage}]", throwable)
  }

  override def handleOnShutdownException(throwable: Throwable): Unit = {
    logger.error(throwable.getLocalizedMessage, throwable)
  }

  override def handleOnStartException(throwable: Throwable): Unit = {
    logger.error(throwable.getLocalizedMessage, throwable)
  }
}