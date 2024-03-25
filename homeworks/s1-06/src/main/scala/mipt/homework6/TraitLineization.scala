package mipt.homework6

object TraitLineization {

  type MethodDef = String
  case class TraitDef(parents: List[TraitDef], methods: Map[String, MethodDef])

  /**
   * Реализуйте алгоритм линеизации цепочки наследования типов и определения метода для вызова из корректного типа
   */
  def resolveMethod(traitDef: TraitDef, method: String): Option[MethodDef] = ???

}
