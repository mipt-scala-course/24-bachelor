package homework

package object task2 {
  type UserId = Long

  object UserId {
    def apply(long: Long): Option[UserId] =
      if (long >= 0)
        Some(long)
      else
        None
  }

  type UserPhone = String

  object UserPhone {
    def apply(s: String): Option[UserPhone] =
      if (s.startsWith("+79") && s.length == 12)
        Some(s)
      else
        None
  }

}
