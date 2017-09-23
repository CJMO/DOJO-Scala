package models

import play.api.libs.json.{Json, Reads, Writes}

/* Forma alternativa de definir clases */
case class Lugar(id:Int, nombre:String,descripcion:Option[String]);

object Lugar {
  implicit val escritura_lugar: Writes[Lugar] = Json.writes[Lugar]
  implicit val lectura_lugar: Reads[Lugar] = Json.reads[Lugar]
}