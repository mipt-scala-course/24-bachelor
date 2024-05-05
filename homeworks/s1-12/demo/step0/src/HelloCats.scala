import cats.Show
import cats.syntax.all._

object HelloCats extends App {

    case class ToShow(value: String)

    implicit val toShowShow: Show[ToShow] = Show.show(toShow => s"To Show: ${toShow.value}")

    val toShowValue = ToShow("Some value")

    println(toShowValue.show)

}
