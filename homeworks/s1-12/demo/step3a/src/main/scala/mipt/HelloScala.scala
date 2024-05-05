package mipt

import cats.Show
import cats.syntax.all._

object HelloScala extends App {

    case class DataToShow(index: Int, name: String)

    val testData = DataToShow(123, "Nice name")

    implicit val dataShow: Show[DataToShow] = Show.show(data => s"test data has name of '${data.name}'")

    println(testData.show)

}
