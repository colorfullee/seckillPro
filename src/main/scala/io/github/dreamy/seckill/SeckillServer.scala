package io.github.dreamy.seckill

import java.util.concurrent.atomic.AtomicBoolean

import com.google.inject.Injector
import com.typesafe.scalalogging.LazyLogging
import io.github.dreamy.seckill.config.ConfigLoader
import io.github.dreamy.seckill.http.{ AccessLogHandler, Routes, SessionBuilder }
import io.undertow.server.handlers.form.EagerFormParsingHandler
import io.undertow.server.{ HttpHandler, RoutingHandler }
import io.undertow.{ Undertow, UndertowOptions }

/**
 * undertow 服务器
 *
 * @author 梦境迷离
 * @since 2019年8月19日
 * @version v1.0
 */
class SeckillServer(injector: Injector) extends LazyLogging {

  private final lazy val started = new AtomicBoolean(false)
  private final lazy val handlers: Set[http.RoutingHandler] = Routes.getRoutingHandlers(injector, "io.github.dreamy.seckill.handler")
  private final var undertow: Undertow = _
  private final val host = ConfigLoader.getStringValue("seckill.server.host").getOrElse("127.0.0.1")
  private final val port = ConfigLoader.getIntValue("seckill.server.port").getOrElse(80)
  private final val name = ConfigLoader.getStringValue("seckill.server.name").getOrElse("seckill")

  /**
   * 启动server注册handlers
   */
  def startUp() {
    if (started.compareAndSet(false, true)) {
      val handler = new RoutingHandler()
      handlers.foreach { routing: http.RoutingHandler =>
        val routingHandler = routing.asInstanceOf[HttpHandler]
        routing.methods.foreach { method =>
          //巨坑，这个表单解析处理器需要在session之后，并且需要传入router handler
          //session Manager,formData,path,session cookie,formDataParser
          handler.add(method, routing.route, AccessLogHandler(routingHandler, name))
          handler.add(method, routing.route, SessionBuilder.sessionManagerBuild(routingHandler).setNext(new EagerFormParsingHandler(routingHandler)))
        }
      }
      val builder = Undertow.builder()
        .setHandler(handler)
        .addHttpListener(port, host)
        .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, Boolean.box(true))
        .setWorkerThreads(Runtime.getRuntime.availableProcessors())
      undertow = builder.build()
      undertow.start()
      logger.info(s"undertow server start successfully on port: $port")
    }
  }

  /**
   * 暂不考虑
   */
  def shutdown() {
    if (started.compareAndSet(true, false)) {
      if (undertow != null) undertow.stop()
    } else {
      throw new IllegalStateException("server not started")
    }
  }
}