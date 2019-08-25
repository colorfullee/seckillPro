package io.github.dreamy.seckill.presenter

import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.util.{ ImplicitUtils, MD5Utils }
import play.api.libs.json.{ Json, Writes }

/**
 * 商品详情
 *
 * presenter用于展示
 *
 * @author 梦境迷离
 * @since 2019-08-25
 * @version v1.0
 */
case class GoodsDetailPresenter (
                                  /**
                                   * 秒杀状态
                                   */
                                  seckillStatus: Int,

                                  /**
                                   * 遗留时间
                                   */
                                  remainSeconds: Long,

                                  /**
                                   * 商品视图对象
                                   */
                                  goodsVo: GoodsVo,

                                  /**
                                   * 秒杀用户
                                   */
                                  user: SeckillUser,
                                )

object GoodsDetailPresenter {

  implicit val writer: Writes[GoodsDetailPresenter] = (goodsDetailPresenter: GoodsDetailPresenter) => {

    val user = Json.obj(
      //不显示密码和盐，注册时间
      "id" -> MD5Utils.md5(goodsDetailPresenter.user.id.getOrElse(-1L).toString),
      "nickname" -> goodsDetailPresenter.user.nickname,
      "head" -> goodsDetailPresenter.user.head,
      "lastLoginDate" -> ImplicitUtils.toStr(goodsDetailPresenter.user.lastLoginDate))

    val goodsVo = Json.obj(
      //含秒杀库存，秒杀价格，不显示实际总库存
      "id" -> MD5Utils.md5(goodsDetailPresenter.goodsVo.goods.id.getOrElse(-1).toString),
      "goodsName" -> goodsDetailPresenter.goodsVo.goods.goodsName,
      "goodsTitle" -> goodsDetailPresenter.goodsVo.goods.goodsTitle,
      "goodsImg" -> goodsDetailPresenter.goodsVo.goods.goodsImg,
      "goodsDetail" -> goodsDetailPresenter.goodsVo.goods.goodsDetail,
      "goodsPrice" -> goodsDetailPresenter.goodsVo.goods.goodsPrice,
      "stockCount" -> goodsDetailPresenter.goodsVo.stockCount,
      "seckillPrice" -> goodsDetailPresenter.goodsVo.seckillPrice,
      "startDate" -> ImplicitUtils.toStr(goodsDetailPresenter.goodsVo.startDate),
      "endDate" -> ImplicitUtils.toStr(goodsDetailPresenter.goodsVo.endDate)
    )

    Json.obj(
      "seckillStatus" -> goodsDetailPresenter.seckillStatus,
      "remainSeconds" -> goodsDetailPresenter.remainSeconds,
      "user" -> user,
      "goodsVo" -> goodsVo
    )
  }
}
