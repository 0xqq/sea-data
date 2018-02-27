package seadata.ipc.data

import java.sql.Timestamp
import java.time._
import java.util.Date

import helloscala.common.types._

import scala.beans.BeanProperty

trait Cell {
  val value: AnyRef

  def getString: Option[String] = AsString.unapply(value)

  def getByte: Option[Byte] = AsByte.unapply(value)

  def getChar: Option[Char] = AsChar.unapply(value)

  def getShort: Option[Short] = AsShort.unapply(value)

  def getInt: Option[Int] = AsInt.unapply(value)

  def getLong: Option[Long] = AsLong.unapply(value)

  def getFloat: Option[Float] = AsFloat.unapply(value)

  def getDouble: Option[Double] = AsDouble.unapply(value)

  def getDate: Option[Date] = AsDate.unapply(value)

  def getTimestamp: Option[Timestamp] = AsTimestamp.unapply(value)

  def getInstant: Option[Instant] = AsInstant.unapply(value)

  def getLocalDate: Option[LocalDate] = AsLocalDate.unapply(value)

  def getLocalTime: Option[LocalTime] = AsLocalTime.unapply(value)

  def getLocalDateTime: Option[LocalDateTime] = AsLocalDateTime.unapply(value)

  def getZonedDateTime: Option[ZonedDateTime] = AsZonedDateTime.unapply(value)
}

trait IndexCell extends Cell {
  val idx: Int
}

case class ElementCell(
    // 数据例索引号，从0开始
    @BeanProperty idx: Int,
    // 数据值
    @BeanProperty value: AnyRef
) extends IndexCell {

}
