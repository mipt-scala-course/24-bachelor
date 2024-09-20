package homework

trait Instances:

  given Eq[Int] = _ == _
  given Eq[String] = _ == _
  given Eq[Boolean] = _ == _
  given Eq[Double] = _ == _
  given Eq[Long] = _ == _
  given Eq[Float] = _ == _

  given [A: Eq]: Eq[Option[A]] =
    case (None, None) => true
    case (Some(first), Some(second)) => summon[Eq[A]].eqv(first, second)
    case _ => false

  given [A: Eq]: Eq[List[A]] with
    def eqv(first: List[A], second: List[A]) =
      first.length == second.length && first.zip(second).forall(summon[Eq[A]].eqv(_, _))

